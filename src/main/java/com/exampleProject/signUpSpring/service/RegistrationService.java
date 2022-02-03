package com.exampleProject.signUpSpring.service;

import com.exampleProject.signUpSpring.domain.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    public String register(RegistrationRequest request) {
        return "ok";
    }
}
