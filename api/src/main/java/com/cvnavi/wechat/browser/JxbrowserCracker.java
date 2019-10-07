package com.cvnavi.wechat.browser;

import com.teamdev.jxbrowser.deps.com.google.protobuf.AbstractParser;
import com.teamdev.jxbrowser.deps.com.google.protobuf.CodedInputStream;
import com.teamdev.jxbrowser.deps.com.google.protobuf.ExtensionRegistryLite;
import com.teamdev.jxbrowser.deps.com.google.protobuf.InvalidProtocolBufferException;
import com.teamdev.jxbrowser.internal.rpc.ConnectionCreated;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

public class JxbrowserCracker{

    public static void crack() {
        try {
            System.setProperty("jxbrowser.license.key","6P830J66YAAWAPF6GXQB1UCRMUXXHY0LCAEKQ3J2IGCRTNKZGGIXJTU4Y57NK2MMVFQE");
//            System.setProperty("jxbrowser.license.key","96FF2FFA5C9173C76D47184B3E86D267B37781DE");


//            Field f3=ConnectionCreated.class.getDeclaredField("PARSER");
//            f3.setAccessible(true);
//
//            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
//            modifiersField.setAccessible( true );
//            modifiersField.setInt( f3, f3.getModifiers() & ~Modifier.FINAL );
//            f3.set(null,p);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    static AbstractParser p=new AbstractParser<ConnectionCreated>() {
        public ConnectionCreated parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws
        InvalidProtocolBufferException {
            ConnectionCreated cc=null;
            try {
                Constructor<ConnectionCreated> con=ConnectionCreated.class.
                        getDeclaredConstructor(CodedInputStream.class,ExtensionRegistryLite.class);
                con.setAccessible(true);
                cc=con.newInstance(input,extensionRegistry);

                Field f=ConnectionCreated.class.getDeclaredField("needsLicense_");
                f.setAccessible(true);
//                f.set(cc,false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return cc;
        }
    };
}