package africa.semicolon.regcrow.services.notification.mail;

import africa.semicolon.regcrow.dtos.request.EmailNotificationRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.SendMailResponse;

public interface MailService {
    SendMailResponse sendMail(EmailNotificationRequest emailNotificationRequest);
}
