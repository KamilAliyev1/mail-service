package main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.dto.MailDTO;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String toEmail,
                         String subject,
                         String body) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);


        try {
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }


        log.info("MAIL:{} SENDING SUCCESSFULLY",message);
    }



    @RabbitListener(queues = "mail-que")
    public void handleNotification(String mail2){
        log.info("GETTING NOTIF:{}",mail2);
        ObjectMapper objectMapper = new ObjectMapper();

        MailDTO mailDTO;
        try {
            mailDTO=objectMapper.readValue(mail2,MailDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            sendMail(mailDTO.toEmail(), mailDTO.subject(), mailDTO.body());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
