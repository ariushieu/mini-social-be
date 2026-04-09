package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.exception.EmailSendingException;
import com.isocial.minisocialbe.model.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridMailService {
    
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;
    
    @Value("${sendgrid.from-email}")
    private String fromEmail;
    
    @Value("${sendgrid.from-name}")
    private String fromName;
    
    @Value("${app.backend-url}")
    private String backendUrl;

    public void sendVerificationMail(User user, String siteURL) {
        String toAddress = user.getEmail();
        String subject = "Xác thực tài khoản của bạn";
        String verifyURL = backendUrl + "/api/auth/verify?code=" + user.getVerificationCode();
        
        String content = "<p>Chào bạn,</p>"
                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại ISocial Mini Social App. Chỉ còn một bước nữa là bạn có thể bắt đầu.</p>"
                + "<p>Vui lòng click vào nút bên dưới để xác thực địa chỉ email của bạn và kích hoạt tài khoản:</p>"
                + "<p style=\"text-align:center;\">"
                + "  <a href=\"" + verifyURL + "\" style=\"background-color:#007BFF; color:white; padding:10px 20px; text-decoration:none; border-radius:5px; font-weight:bold;\">KÍCH HOẠT NGAY</a>"
                + "</p>"
                + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
                + "<br>"
                + "<hr>"
                + "<br>"
                + "<p>Hello,</p>"
                + "<p>Thank you for registering your account with ISocial Mini Social App. You're just one step away from getting started!</p>"
                + "<p>Please click the button below to verify your email address and activate your account:</p>"
                + "<p style=\"text-align:center;\">"
                + "  <a href=\"" + verifyURL + "\" style=\"background-color:#007BFF; color:white; padding:10px 20px; text-decoration:none; border-radius:5px; font-weight:bold;\">ACTIVATE NOW</a>"
                + "</p>"
                + "<p>If you did not request this, please ignore this email.</p>";

        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toAddress);
        Content emailContent = new Content("text/html", content);
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 400) {
                throw new EmailSendingException("SendGrid API error: " + response.getStatusCode());
            }
        } catch (IOException ex) {
            throw new EmailSendingException("Không thể gửi mail xác thực", ex);
        }
    }
}
