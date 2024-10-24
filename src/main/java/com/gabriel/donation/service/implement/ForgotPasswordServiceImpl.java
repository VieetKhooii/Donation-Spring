package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.ForgotPasswordDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.ForgotPassword;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.ForgotPasswordMapper;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.repository.ForgotPasswordRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    ForgotPasswordRepo forgotPasswordRepo;

    @Override
    public ForgotPasswordDTO findByOtpAndUser(Integer otp, UserDTO userDTO){
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        Optional<ForgotPassword> forgotPasswordOptional = forgotPasswordRepo.findByOtpAndUser(otp, user);
        ForgotPassword forgotPassword = forgotPasswordOptional.get();
        return ForgotPasswordMapper.INSTANCE.toDto(forgotPassword);
    }

    @Override
    public void deleteById(int id)
    {
        if(forgotPasswordRepo.existsById(id))
            forgotPasswordRepo.deleteById(id);
    }

    @Override
    public void saveForgotPassword(ForgotPassword forgotPassword) {
        forgotPasswordRepo.save(forgotPassword);
    }


}
