package com.example.demowithtests.dto;

import java.time.Instant;
import java.util.Date;

public record AddressDto (

    Long id,

    Boolean addressHasActive,

    String country,

    String city,

    String street,
    Date date
) {
    public AddressDto {
        date = Date.from(Instant.now());
    }
}

/* public void sendEmail(String to, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);

        JavaMailSender.send(message);
    } */