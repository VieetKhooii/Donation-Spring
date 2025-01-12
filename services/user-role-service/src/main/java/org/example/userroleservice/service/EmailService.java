package org.example.userroleservice.service;


import org.example.userroleservice.dto.MailBody;

public interface EmailService {
    public void sendSimpleMessage(MailBody mailBody);
}
