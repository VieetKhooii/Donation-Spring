package com.gabriel.donation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.payload.PaymentMethod;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserDonatedService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.service.VNPayService;
import com.gabriel.donation.utils.CookieUtil;
import com.gabriel.donation.utils.DateTimeFormatter;
import com.gabriel.donation.utils.UserDonatedUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
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
    @Autowired
    private UserDonatedUtil userDonatedUtil;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int amount,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("receiverPhone") String phone,
                              @RequestParam("donationPostId") int donationPostId,
                              @RequestParam("anonymous") boolean anonymous,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        UserDonatedDTO userDonatedDTO = userDonatedUtil.setupUserDonatedForTransactions(amount, donationPostId, anonymous, PaymentMethod.VN_PAY, request);

        HttpSession session = request.getSession();
        session.setAttribute("receiverAccount", phone);
        session.setAttribute("userDonatedDTO", userDonatedDTO);

        String vnpayUrl = vnPayService.createOrder(amount, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

//    @PostMapping("/deposit")
//    public String deposit(
//            @RequestParam("amount") int amount,
//            @RequestParam("phone") String phone
//            ){
//
//    }

    @GetMapping("/vnpay-payment")
    public String getMapping(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws JsonProcessingException {

        int paymentStatus =vnPayService.orderReturn(request);

        if (paymentStatus == 1){
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String paymentTime = request.getParameter("vnp_PayDate");
            String transactionId = request.getParameter("vnp_TransactionNo");
            String totalPrice = request.getParameter("vnp_Amount");

            String formattedPaymentTime = dateTimeFormatter.formatDateTimeToString(paymentTime);

            long longParsePrice = Long.parseLong(totalPrice);
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            numberFormat.setMaximumFractionDigits(0);
            String formattedTotalPrice = numberFormat.format(longParsePrice/100);

            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("totalPrice", formattedTotalPrice);
            model.addAttribute("paymentTime", formattedPaymentTime);
            model.addAttribute("transactionId", transactionId);

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
    }
}
