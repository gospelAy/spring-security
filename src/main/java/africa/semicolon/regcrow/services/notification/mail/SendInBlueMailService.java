package africa.semicolon.regcrow.services.notification.mail;

import africa.semicolon.regcrow.config.MailConfig;
import africa.semicolon.regcrow.dtos.request.EmailNotificationRequest;
import africa.semicolon.regcrow.dtos.response.SendMailResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static africa.semicolon.regcrow.utils.AppUtils.API_KEY_VALUE;
import static africa.semicolon.regcrow.utils.AppUtils.EMAIL_URL;

@Service
@AllArgsConstructor
@Slf4j
public class SendInBlueMailService implements MailService{
    private final MailConfig mailConfig;
    @Override
    public SendMailResponse sendMail(EmailNotificationRequest emailNotificationRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(API_KEY_VALUE, mailConfig.getMailApiKey());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<EmailNotificationRequest> entity =
                new RequestEntity<>(emailNotificationRequest,httpHeaders, HttpMethod.POST, URI.create(EMAIL_URL));
        ResponseEntity<SendMailResponse> response =
                restTemplate.postForEntity(EMAIL_URL, entity, SendMailResponse.class);

        return response.getBody();
    }
}
