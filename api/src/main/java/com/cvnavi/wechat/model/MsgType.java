package com.cvnavi.wechat.model;

public enum MsgType {
    TEXT(1), IMAGE(3), VOICE(34),VERIFYMSG(37),POSSIBLEFRIEND_MSG(40),SHARECARD(42),
    VIDEO(43),EMOTION(47),LOCATION(48), APP(49),VOIPMSG(50),STATUSNOTIFY(51),
    VOIPNOTIFY(52),VOIPINVITE(53), MICROVIDEO(62),SYSNOTICE(9999),SYS(10000),RECALLED(10002);

    int type;
    MsgType(int type){
        this.type=type;
    }

    public static MsgType parse(int type){
        for(MsgType t:MsgType.values()){
            if(t.getIntValue()==type){
                return t;
            }
        }
        return TEXT;
    }

    public int getIntValue(){
        return type;
    }

}
