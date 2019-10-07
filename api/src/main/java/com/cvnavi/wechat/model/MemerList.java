package com.cvnavi.wechat.model;

import java.util.ArrayList;

public class MemerList extends ArrayList<Member> {
    public Member findMemberByUserName(String userName) {
        for(Member m: this){
            if(m.getUserName().equals(userName)){
                return  m;
            }
        }
        return  null;
    }
    public Member findMemberByNickName(String nickName) {
        for(Member m: this){
            if(m.getNickName().equals(nickName)){
                return  m;
            }
        }
        return  null;
    }
}
