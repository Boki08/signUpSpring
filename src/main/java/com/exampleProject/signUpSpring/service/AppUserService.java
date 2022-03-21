package com.exampleProject.signUpSpring.service;

import com.exampleProject.signUpSpring.domain.AppUser;
import com.exampleProject.signUpSpring.domain.token.ConfirmationToken;
import com.exampleProject.signUpSpring.repo.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(String.format("User with email: %s not found", username)));
    }

    @Transactional
    public String SignUpUser(AppUser appUser){
        if(appUserRepository.findByEmail(appUser.getEmail()).isPresent()){
            AppUser appUserDB = appUserRepository.findByEmail(appUser.getEmail()).get();
            if( !appUserDB.getEnabled()
                    && confirmationTokenService.getTokenByUser(appUserDB).getExpiresAt().isBefore(LocalDateTime.now()))
            {
                String token = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(20), appUserDB);

                confirmationTokenService.saveConfirmationToken(confirmationToken);

                return token;
            }
            else{
                throw new IllegalStateException("Email already taken");
            }
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    @Transactional
    public void enableUser(String username) {
        AppUser appUser = appUserRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(String.format("User with email: %s not found", username)));

        appUser.setEnabled(true);

    }
}
