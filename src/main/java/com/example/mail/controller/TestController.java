package com.example.mail.controller;

import com.example.mail.domain.MailInfo;
import com.example.mail.enums.MailEnum;
import com.example.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import java.util.*;

/**
 * @author xiongfeng
 * @date 2024/1/17 13:48
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private MailService mailService;

    @GetMapping
    public boolean testSendMail(String toMail,String content) {
        String subject = "TestEmail";
//        mailService.sendSimpleMail(subject, toMail, content);
        List<String> fileList = new ArrayList<>();
        fileList.add("C:\\Users\\hf\\Downloads\\bg (1).png");
        fileList.add("C:\\Users\\hf\\Downloads\\bg (2).png");
//        mailService.sendAttachmentsMail(subject, toMail, content, fileList);
        Context context = new Context();
        context.setVariable("id","223");
        context.setVariable("name","张三");
        mailService.sendHtmlMail(subject,toMail,context,"reportTemplate",null);
        return true;
    }

    @GetMapping("test")
    public void test(String toMail,String[] fileList) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailEnum(MailEnum.REPORT);
        mailInfo.setToMail(toMail);
        Map<String, Object> param = new HashMap<>();
        param.put("name",toMail);
        param.put("id","2024");
        mailInfo.setParam(param);
        mailInfo.setFileList(Arrays.asList(fileList));
        try {
            mailService.sendMail(mailInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
