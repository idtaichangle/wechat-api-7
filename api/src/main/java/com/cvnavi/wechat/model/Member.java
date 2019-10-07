package com.cvnavi.wechat.model;

public class Member {
    public String userName="";
    public String nickName="";
    public String remarkName="";

    public Member(String userName, String nickName, String remarkName) {
        this.userName = userName;
        this.nickName = nickName;
        this.remarkName = remarkName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    @Override
    public String toString() {
        return userName+"("+nickName+")";
    }
}
