package org.example.postimageservice.service;

import com.gabriel.donation.dto.ForgotPasswordDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.ForgotPassword;
import com.gabriel.donation.entity.User;

public interface ForgotPasswordService {

    ForgotPasswordDTO findByOtpAndUser(Integer otp, UserDTO userDTO);

    void deleteById(int id);

    void saveForgotPassword(ForgotPassword forgotPassword);

    void deleteExpiredOtps();
}
