package com.lichong.entity;

import java.util.List;
import java.util.Map;

public class Mail {
    private String from;

    // 收件人
    private List<String> to;

    // 主题
    private String subject;

    // 文本
    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    // 模板名
    private String templateName;

    public Map<String, Object> getContextVar() {
        return contextVar;
    }

    public void setContextVar(Map<String, Object> contextVar) {
        this.contextVar = contextVar;
    }

    // 模板变量
    private Map<String,Object> contextVar;

}
