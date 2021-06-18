package com.anyuan.engineflow.other;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * @author liangjy on 2021/4/14.
 */
public class FileTest {

    public static void main(String[] args) throws FileNotFoundException {
        //String s=System.getProperty("user.dir");
        String s= ResourceUtils.getURL("classpath:").getPath();
        System.out.println(s);
    }

}
