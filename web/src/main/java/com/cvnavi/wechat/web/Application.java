package com.cvnavi.wechat.web;

import com.cvnavi.wechat.WechatAdapter;
import com.cvnavi.wechat.WechatClient;
import com.cvnavi.wechat.model.GroupMessage;
import com.cvnavi.wechat.model.Member;
import com.cvnavi.wechat.model.Message;
import com.cvnavi.wechat.model.MsgType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class Application {

    public static void showFrame(){

        SwingUtilities.invokeLater(()->{
            WechatClient.INSTANCE.addListener(new WechatAdapter(){

                SimpleDateFormat formater =new SimpleDateFormat("HH:mm:ss");
                @Override
                public void onLoginSuccess(Member user) {
                    System.out.println("["+ formater.format(new Date())+"] login:"+user);
                }

                @Override
                public void onLogout() {
                    System.out.println("["+ formater.format(new Date())+"] logout");
                }

                @Override
                public void onMessage(Message msg) {
                    String log="["+ formater.format(msg.getCreateTime())+"] "+msg.getFrom().getNickName();
                    if(msg instanceof GroupMessage && ((GroupMessage)msg).getMsgSender()!=null){
                        log+=" >> "+((GroupMessage)msg).getMsgSender().getNickName();
                    }
                    log+=": "+msg.getContent();
                    System.out.println(log);
                    if(msg instanceof GroupMessage){
                        GroupMessage msg2=new GroupMessage(null,msg.getFrom(),MsgType.TEXT,null,"收到 "+msg.getContent());
                        //client.sendGroupMessage(msg2);
                    }else{
                        Message msg2=new Message(null,msg.getFrom(),MsgType.TEXT,null,"收到 "+msg.getContent());
                        //client.sendMessage(msg2);
                    }
                }
            });
        });
    }

    public static void main(String[] args) throws Exception {
        showFrame();
        SpringApplication.run(Application.class, args);
    }
}
