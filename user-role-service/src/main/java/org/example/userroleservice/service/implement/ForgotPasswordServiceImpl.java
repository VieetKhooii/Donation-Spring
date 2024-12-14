package org.example.userroleservice.service.implement;

import com.gabriel.donation.dto.ForgotPasswordDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.ForgotPassword;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.ForgotPasswordMapper;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.repository.ForgotPasswordRepo;
import com.gabriel.donation.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    ForgotPasswordRepo forgotPasswordRepo;

    @Override
    public ForgotPasswordDTO findByOtpAndUser(Integer otp, UserDTO userDTO){
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        Optional<ForgotPassword> forgotPasswordOptional = forgotPasswordRepo.findByOtpAndUser(otp, user);
        if(forgotPasswordOptional.isPresent()) {
            ForgotPassword forgotPassword = forgotPasswordOptional.get();
            return ForgotPasswordMapper.INSTANCE.toDto(forgotPassword);
        }

        return null;
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

    @Override
    public void deleteExpiredOtps() {
        Instant oneMinuteAgo = Instant.now().minusSeconds(60);
        List<ForgotPassword> expiredOtps = forgotPasswordRepo.findAllByExpirationTimeBefore(Date.from(oneMinuteAgo));
        for (ForgotPassword fp : expiredOtps) {
            forgotPasswordRepo.delete(fp);
        }
    }

}
