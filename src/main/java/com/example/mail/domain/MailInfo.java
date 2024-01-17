package com.example.mail.domain;

import com.example.mail.enums.MailEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xiongfeng
 * @date 2024/1/17 16:42
 */
@Data
public class MailInfo {
    /**
     * 收件人
     */
    private String toMail;
    /**
     * 邮件类型
     */
    private MailEnum mailEnum;
    /**
     * 模版参数
     */
    private Map<String, Object> param;
    /**
     * 附件path
     */
    private List<String> fileList;
}
