package com.cvnavi.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String loginUrl="http://wx.qq.com";
    static {
        InputStream is=Config.class.getResourceAsStream("/wechat.properties");
        Properties p=new Properties();
        try {
            p.load(is);
            loginUrl=p.getProperty("wechat.login.url");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
