package com.notification.core.service.delivery;

import com.common.kafka.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void sendMessage(NotificationDto notificationDto) {
        log.info("Sending email to: {}", notificationDto.userData());
        List<SimpleMailMessage> messageList = getMessageList(notificationDto);
        messageList.forEach(emailSender::send);
    }

    private List<SimpleMailMessage> getMessageList(NotificationDto notificationDto) {
        return notificationDto.userData()
                .stream()
                .map(email -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(sender);
                    message.setTo(email);
                    message.setSubject(notificationDto.header());
                    message.setText(notificationDto.text());
                    return message;
                })
                .toList();
    }
}
