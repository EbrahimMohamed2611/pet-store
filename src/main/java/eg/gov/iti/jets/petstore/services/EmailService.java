package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.ContactUsDTO;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void sendActivationEmail(String activationCode) throws MessagingException, IOException, TemplateException;
    void contactUs(ContactUsDTO contactUsDTO) throws MessagingException, IOException, TemplateException;
}
