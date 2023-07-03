package com.example.userservice.service.impl;

import com.example.userservice.domain.Confirmation;
import com.example.userservice.domain.User;
import com.example.userservice.repository.ConfirmationRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.EmailService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

//        TODO send Email to user with token
//        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithAttachment(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedImages(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
//        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
