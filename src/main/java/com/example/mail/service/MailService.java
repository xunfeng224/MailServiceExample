package com.example.mail.service;

import com.example.mail.domain.MailInfo;
import com.example.mail.enums.MailEnum;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author xiongfeng
 * @date 2024/1/17 13:43
 */
@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.from.address}")
    private String mailFrom;

    /**
     * 发送简单邮件
     *
     * @param subject
     * @param toMail
     * @param content
     */
    public void sendSimpleMail(String subject, String toMail, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    /**
     * 发送带附件邮件
     *
     * @param subject
     * @param toMail
     * @param content
     * @param fileList
     */
    public void sendAttachmentsMail(String subject, String toMail, String content, List<String> fileList) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(content);
            for (String filePath : fileList) {

                File file = new File(filePath);
                if (!file.exists()) {
                    return;
                }
                helper.addAttachment(file.getName(), file);
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据thymeleaf模版发送邮件
     *
     * @param subject
     * @param toMail
     * @param context
     * @param templateName
     * @param fileList
     */
    public void sendHtmlMail(String subject, String toMail, Context context, String templateName, List<String> fileList) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(toMail);
            helper.setSubject(subject);
            //解析模版
            String text = templateEngine.process(templateName, context);
            helper.setText(text, true);
            if (fileList != null && !fileList.isEmpty()) {
                for (String filePath : fileList) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        throw new FileNotFoundException("文件不存在");
                    }
                    helper.addAttachment(file.getName(), file);
                }
            }
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据邮件类型发送邮件
     *
     * @param mail
     * @return
     */
    public void sendMail(MailInfo mail) throws Exception {
        if (mail.getToMail() == null || mail.getToMail().isEmpty()) {
            throw new Exception("收件人不能为空");
        }
        if (null == mail.getMailEnum()) {
            throw new Exception("邮件类型不能为空");
        }
        MailEnum mailEnum = mail.getMailEnum();
        Context context = new Context();
        context.setVariables(mail.getParam());
        this.sendHtmlMail(mailEnum.getSubject(), mail.getToMail(), context, mailEnum.getTemplateName(), mail.getFileList());
    }


}
