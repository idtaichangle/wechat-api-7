package com.cvnavi.wechat.model;

import java.util.Date;

public class Message {
    private String msgId;
    private Member from;
    private Member to;
    private MsgType msgType;
    private Date createTime;
    private String content;
    private Object attachment;

    public Message(String msgId,Member from, Member to, MsgType msgType, Date createTime, String content) {
        this.msgId=msgId;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.createTime = createTime;
        this.content = content;
    }

    public Message(Member from, Member to, MsgType msgType, Date createTime, String content) {
        this("",from,to,msgType,createTime,content);
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Member getFrom() {
        return from;
    }

    public void setFrom(Member from) {
        this.from = from;
    }

    public Member getTo() {
        return to;
    }

    public void setTo(Member to) {
        this.to = to;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
