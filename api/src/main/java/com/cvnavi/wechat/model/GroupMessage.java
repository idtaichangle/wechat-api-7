package com.cvnavi.wechat.model;

import java.util.Date;

public class GroupMessage extends Message {

    private Member msgSender;

    public GroupMessage(String msgId, Member from, Member to, MsgType msgType, Date createTime, String content) {
        super(msgId, from, to, msgType, createTime, content);
    }

    public GroupMessage(Member from, Member to, MsgType msgType, Date createTime, String content) {
        super(from, to, msgType, createTime, content);
    }

    public Group getGroup(){
        return (Group)getFrom();
    }

    public Member getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(Member msgSender) {
        this.msgSender = msgSender;
    }
}
