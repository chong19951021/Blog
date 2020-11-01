package com.lichong.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {
     private Long id;
     private Long parentMessageId;

    public String getParentNickname() {
        return parentNickname;
    }

    public void setParentNickname(String parentNickname) {
        this.parentNickname = parentNickname;
    }

    private String nickname;
     private String parentNickname;
     private String email;
     private String content;
     private String avatar;
     private Date createTime;
     private Boolean adminMessage;

    public List<Message> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Message> replyComments) {
        this.replyComments = replyComments;
    }

    private List<Message> replyComments = new ArrayList<>();
    private com.lichong.entity.Message parentMessage;

    public Message getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    public Boolean getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(Boolean adminMessage) {
        this.adminMessage = adminMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(Long parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
