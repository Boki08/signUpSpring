package com.exampleProject.signUpSpring.service;

import com.exampleProject.signUpSpring.domain.AppUser;
import com.exampleProject.signUpSpring.domain.token.ConfirmationToken;
import com.exampleProject.signUpSpring.repo.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getToken(String token){
        return confirmationTokenRepository.findByToken(token).orElseThrow(()-> new IllegalStateException("token does not exist"));
    }
    @Transactional
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public ConfirmationToken getTokenByUser(AppUser appUser){
        return confirmationTokenRepository.findByUser(appUser.getId()).orElseThrow(()-> new IllegalStateException("token does not exist"));
    }
}
