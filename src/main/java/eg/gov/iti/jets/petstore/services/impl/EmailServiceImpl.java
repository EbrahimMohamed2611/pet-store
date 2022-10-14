package eg.gov.iti.jets.petstore.services.impl;

import eg.gov.iti.jets.petstore.dto.ContactUsDTO;
import eg.gov.iti.jets.petstore.services.EmailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String ourEmail;

    private final Configuration configuration;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

//    @Override
//    public void sendEmail(User user) throws MessagingException, IOException, TemplateException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//        helper.setSubject("Welcome To Petstore");
//        helper.setTo(user.getEmail());
//        String emailContent = getEmailContent(user);
//        helper.setText(emailContent, true);
//        try {
//            javaMailSender.send(mimeMessage);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }

    private String getEmailContent(ContactUsDTO contactUsDTO) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("contactUsDTO", contactUsDTO);
        configuration.getTemplate("contact-us.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @Override
    public void sendActivationEmail(String activationCode) throws MessagingException, IOException, TemplateException {

    }

    @Override
    public void contactUs(ContactUsDTO contactUsDTO) throws MessagingException, IOException, TemplateException {
        System.out.println("contactUsDTO " +contactUsDTO);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(contactUsDTO.getSubject());
        helper.setTo(ourEmail);
        String emailContent = getEmailContent(contactUsDTO);
        helper.setText(emailContent, true);
        try {
            javaMailSender.send(mimeMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

