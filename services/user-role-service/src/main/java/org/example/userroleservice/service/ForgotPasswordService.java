package org.example.userroleservice.service;


import org.example.userroleservice.dto.ForgotPasswordDTO;
import org.example.userroleservice.dto.UserDTO;
import org.example.userroleservice.entity.ForgotPassword;

public interface ForgotPasswordService {

    ForgotPasswordDTO findByOtpAndUser(Integer otp, UserDTO userDTO);

    void deleteById(int id);

    void saveForgotPassword(ForgotPassword forgotPassword);

    void deleteExpiredOtps();
}
