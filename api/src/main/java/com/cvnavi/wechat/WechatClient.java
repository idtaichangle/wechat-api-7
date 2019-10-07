package com.cvnavi.wechat;

import com.cvnavi.wechat.browser.BrowserEventCallback;
import com.cvnavi.wechat.browser.JxbrowserCracker;
import com.cvnavi.wechat.model.*;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.dom.Element;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.net.Network;
import com.teamdev.jxbrowser.net.callback.BeforeUrlRequestCallback;
import com.teamdev.jxbrowser.net.event.RequestCompleted;
import com.teamdev.jxbrowser.net.event.ResponseBytesReceived;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

/**
 * 微信客户端
 */
public class WechatClient implements AutoCloseable{

    public static WechatClient INSTANCE=new WechatClient();

    private Engine engine;
    @Getter
    private Browser browser;
    @Getter
    private BrowserView browserView;

    private BrowserEventCallback callback;

    @Getter @Setter
    private boolean isLogin=false;
    @Getter @Setter
    private BufferedImage qrImage;
    @Getter @Setter
    private Member self;
    @Getter
    private MemerList memberList=new MemerList();
    @Getter
    private GroupList groupList=new GroupList();

    @Getter
    private HashSet<WechatListener> listeners=new HashSet<>();

    private WechatClient(){
        JxbrowserCracker.crack();

        EngineOptions options=EngineOptions.newBuilder(HARDWARE_ACCELERATED)
                .addSwitch("--temporary").build();
        engine = Engine.newInstance(options);
        Network network=engine.network();
        callback=new BrowserEventCallback(this);
        network.set(BeforeUrlRequestCallback.class,params -> callback.on(params));
        network.on(ResponseBytesReceived.class, event->callback.on(event));
        network.on(RequestCompleted.class,event->callback.on(event));

        browser=engine.newBrowser();
        browserView=BrowserView.newInstance(browser);
    }



    /**
     * 发送消息
     * @param msg
     * @return
     */
    public boolean sendMessage(final Message msg){
        if(!isLogin){
            return  false;
        }
        SwingUtilities.invokeLater(()->{
            try{
                if(openChatWindow(msg.getTo().getUserName())){
                    Thread.sleep(10);
                    Optional<Element> document= browser.mainFrame().get().document().get().documentElement();
                    Optional<Element> de=document.get().findElementById("editArea");
                    de.get().textContent(msg.getContent());
                    browser.mainFrame().get().executeJavaScript("document.getElementById(\"editArea\").focus();");
                    fireTextChangeEvent();

                    Thread.sleep(5);
                    document.get().findElementByClassName("btn_send").get().click();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        return true;
    }
    /**
     * 发送消息
     * @param msg
     * @return
     */
    public boolean sendGroupMessage(final GroupMessage msg){
        if(!isLogin){
            return  false;
        }
        SwingUtilities.invokeLater(()->{
            try{
                Optional<Element> de=findDomByUserName(msg.getTo().getUserName());
                if(de.isPresent()){
                    de.get().click();
                    Thread.sleep(20);
                    Optional<Element> document= browser.mainFrame().get().document().get().documentElement();
                    de=document.get().findElementById("editArea");
                    de.get().textContent(msg.getContent());
                    browser.mainFrame().get().executeJavaScript("document.getElementById(\"editArea\").focus();");
                    fireTextChangeEvent();

                    Thread.sleep(10);

                    document.get().findElementByClassName("btn_send").get().click();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        return true;
    }

    /**
     * 退出登录
     */
    public void logout(){
        Optional<Element> document= browser.mainFrame().get().document().get().documentElement();

        Optional<Element> de= document.get().findElementByCssSelector("a.opt");
        de.ifPresent(element -> {
            element.click();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            document.get().findElementByCssSelector("i.menuicon_quit").ifPresent(element1 -> {
                element.click();
            });
        });
        setLogin(false);
    }

    /**
     * 获取登录页面的二维码图片
     * @return
     */
    public BufferedImage refreshLoginQrImage(){
        if(!isLogin){
            qrImage=null;
            browser.mainFrame().ifPresent(mainFrame->{
                mainFrame.loadUrl(Config.loginUrl);
            });
            try {
                for(int i=0;i<100;i++){
                    Thread.sleep(200);
                    if(qrImage!=null){
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return qrImage;
        }
        return null;
    }

    /**
     * 从最近聊天记录里点击某个人
     * @param userName
     */
    private Optional<Element> findDomByUserName(String userName) throws InterruptedException {
        Optional<Element> document= browser.mainFrame().get().document().get().documentElement();
        document.get().findElementByCssSelector(".web_wechat_tab_chat").
                ifPresent(element -> {
            element.click();
        });

        Thread.sleep(50);
        List<Element> list=
                document.get().findElementsByClassName("chat_item");
        for(Element de :list){
            String attr=de.attributeValue("data-username");
            if(userName.equals(attr)){
                return  Optional.of(de);
            }
        }
        return  null;
    }

    /**
     * 从联系人列表打开指定人的聊天窗口
     * @param userName
     */
    private boolean openChatWindow(String userName) throws InterruptedException {
        Optional<Element> document= browser.mainFrame().get().document().get().documentElement();
        Optional<Element> tab=document.get().findElementByCssSelector(".web_wechat_tab_friends");
        tab.ifPresent(element -> element.click());
        Thread.sleep(50);
        List<Element> list=document.get().findElementsByCssSelector(".contact_item img");
        for(Element de:list){
            String src=de.attributeValue("mm-src");
            if(src.contains("username="+userName)){
                de.click();
                Thread.sleep(50);
                document.get().findElementByCssSelector(".action_area a").
                        ifPresent(element -> element.click());
                return true;
            }
        }
        return  false;
    }

    /**
     * 触发文本框的值变化事件
     */
    private void fireTextChangeEvent() throws InterruptedException {
//        Thread.sleep(50);
//        forwardKeyEvent(VK_END);
//        Thread.sleep(50);
//        forwardKeyEvent(VK_SPACE);
//        Thread.sleep(50);
//        forwardKeyEvent(VK_BACK);
    }


//    private void forwardKeyEvent(BrowserKeyEvent.KeyCode code) {
//
//        browser.forwardKeyEvent(new BrowserKeyEvent(PRESSED, code));
//        browser.forwardKeyEvent(new BrowserKeyEvent(TYPED, code));
//        browser.forwardKeyEvent(new BrowserKeyEvent(RELEASED, code));
//    }

    @Override
    public void close() throws Exception {
        browser.close();
    }


    public void onLogout() {
        if(isLogin){
            setLogin(false);
            setSelf(null);
            memberList.clear();
            groupList.clear();
            for(WechatListener listener :listeners){
                listener.onLogout();
            }
        }
    }

    public void addListener(WechatListener wechatAdapter) {
        listeners.add(wechatAdapter);
    }
}
