package africa.semicolon.regcrow.services.notification.mail;


import africa.semicolon.regcrow.dtos.request.EmailNotificationRequest;
import africa.semicolon.regcrow.dtos.request.Recipient;
import africa.semicolon.regcrow.dtos.request.Sender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SendInBlueMailServiceTest {
    @Autowired
    private MailService mailService;

    @Test
    public void testSendMail(){
        Sender sender = new Sender("regcrow inc.", "noreply@regcrow.com");

        Recipient recipient = new Recipient("Tinu", "recelo5446@vaband.com");

        EmailNotificationRequest emailNotificationRequest = new EmailNotificationRequest();
        emailNotificationRequest.setEmailSender(sender);
        emailNotificationRequest.setRecipients(Set.of(recipient));

        emailNotificationRequest.setContent("<p>Hit the ground running</p>");
        emailNotificationRequest.setSubject("Y'ello");

        var response =mailService.sendMail(emailNotificationRequest);

        assertThat(response).isNotNull();

    }
}
