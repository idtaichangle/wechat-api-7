package com.cvnavi.wechat.browser;

import com.cvnavi.wechat.WechatClient;
import com.cvnavi.wechat.WechatParser;
import com.teamdev.jxbrowser.net.UrlRequest;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback;
import com.teamdev.jxbrowser.net.event.RequestCompleted;
import com.teamdev.jxbrowser.net.event.ResponseBytesReceived;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class BrowserEventCallback {

    private WechatClient client;

    public BrowserEventCallback(WechatClient client) {
        this.client = client;
    }

    /**
     * 某些请求返回的数据量比较大，一次请求可能要多次调用 onDataReceived。
     * 需要将多次请求返回的结果拼接起来。
     */
    ConcurrentHashMap<UrlRequest.Id,byte[]> respMap=new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,BufferedImage> imageMap=new ConcurrentHashMap<>();

    public BeforeUrlRequestCallback.Response on(BeforeUrlRequestCallback.Params  params) {
        log.debug("BeforeUrlRequestCallback:"+params.urlRequest().url());
        if(params.urlRequest().url().contains("webwxlogout")){
            client.onLogout();
        }
        return BeforeUrlRequestCallback.Response.proceed();
    }

    public void on(RequestCompleted requestCompleted){
        String url=requestCompleted.urlRequest().url();
        UrlRequest.Id id=requestCompleted.urlRequest().id();
        log.debug("RequestCompleted:"+url);
        if(url.contains("webwxgetcontact")){
            new Thread(()->{
                byte b[]=respMap.get(id);
                respMap.remove(id);
                if(b!=null){
                    WechatParser.parseMemberList(new String(b));
                }
            }).start();
        }else if(url.contains("webwxbatchgetcontact")){
            new Thread(()->{
                byte b[]=respMap.get(id);
                respMap.remove(id);
                if(b!=null){
                    WechatParser.parseGroupList(new String(b));
                }
            }).start();
        }else if(url.contains("webwxsync")){
            new Thread(()->{
                byte b[]=respMap.get(id);
                respMap.remove(id);
                if(b!=null){
                    WechatParser.parseMessage(new String(b));
                }

            }).start();
        }else if(url.contains("webwxinit")){
            if(!client.isLogin()){
                client.setLogin(true);
                byte b[]=respMap.get(id);
                respMap.remove(id);
                if(b!=null){
                    WechatParser.parseLogin(new String(b));
                }
            }
        }else if(url.contains("/qrcode/")){
            new Thread(()->{
                byte b[]=respMap.get(id);
                respMap.remove(id);
                if(b!=null){
                    try {
                        BufferedImage image=ImageIO.read(new ByteArrayInputStream(b));
                        client.setQrImage(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else if(url.contains("/webwxgetmsgimg")){
            new Thread(()->{
                byte b[]=respMap.get(id);
                respMap.remove(id);
                if(b!=null){
                    Matcher m=Pattern.compile("(?<=MsgID=)[0-9]+").matcher(url);
                    if(m.find()){
                        String msgID=m.group(0);
                        try {
                            BufferedImage image=ImageIO.read(new ByteArrayInputStream(b));
                            imageMap.put(msgID,image);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    imageMap.remove(msgID);
                                }
                            },3000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public void on(ResponseBytesReceived responseBytesReceived) {
        String url=responseBytesReceived.urlRequest().url();
        UrlRequest.Id id=responseBytesReceived.urlRequest().id();
        byte[] data=responseBytesReceived.data();

        if(url.contains("webwxgetcontact") ||
                url.contains("webwxbatchgetcontact") ||
                url.contains("webwxsync") ||
                url.contains("webwxinit") ||
                (url.contains("/qrcode/") && responseBytesReceived.mimeType().get().value().contains("image")) ||
                (url.contains("/webwxgetmsgimg") && responseBytesReceived.mimeType().get().value().contains("image"))){
            synchronized (respMap){
                log.debug("ResponseBytesReceived:"+url);
                byte[] b=respMap.get(id);
                if(b==null){
                    b=data;
                }else{
                    byte[] temp=new byte[b.length+data.length];
                    System.arraycopy(b,0,temp,0,b.length);
                    System.arraycopy(data,0,temp,b.length,data.length);
                    b=temp;
                }
                respMap.put(id,b);
            }
        }
    }

}
