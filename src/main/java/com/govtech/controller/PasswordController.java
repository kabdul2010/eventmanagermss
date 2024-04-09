package com.govtech.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.dtos.EmailDetail;
import com.govtech.entity.ResetRequest;
import com.govtech.entity.User;
import com.govtech.exception.InvalidInputException;
import com.govtech.exception.UserNotFoundException;
import com.govtech.repository.UserRepository;
import com.govtech.service.IUserService;
import com.govtech.serviceImpl.EmailTriggerService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/password")
public class PasswordController {


    @Autowired
    private IUserService userService;

  

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepo;
    
    
    @Autowired
    private EmailTriggerService emailTriggerService;

    @PostMapping("/forgot/{email}")
    public ResponseEntity<User> processForgotPasswordForm(@PathVariable("email") String userEmail,
                                                          HttpServletRequest request) {

        System.out.println("in forgot");

        if (userEmail == null || userEmail.isEmpty()) {
            throw new InvalidInputException("Email cannot be null or empty");
        }

        Optional<User> optional = userService.findUserByEmail(userEmail);
        if (optional.isEmpty()) {
            throw new UserNotFoundException("Provided Email ID '" + userEmail + "' is Not Associated with any Client");
        }

        if (!optional.isPresent()) {
            return ResponseEntity.ok(new User());
        } else {
            User user = optional.get();
            user.setResetToken(UUID.randomUUID().toString());
            userService.saveUser(user);
            String appUrl = request.getScheme() + "://" + request.getServerName();
            
        
            EmailDetail emailDetail=new EmailDetail();
            
            emailDetail.setContent("To reset your password, click the link below:\n" + appUrl + ":4200/password-change?token="
                    + user.getResetToken() + "&email=" + user.getEmail());
            emailDetail.setToAddress(user.getEmail());
            emailDetail.setSubject("Password Reset Request");
            emailTriggerService.emailTrigger(emailDetail);
            
            
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/reset")
    public String setNewPassword(@RequestBody ResetRequest resetRequest) {

        String resetToken = resetRequest.getToken();
        String newPassword = resetRequest.getPassword();

        // Check if reset token or password is null
        if (resetRequest.getToken() == null || resetRequest.getToken().isEmpty() || resetRequest.getPassword() == null || resetRequest.getPassword().isEmpty()) {
            throw new InvalidInputException("Reset token or password cannot be null or empty.");
        }

        User user = userService.findUserByResetToken(resetToken);
        if (user != null) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            user.setResetToken(resetToken);
            userRepo.save(user);
            return "You Have Changed Your Password Successfully.";
        } else {
            return "Invalid or expired reset token.";
        }
    }

}
