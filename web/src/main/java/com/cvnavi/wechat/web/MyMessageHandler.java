package com.cvnavi.wechat.web;

import com.cvnavi.wechat.WechatAdapter;
import com.cvnavi.wechat.WechatClient;
import com.cvnavi.wechat.WechatListener;
import com.cvnavi.wechat.model.GroupMessage;
import com.cvnavi.wechat.model.Member;
import com.cvnavi.wechat.model.MemerList;
import com.cvnavi.wechat.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyMessageHandler extends TextWebSocketHandler implements WechatListener {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public  MyMessageHandler(){
        WechatClient.INSTANCE.addListener(this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
    }

    @Override
    public void onLoginSuccess(Member user) {
        for(WebSocketSession s:sessions){
            HashMap<String,Object> map=new HashMap<>();
            map.put("action","onLoginSuccess");
            map.put("success",true);
            map.put("name",user.toString());
            String json=toJsonStr(map);
            try {
                s.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLogout() {
        for(WebSocketSession s:sessions){
            HashMap<String,Object> map=new HashMap<>();
            map.put("action","onLogout");
            map.put("success",true);
            String json=toJsonStr(map);
            try {
                s.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onContactLoaded(MemerList memers) {

    }

    @Override
    public void onMessage(Message msg) {
        for(WebSocketSession s:sessions){
            HashMap<String,Object> map=new HashMap<>();
            map.put("action","onMessage");
            map.put("msg",msg);
            msg.setAttachment(null);
            String json=toJsonStr(map);
            try {
                s.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String toJsonStr(HashMap<String,Object>map){
        ObjectMapper mapper=new ObjectMapper();
        String json= "";
        try {
            json = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}


