package com.polymeric.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailUtil {

    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
    private static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
    private static final String MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    
    private static final String TLSv = "TLSv1.2";
    private static final String TRUE = "true";
    private static final String TIMEOUT_VALUE = "10000";
    
    private static String EMAIL_HOST;
    private static String EMAIL_PORT;
    private static String USERNAME;
    private static String PASSWORD;
    private static String SENDENAME;

    @Value("${yunmail.host}")
    public void setEmailHost(String emailHost) {
        EMAIL_HOST = emailHost;
    }

    @Value("${yunmail.port}")
    public void setEmailPort(String emailPort) {
        EMAIL_PORT = emailPort;
    }

    @Value("${yunmail.username}")
    public void setUsername(String username) {
        USERNAME = username;
    }

    @Value("${yunmail.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }
    
    @Value("${yunmail.sendeName}")
    public void setSendeName(String sendeName) {
        SENDENAME = sendeName;
    }

    public static String getEmailHost() {
        return EMAIL_HOST;
    }

    public static String getEmailPort() {
        return EMAIL_PORT;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
    
    public static String getSendeName() {
        return SENDENAME;
    }
    
    public static void sendEmail(String toEmail, String title, String content) {
        try {
            Properties props = new Properties();
            // SMTP 服务配置
            props.setProperty(MAIL_SMTP_HOST, EMAIL_HOST);
            props.setProperty(MAIL_SMTP_PORT, EMAIL_PORT);
            
            // SSL 配置
            props.setProperty(MAIL_SMTP_SSL_ENABLE, TRUE);
            props.setProperty(MAIL_SMTP_SSL_PROTOCOLS, TLSv);
            props.setProperty(MAIL_SMTP_SSL_TRUST, EMAIL_HOST);  // 信任SSL证书
            
            // 认证配置
            props.setProperty(MAIL_SMTP_AUTH, TRUE);
            
            // 超时配置
            props.setProperty(MAIL_SMTP_CONNECTIONTIMEOUT, TIMEOUT_VALUE);
            props.setProperty(MAIL_SMTP_TIMEOUT, TIMEOUT_VALUE);
            
            // 可选：针对某些邮件服务器的额外配置
            props.setProperty("mail.smtp.ssl.checkserveridentity", "false");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
            
            // 建立邮件会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });
            
            // 开启debug模式（调试完可关闭）
            // session.setDebug(true);

            // 创建邮件对象
            MimeMessage message = new MimeMessage(session);
            
            // 设置发件人（带自定义名称）
            if (SENDENAME != null && !SENDENAME.trim().isEmpty()) {
                try {
                    // 方式1：标准方式，支持中文
                    message.setFrom(new InternetAddress(USERNAME, SENDENAME, "UTF-8"));
                    
                    // 方式2：同时设置Sender（增强兼容性）
                    message.setSender(new InternetAddress(USERNAME, SENDENAME, "UTF-8"));
                    
                } catch (UnsupportedEncodingException e) {
                    // 编码失败时的备选方案
                    message.setFrom(new InternetAddress(USERNAME));
                }
            } else {
                // 如果没有配置发件人名称，只使用邮箱
                message.setFrom(new InternetAddress(USERNAME));
            }
            
            // 设置回复地址（可选）
            if (SENDENAME != null && !SENDENAME.trim().isEmpty()) {
                message.setReplyTo(new Address[]{
                    new InternetAddress(USERNAME, SENDENAME, "UTF-8")
                });
            } else {
                message.setReplyTo(new Address[]{
                    new InternetAddress(USERNAME)
                });
            }
            
            // 支持多个收件人（以逗号分隔）
            String[] toArray = toEmail.split(",");
            InternetAddress[] sendTo = new InternetAddress[toArray.length];
            for (int i = 0; i < toArray.length; i++) {
                sendTo[i] = new InternetAddress(toArray[i].trim());
            }
            message.setRecipients(Message.RecipientType.TO, sendTo);
            
            // 邮件标题和内容
            message.setSubject(title, "UTF-8");
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            
            // 发送邮件
            Transport.send(message);
            System.out.println("✅ 邮件发送成功！收件人: " + toEmail);
            
        } catch (MessagingException e) {
            System.err.println("❌ 邮件发送失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ 未知异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 重载方法：支持自定义发件人名称（动态覆盖配置）
    public static void sendEmail(String toEmail, String title, String content, String customSenderName) {
        // 临时保存原有发件人名称
        String originalSenderName = SENDENAME;
        try {
            // 使用自定义名称
            if (customSenderName != null && !customSenderName.trim().isEmpty()) {
                SENDENAME = customSenderName;
            }
            sendEmail(toEmail, title, content);
        } finally {
            // 恢复原有配置
            SENDENAME = originalSenderName;
        }
    }

    // 简单测试
    public static void main(String[] args) {
        // 测试前请确保Spring配置已加载，或者手动设置配置值用于测试
        // 如果直接运行测试，需要手动设置以下值：
        // EMAIL_HOST = "smtp.ezmail.vip";
        // EMAIL_PORT = "465";
        // USERNAME = "support@1token.me";
        // PASSWORD = "Aasd147258!@#$";
        // SENDENAME = "OneToken";
        
        String to = "csz15621349306@gmail.com,1299681745@qq.com";
        String title = "充值订单审核通知";
        String content = "<b>测试测试测试，您有一条充值订单需审核</b><br>" +
                "充值人ID：888<br>" +
                "充值时间：2025-08-28 16:00<br>" +
                "充值金额：800 USD<br>" +
                "请尽快前往后台进行审核。";
        
        // 方式1：使用配置文件中的发件人名称
        sendEmail(to, title, content);
        
        // 方式2：临时使用自定义发件人名称
        // sendEmail(to, title, content, "OneToken系统通知");
    }
}