package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.exception.EmailSendingException;
import com.isocial.minisocialbe.model.User;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResendMailService {

    private final Resend resend;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Value("${resend.from-name}")
    private String fromName;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public ResendMailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendVerificationMail(User user) {
        String verifyURL = frontendUrl + "/verify?code=" + user.getVerificationCode();

        String htmlContent = buildVerificationEmailHtml(user.getFullName(), verifyURL);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromName + " <" + fromEmail + ">")
                .to(user.getEmail())
                .subject("Xác thực tài khoản của bạn - Verify Your Account")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            log.info("Email sent successfully to {}. Email ID: {}", user.getEmail(), response.getId());
        } catch (ResendException e) {
            log.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage());
            throw new EmailSendingException("Không thể gửi email xác thực", e);
        }
    }

    private String buildVerificationEmailHtml(String fullName, String verifyURL) {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">"
                + "<h2 style=\"color: #333;\">Xác thực tài khoản / Verify Your Account</h2>"
                + "<p>Chào " + fullName + ",</p>"
                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại ISocial Mini Social App. Chỉ còn một bước nữa là bạn có thể bắt đầu.</p>"
                + "<p>Vui lòng click vào nút bên dưới để xác thực địa chỉ email của bạn và kích hoạt tài khoản:</p>"
                + "<p style=\"text-align:center; margin: 30px 0;\">"
                + "  <a href=\"" + verifyURL + "\" style=\"background-color:#007BFF; color:white; padding:12px 30px; text-decoration:none; border-radius:5px; font-weight:bold; display:inline-block;\">KÍCH HOẠT NGAY / ACTIVATE NOW</a>"
                + "</p>"
                + "<p style=\"color: #666; font-size: 14px;\">Nếu nút không hoạt động, copy link này vào trình duyệt:</p>"
                + "<p style=\"color: #007BFF; word-break: break-all;\">" + verifyURL + "</p>"
                + "<hr style=\"border: none; border-top: 1px solid #eee; margin: 30px 0;\">"
                + "<p>Hello " + fullName + ",</p>"
                + "<p>Thank you for registering your account with ISocial Mini Social App. You're just one step away from getting started!</p>"
                + "<p>Please click the button above to verify your email address and activate your account.</p>"
                + "<p style=\"color: #999; font-size: 12px; margin-top: 30px;\">If you did not request this, please ignore this email.</p>"
                + "</div>";
    }
}
