package com.crossover.trial.journals.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by YongSengChia on 12/21/2016.
 */
@Configuration
public class MailConfig implements EnvironmentAware {

    private static final String HOST = "mail.server.host";
    private static final String PORT = "mail.server.port";
    private static final String PROTOCOL = "mail.server.protocol";
    private static final String USERNAME = "mail.server.username";
    private static final String PASSWORD = "mail.server.password";

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Bean
    public JavaMailSender mailSender() {
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(env.getProperty(HOST));
        sender.setPort(Integer.parseInt(env.getProperty(PORT)));
        sender.setProtocol(env.getProperty(PROTOCOL));
        sender.setUsername(env.getProperty(USERNAME));
        sender.setPassword(env.getProperty(PASSWORD));

        // properties for google's smtp settings
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        sender.setJavaMailProperties(properties);

        return sender;
    }
}
