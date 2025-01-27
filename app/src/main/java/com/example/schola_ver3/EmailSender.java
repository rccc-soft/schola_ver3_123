package com.example.schola_ver3;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "rccc7597@gmail.com";
        final String password = "fsqkmupqhoqtpdds";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }}, new java.security.SecureRandom());

            props.put("mail.smtp.ssl.socketFactory", sslContext.getSocketFactory());
            props.put("mail.smtp.ssl.checkserveridentity", "false");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            Log.d("EmailSender", "Email sent successfully to " + toEmail);
        } catch (Exception e) {
            Log.e("EmailSender", "Error sending email", e);
            e.printStackTrace();
        }
    }
}
//package com.example.schola_ver3;
//
//import android.util.Log;
//
//import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//public class EmailSender {
//    public static void sendEmail(String toEmail, String subject, String body) {
//        final String fromEmail = "rccc7597@gmail.com"; // 送信元メールアドレス
//        final String password = "fsqkmupqhoqtpdds"; // 生成されたアプリパスワード（空白なし）
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true"); // TLSを有効化
//
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(fromEmail, password);
//            }
//        });
//
//        try {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(fromEmail)); // 送信元を設定
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // 送信先を設定
//            message.setSubject(subject); // 件名を設定
//            message.setText(body); // 本文を設定
//
//            Transport.send(message); // メールを送信
//            Log.d("EmailSender", "Email sent successfully to " + toEmail);
//        } catch (MessagingException e) {
//            Log.e("EmailSender", "Error sending email", e);
//            e.printStackTrace();
//        }
//    }
//}