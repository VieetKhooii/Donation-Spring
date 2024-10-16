package com.gabriel.donation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.payload.PaymentMethod;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserDonatedService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.service.VNPayService;
import com.gabriel.donation.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping("/vnpay")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDonatedService userDonatedService;
    @Autowired
    private DonationPostService donationPostService;
    @Autowired
    private CookieUtil cookieUtil;

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("receiver") String receiver,
                              @RequestParam("donationPostId") int donationPostId,
                              @RequestParam("anonymous") boolean anonymous,
                              HttpServletRequest request) throws JsonProcessingException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        System.out.println("Anonymous: "+anonymous);
        Cookie[] cookies = request.getCookies();

        UserDonatedDTO userDonatedDTO = new UserDonatedDTO();
        try {
            String cookieValueEncoded = cookieUtil.getCookieValue(cookies, String.valueOf(CookieName.userInfo));
            UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(cookieValueEncoded);
            userDonatedDTO.setUserId(userDTO.getUserId());
            userDonatedDTO.setDonationPostId(donationPostId);
            userDonatedDTO.setAmount(orderTotal);
            userDonatedDTO.setAnonymous(anonymous);
            userDonatedDTO.setDeleted(false);
            userDonatedDTO.setPaymentMethod(PaymentMethod.VN_PAY);
            userDonatedDTO.setDonateDate(new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("User must be logged in to submit an order.");
        }

        System.out.println("User ID: " + userDonatedDTO.getUserId());
        System.out.println("Donation Post ID: " + userDonatedDTO.getDonationPostId());
        System.out.println("Amount: " + userDonatedDTO.getAmount());
        System.out.println("Anonymous: " + userDonatedDTO.isAnonymous());
        System.out.println("Donate Date: " + userDonatedDTO.getDonateDate());
        System.out.println("Is Deleted: " + userDonatedDTO.isDeleted());

        HttpSession session = request.getSession();

        session.setAttribute("receiverAccount", receiver);
        session.setAttribute("userDonatedDTO", userDonatedDTO);

        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String getMapping(
            HttpServletRequest request,
            Model model){
        try {
            int paymentStatus =vnPayService.orderReturn(request);

            String orderInfo = request.getParameter("vnp_OrderInfo");
            String paymentTime = request.getParameter("vnp_PayDate");
            String transactionId = request.getParameter("vnp_TransactionNo");
            String totalPrice = request.getParameter("vnp_Amount");

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(paymentTime, inputFormatter);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedPaymentTime = dateTime.format(dateTimeFormatter);
            Date formattedPaymentDate = new Date(Long.parseLong(paymentTime));


            long longParsePrice = Long.parseLong(totalPrice);
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            numberFormat.setMaximumFractionDigits(0);
            String formattedTotalPrice = numberFormat.format(longParsePrice/100);

            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("totalPrice", formattedTotalPrice);
            model.addAttribute("paymentTime", formattedPaymentTime);
            model.addAttribute("transactionId", transactionId);

            if (paymentStatus == 1){
                HttpSession session = request.getSession();
                String receiverPhone = (String) session.getAttribute("receiverAccount");
                UserDonatedDTO userDonatedDTO = (UserDonatedDTO) session.getAttribute("userDonatedDTO");

                UserDTO receiverDTO = userService.findByPhone(receiverPhone);
                receiverDTO.setBalance(receiverDTO.getBalance()+userDonatedDTO.getAmount());
                userService.updateUser(receiverDTO, receiverDTO.getUserId());

                int donatePersonId = userDonatedDTO.getUserId();
                userDonatedService.processDonation(userDonatedDTO, donatePersonId);
            }

            return paymentStatus == 1 ? "/transaction/order-success" : "/transaction/order-fail";
        } catch (Exception e) {
            System.out.println("helooooo");
            return "/transaction/order-fail";
        }
    }
}
