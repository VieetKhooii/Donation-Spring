package org.example.paymentuserdonatedservice.controller;

import com.gabriel.donation.dto.ForgotPasswordDTO;
import com.gabriel.donation.dto.MailBody;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.ForgotPassword;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.EmailService;
import com.gabriel.donation.service.ForgotPasswordService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.util.ChangePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    //send mail to email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){

        forgotPasswordService.deleteExpiredOtps();

        UserDTO userDTO = Optional.ofNullable(userService.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email"));

        User user = UserMapper.INSTANCE.toEntity(userDTO);

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request :  " +  otp)
                .subject("OTP for Forgot Password request")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() +70 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordService.saveForgotPassword(fp);

        return ResponseEntity.ok( "Email sent for verification");

    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){

        UserDTO userDTO = Optional.ofNullable(userService.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email"));

        ForgotPasswordDTO fp = Optional.ofNullable(forgotPasswordService.findByOtpAndUser(otp, userDTO))
                .orElseThrow(()-> new RuntimeException("Invalid otp for email: " + email));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordService.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email){
        if(!Objects.equals(changePassword.password(), changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userService.updatePassword(email, encodedPassword);
        return ResponseEntity.ok("Password has been changed!");
    }

    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
