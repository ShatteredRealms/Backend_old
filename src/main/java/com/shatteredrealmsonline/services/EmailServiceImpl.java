package com.shatteredrealmsonline.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailServiceImpl implements MailSender {

    @Qualifier("getJavaMailSender")
    @Autowired
    private JavaMailSender emailSender;

    public void sendAdminMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@shatteredrealmsonline.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        send(message);
    }

    @Override
    public void send(@NotNull SimpleMailMessage simpleMessage) throws MailException {
        emailSender.send(simpleMessage);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        for(SimpleMailMessage message : simpleMessages)
            emailSender.send(message);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.shatteredrealmsonline.com");
        mailSender.setPort(25);

        mailSender.setUsername("no-reply@shatteredrealmsonline.com");
        mailSender.setPassword("15987ShatteredRealmsDev!");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.shatteredrealmsonline.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;

        /*
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("teamcs786@gmail.com");
        mailSender.setPassword("czujfhilmgrfgczw");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
         */
    }
}
