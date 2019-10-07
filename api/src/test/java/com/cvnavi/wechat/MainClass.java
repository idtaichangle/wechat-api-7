package com.cvnavi.wechat;
import com.cvnavi.wechat.model.GroupMessage;
import com.cvnavi.wechat.model.Member;
import com.cvnavi.wechat.model.Message;
import com.cvnavi.wechat.model.MsgType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainClass {

    public static void main(String[] args) {
        WechatClient client=WechatClient.INSTANCE;
        client.addListener(new WechatAdapter() {
            @Override
            public void onLoginSuccess(Member user) {
                System.out.println("login success: "+user.getUserName());
            }

            @Override
            public void onMessage(Message msg) {
                String log="["+msg.getCreateTime()+"] "+msg.getFrom().getNickName();
                if(msg instanceof GroupMessage){
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

        JFrame f=new JFrame();
        f.setSize(1000,800);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.add(client.getBrowserView(),BorderLayout.CENTER);
        JButton btn=new JButton("OK");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getBrowser().mainFrame().ifPresent(mainFrame->{
                    mainFrame.loadUrl(Config.loginUrl);
                });

//                Member s=memberList.findMemberByNickName("Lee");
//                Message msg=new Message(null,s,MsgType.TEXT,null,"哈哈");
//                sendMessage(msg);
            }
        });
        f.add(btn,BorderLayout.NORTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
