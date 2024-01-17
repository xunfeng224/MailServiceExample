package com.example.mail.enums;

import lombok.Getter;

/**
 * @author xiongfeng
 * @date 2024/1/17 16:02
 */
@Getter
public enum MailEnum {
    REPORT("报告邮件测试", "reportTemplate");
    private final String subject;
    private final String templateName;

    MailEnum(String subject, String templateName) {
        this.subject = subject;
        this.templateName = templateName;
    }
}
