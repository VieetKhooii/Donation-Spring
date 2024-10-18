package com.gabriel.donation.utils;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.payload.PaymentMethod;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserDonatedUtil {

    @Autowired
    CookieUtil cookieUtil;

    public UserDonatedDTO setupUserDonatedForTransactions(int orderTotal, int donationPostId, boolean anonymous, PaymentMethod paymentMethod, HttpServletRequest request) {
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
            userDonatedDTO.setPaymentMethod(paymentMethod);
            userDonatedDTO.setDonateDate(new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("User must be logged in to submit an order.");
        }
        return userDonatedDTO;
    }
}
