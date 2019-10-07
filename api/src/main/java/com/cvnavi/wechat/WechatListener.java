package com.cvnavi.wechat;

import com.cvnavi.wechat.model.Member;
import com.cvnavi.wechat.model.MemerList;
import com.cvnavi.wechat.model.Message;

public interface WechatListener {
    public void onLoginSuccess(Member user);
    public void onLogout();
    public void onContactLoaded(MemerList memers);
    public void onMessage(Message msg);
}
