package com.gabriel.donation.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.payload.PaymentMethod;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserDonatedService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.service.VNPayService;
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
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDonatedService userDonatedService;
    @Autowired
    private DonationPostService donationPostService;

    @GetMapping("/vnpay")
    public String vnPay() {
        return "/vnpay/transaction-test";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("receiver") String receiver,
                              @ModelAttribute UserDonatedDTO userDonatedDTO,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        userDonatedDTO.setPaymentMethod(PaymentMethod.VN_PAY);
        userDonatedDTO.setDonationPostId(1);

        HttpSession session = request.getSession();
        userDonatedDTO.setDonateDate(new Date());
        session.setAttribute("receiverAccount", receiver);
        session.setAttribute("userDonatedDTO", userDonatedDTO);

        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String getMapping(
            HttpServletRequest request,
            Model model){
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

            int donatePersonId = (int) session.getAttribute("userId");
            userDonatedService.processDonation(userDonatedDTO, donatePersonId);
        }

        return paymentStatus == 1 ? "/vnpay/order-success" : "/vnpay/order-fail";
    }
}
