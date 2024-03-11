package io.github.alexwu727.authsystemauthenticationservice.util;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class CodeUtil {

    private final JavaMailSender mailSender;
    public String generateCode() {
        return generateCode(6);
    }

    public String generateCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder verificationCode = new StringBuilder(String.valueOf(random.nextInt((int) Math.pow(10, length))));
        while (verificationCode.length() < length) {
            verificationCode.insert(0, "0");
        }
        return verificationCode.toString();
    }

    public void sendCode(String username, String email, String code, boolean isVerification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("auth.system.io@gmail.com");
        message.setTo("alexwu727@gmail.com");
//        message.setTo(email);
        String subject = isVerification ? "Verification" : "Password Reset";
        message.setSubject(subject + " Code");
        // hello alexwu727, your verification code is: 123456
        //
        // or you can click the link below to verify your email
        //
        // http://localhost:8080/api/v1/auth/verify?email=alexwu727&verificationCode=123456

        String text = """
                hello %s, your %s code is: %s
                
                or you can click the link below to verify your email
                
                http://localhost:3000/verify-result?email=%s&code=%s
                """;
        message.setText(String.format(text, username, subject, code, email, code));


        mailSender.send(message);
    }
}
