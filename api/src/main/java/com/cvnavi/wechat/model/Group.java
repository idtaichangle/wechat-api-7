package com.cvnavi.wechat.model;

public class Group extends Member {

    private String encryChatRoomId;
    private MemerList memberList=new MemerList();

    public Group(String userName, String nickName, String remarkName) {
        super(userName, nickName, remarkName);
    }

    public String getEncryChatRoomId() {
        return encryChatRoomId;
    }

    public void setEncryChatRoomId(String encryChatRoomId) {
        this.encryChatRoomId = encryChatRoomId;
    }

    public MemerList getMemberList() {
        return memberList;
    }

    public void setMemberList(MemerList memberList) {
        this.memberList = memberList;
    }
}
