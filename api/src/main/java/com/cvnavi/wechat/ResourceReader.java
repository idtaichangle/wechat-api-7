package com.cvnavi.wechat;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ResourceReader {

    public static byte[] readFile(String file) {
        byte[] content = null;
        try {
            InputStream is = ResourceReader.class.getClassLoader().getResourceAsStream(file);
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            content = baos.toByteArray();
            baos.close();
            is.close();
        } catch (IOException e) {
        }
        return content;
    }

    public static List<String> readLines(String file) {
        List<String> list = new ArrayList<>();
        try {
            InputStream is = ResourceReader.class.getClassLoader().getResourceAsStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0 && !line.startsWith("#")) {
                    list.add(line);
                }
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
        }
        return list;
    }

    public static Properties readProperties(String file) {
        Properties p = new Properties();
        try {
            InputStream is = ResourceReader.class.getClassLoader().getResourceAsStream(file);
            p.load(is);
            is.close();
        } catch (IOException e) {
        }
        return p;
    }
}
