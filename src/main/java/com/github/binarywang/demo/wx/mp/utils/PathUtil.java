package com.github.binarywang.demo.wx.mp.utils;

import com.github.binarywang.demo.wx.mp.utils.message.WeixinCoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Component
public class PathUtil {

    public  String getFileUpLoadLocalStoragePath(String sysFilePath){
        String localStoragePath = null;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            localStoragePath = "d://uploadFiles";
        } else if (os.toLowerCase().startsWith("lin")) {
            localStoragePath = sysFilePath + File.separator + "";
        } else {
            localStoragePath = System.getProperty("user.home");
        }
        if (!localStoragePath.endsWith(File.separator)) {
            localStoragePath += File.separator;
        }
        //localStoragePath = sysFilePath+"/";
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String subDir = df.format(date);
        //String subDir = DateUtil.format(new Date(), "yyyyMMdd");
        localStoragePath += subDir;
        File file = new File(localStoragePath);
        if (!file.exists() || !file.isDirectory()) {
            if (!file.mkdir()) {
                log.error("创建文件夹失败");
            }
        }
        return localStoragePath;
    }

}
