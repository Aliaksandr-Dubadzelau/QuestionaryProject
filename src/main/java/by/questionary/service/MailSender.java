package by.questionary.service;

public interface MailSender {

    void send(String mailTo, String subject, String message);

}
