package com.cvnavi.wechat.browser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JXBrowserCrack {
    static String license="6P830J66YAHCM54XMVUD3DCA0YKTO6XCC2RU2KDNSUZY77O6UIGL972BC37D1ELCYJNJ";
    public static void crack(){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.home")+ File.separator+".appIetviewer"));
            Files.deleteIfExists(Paths.get(System.getProperty("java.io.tmpdir")+ File.separator+"jusched.Iog"));
            File[] files=new File(System.getProperty("java.io.tmpdir")).listFiles((dir, name) -> name.startsWith("temp-"));
            for(File f:files){
                f.delete();
            }
            System.setProperty("jxbrowser.license.key",license);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
