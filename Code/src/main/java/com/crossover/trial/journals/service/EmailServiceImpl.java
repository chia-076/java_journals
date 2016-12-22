package com.crossover.trial.journals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by YongSengChia on 12/21/2016.
 */
@Service
public class EmailServiceImpl implements EmailService {

    protected static final String EMAIL_TEMPLATE_FROM = "mail.template.from";
    protected static final String EMAIL_TEMPLATE_SUBJECT = "mail.template.subject";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    Environment env;

    @Override
    public void sendPlainText(String recipientEmail, String plainText) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setSubject(env.getProperty(EMAIL_TEMPLATE_SUBJECT));
        helper.setFrom(env.getProperty(EMAIL_TEMPLATE_FROM));
        helper.setTo(recipientEmail);
        helper.setText(plainText);

        mailSender.send(mimeMessage);
    }
}
