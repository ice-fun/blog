package com.knowswift.myspringboot.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;


//Base64工具类
public class Base64Utils {


    public static String base64ToImage(String base64, String imagePath, String imageName) {
        if (StringUtils.isEmpty(base64)) {
            return null;
        }
        FileUtils.createDirectoryIfNotExist(imagePath);


        String type = base64.substring(base64.indexOf(":") + 1, base64.indexOf(";"));
        String data = base64.substring(base64.indexOf(",") + 1);
        String suffix = FileUtils.FILE_TYPE_MAP.get(type);
        FileOutputStream fileOutputStream = null;
        byte[] buffer = Base64.decodeBase64(data);
        String filePath = imagePath + imageName + suffix;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(buffer);
            fileOutputStream.close();
            return filePath;
        } catch (IOException e) {
            try {
                fileOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
    }

}
