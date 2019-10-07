package com.cvnavi.wechat.web;

import com.cvnavi.wechat.WechatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class IndexController {

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/qr",  produces = MediaType.IMAGE_JPEG_VALUE)
    public Object qr() throws IOException {
        if(WechatClient.INSTANCE.refreshLoginQrImage()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( WechatClient.INSTANCE.getQrImage(), "jpg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();

            return imageInByte;
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    @RequestMapping("/is_login")
    @ResponseBody
    public Object isLogin(){
        return WechatClient.INSTANCE.isLogin();
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Object logout(){
        WechatClient.INSTANCE.logout();
        return true;
    }
}
