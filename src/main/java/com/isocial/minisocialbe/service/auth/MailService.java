package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "hieutrumai@gmail.com";
        String senderName = "Mini Social Network - iSocial";
        String subject = "Xác thực tài khoản của bạn";
        String content = "<p>Chào bạn,</p>"
                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại ISocial Mini Social App. Chỉ còn một bước nữa là bạn có thể bắt đầu.</p>"
                + "<p>Vui lòng click vào nút bên dưới để xác thực địa chỉ email của bạn và kích hoạt tài khoản:</p>"
                + "<p style=\"text-align:center;\">"
                + "  <a href=\"[[URL]]\" style=\"background-color:#007BFF; color:white; padding:10px 20px; text-decoration:none; border-radius:5px; font-weight:bold;\">KÍCH HOẠT NGAY</a>"
                + "</p>"
                + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
                + "<br>"
                + "<hr>"
                + "<br>"
                + "<p>Hello,</p>"
                + "<p>Thank you for registering your account with ISocial Mini Social App. You're just one step away from getting started!</p>"
                + "<p>Please click the button below to verify your email address and activate your account:</p>"
                + "<p style=\"text-align:center;\">"
                + "  <a href=\"[[URL]]\" style=\"background-color:#007BFF; color:white; padding:10px 20px; text-decoration:none; border-radius:5px; font-weight:bold;\">ACTIVATE NOW</a>"
                + "</p>"
                + "<p>If you did not request this, please ignore this email.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress,  senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = siteURL + "/api/auth/verify?code=" + user.getVerificationCode();
        content = content.replace("[[name]]", user.getFullName());
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
