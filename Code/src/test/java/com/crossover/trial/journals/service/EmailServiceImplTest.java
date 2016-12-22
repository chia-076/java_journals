package com.crossover.trial.journals.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.crossover.trial.journals.service.EmailServiceImpl.EMAIL_TEMPLATE_FROM;
import static com.crossover.trial.journals.service.EmailServiceImpl.EMAIL_TEMPLATE_SUBJECT;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by YongSengChia on 12/22/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceImplTest {
    @InjectMocks
    EmailServiceImpl emailService;

    @Mock
    Environment env;

    @Mock
    JavaMailSender mailSender;

    @Mock
    MimeMessage mimeMessage;

    @Test
    public void testSendPlainText() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(env.getProperty(EMAIL_TEMPLATE_SUBJECT)).thenReturn("test subject");
        when(env.getProperty(EMAIL_TEMPLATE_FROM)).thenReturn("sender");

        emailService.sendPlainText("test1@email.com", "simple plaintext");

        verify(mailSender, times(1)).createMimeMessage();
        verify(env, times(2)).getProperty(anyString());
        verify(env, times(1)).getProperty(EMAIL_TEMPLATE_FROM);
        verify(env, times(1)).getProperty(EMAIL_TEMPLATE_SUBJECT);
        verify(mailSender, times(1)).send(mimeMessage);
        verifyZeroInteractions(mailSender, env);
    }
}