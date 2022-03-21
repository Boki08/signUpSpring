package com.exampleProject.signUpSpring.domain;

public interface EmailSender {
    void send(String to, String email);
}
