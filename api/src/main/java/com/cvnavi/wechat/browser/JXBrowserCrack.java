package com.cvnavi.wechat.browser;

import com.cvnavi.wechat.ResourceReader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.prefs.Preferences;

public class JXBrowserCrack {
    static String license="1BNDHFSC1FWXTOLXDRSSEF7WDFRRAVEPW0O5GYJL6C8C3GDLHIES8Y7RV7GUXU2ETG4QYD";
    public static void crack(){
        try {
            Files.deleteIfExists(Paths.get(System.getProperty("user.home")+ File.separator+".appIetviewer"));
            Files.deleteIfExists(Paths.get(System.getProperty("java.io.tmpdir")+ File.separator+"jusched.Iog"));
            File[] files=new File(System.getProperty("java.io.tmpdir")).listFiles((dir, name) -> name.startsWith("temp-"));
            for(File f:files){
                f.delete();
            }

            Preferences.userRoot().node("/org/abobe").removeNode();
            Preferences.userRoot().node("/com/adept").removeNode();

            Properties p= ResourceReader.readProperties("jxbrowser.properties");
            System.setProperty("jxbrowser.license.key",p.getProperty("jxbrowser.license.key"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
