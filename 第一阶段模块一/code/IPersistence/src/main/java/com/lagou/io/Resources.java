package com.lagou.io;

import java.io.InputStream;

/**
 * @author hanbing
 * @date 2020-04-24 14:09
 */
public class Resources {

    //根据配置文件的路径将配置文件加载成输入流，存储在内存中
    public static InputStream getResourceAsStream(String path){
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
