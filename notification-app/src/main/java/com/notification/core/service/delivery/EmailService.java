package com.notification.core.service.delivery;

import com.common.notification.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements IDeliveryChannelService {
    private final JavaMailSender emailSender;
    private final String sender;

    public EmailService(
            JavaMailSender emailSender,
            @Value("${spring.mail.username}") String sender
    ) {
        this.emailSender = emailSender;
        this.sender = sender;
    }

    @Override
    public void sendMessage(MessageDto messageDto) {
        log.info("Sending email to: {}", messageDto.recipient());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(messageDto.recipient());
        message.setSubject(messageDto.subject());
        message.setText(messageDto.content());

        emailSender.send(message);
    }
}
