package com.cvnavi.wechat;

import com.cvnavi.wechat.browser.BrowserEventCallback;
import com.cvnavi.wechat.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringEscapeUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Log4j2
public class WechatParser {

    private static WechatClient client=WechatClient.INSTANCE;

    /**
     * 登录成功后的信息
     * @param content
     */
    public static void parseLogin(String content){
        if(client.getSelf()==null){
            ObjectMapper mapper=new ObjectMapper();
            try {
                Map map= mapper.readValue(content,Map.class);
                Map m=(Map) map.get("User");
                Member self=new Member(m.get("UserName").toString(),m.get("NickName").toString(),"");
                client.setSelf(self);
                for(WechatListener listener :client.getListeners()){
                    listener.onLoginSuccess(self);
                }
            } catch (IOException e) {
                log.error("error parse message:"+content,e);
            }
        }
    }

    /**
     * 解析联系人
     * @param content
     */
    public static void parseMemberList(String content){
        ObjectMapper mapper=new ObjectMapper();
        try {
            Map map= mapper.readValue(content,Map.class);

            List mList= (List) map.get("MemberList");
            for(Object obj:mList){
                Map m= (Map) obj;
                client.getMemberList().add(new Member(m.get("UserName").toString(),m.get("NickName").toString(),m.get("RemarkName").toString()));
            }
            for(WechatListener listener :client.getListeners()){
                listener.onContactLoaded(client.getMemberList());
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 解析联系人
     * @param content
     */
    public static void parseGroupList(String content){
        ObjectMapper mapper=new ObjectMapper();
        try {
            Map map= mapper.readValue(content,Map.class);
            List mList= (List) map.get("ContactList");
            for(Object obj:mList){
                Map m= (Map) obj;
                if(((Integer)m.get("MemberCount"))>0){
                    Group gc=new Group(m.get("UserName").toString(),m.get("NickName").toString(),m.get("RemarkName").toString());
                    client.getGroupList().add(gc);
                    List ml= (List) m.get("MemberList");
                    for(Object obj2:ml){
                        m= (Map) obj2;
                        gc.getMemberList().add(new Member(m.get("UserName").toString(),m.get("NickName").toString(),""));
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
    /**
     * 解析消息
     * @param msgStr
     */
    public static void parseMessage(String msgStr){

        if(client.getMemberList()!=null){
            ObjectMapper mapper=new ObjectMapper();
            try {
                Map map= mapper.readValue(msgStr,Map.class);
                List list=(List)map.get("AddMsgList");
                for(Object obj:list){
                    Message msg=null;
                    Map m= (Map) obj;
                    String msgId=m.get("MsgId").toString();
                    MsgType msgType=MsgType.parse((Integer)m.get("MsgType"));
                    if(msgType==MsgType.STATUSNOTIFY){
                        continue;
                    }
                    Member to=client.getMemberList().findMemberByUserName(m.get("ToUserName").toString());
                    if(to==null && client.getSelf().getUserName().equals(m.get("ToUserName").toString())){
                        to=client.getSelf();
                    }
                    int time=(Integer)m.get("CreateTime");
                    Date createTime=new Date((long)time*1000);
                    String content=m.get("Content").toString();
                    if(msgType==MsgType.APP){
                        content= StringEscapeUtils.unescapeXml(content);
                    }
                    Member from=client.getMemberList().findMemberByUserName(m.get("FromUserName").toString());
                    if(from==null && client.getSelf().getUserName().equals(m.get("FromUserName").toString())){
                        from=client.getSelf();
                    }
                    if(from!=null){
                        msg=new Message(msgId,from,to,msgType,createTime,content);
                    } else {
                        Group from2=client.getGroupList().findGroupByUserName(m.get("FromUserName").toString());
                        if(from2!=null){
                            GroupMessage gm=new GroupMessage(msgId,from2,to,msgType,createTime,content);
                            if(gm.getContent().contains("<br/>")){
                                String gMember=gm.getContent().split(":<br/>")[0];
                                gm.setContent(gm.getContent().split(":<br/>")[1]);
                                Member member= from2.getMemberList().findMemberByUserName(gMember);
                                gm.setMsgSender(member);
                            }
                            msg=gm;
                        }
                    }
                    if(msgType==MsgType.IMAGE){
                        for(int i=0;i<100;i++){
                            BufferedImage image = BrowserEventCallback.imageMap.get(msgId);
                            if(image!=null){
                                msg.setAttachment(image);
                                break;
                            }
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    if(msg!=null){
                        for(WechatListener listener :client.getListeners()){
                            listener.onMessage(msg);
                        }
                    }
                }

            } catch (IOException e) {
                log.error("error parse message:"+msgStr,e);
            }
        }
    }

}
