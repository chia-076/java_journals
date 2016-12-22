package com.crossover.trial.journals.service;

import javax.mail.MessagingException;

/**
 * Created by YongSengChia on 12/21/2016.
 */
public interface EmailService {
    void sendPlainText(String recipientEmail, String plainText) throws MessagingException;
}
