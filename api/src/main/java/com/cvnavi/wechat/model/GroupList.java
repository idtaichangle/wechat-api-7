package com.cvnavi.wechat.model;

import java.util.ArrayList;

public class GroupList extends ArrayList<Group> {

    public Group findGroupByUserName(String userName){
        for(Group gc:this){
            if(gc.getUserName().equals(userName)){
                return gc;
            }
        }
        return null;
    }
}
