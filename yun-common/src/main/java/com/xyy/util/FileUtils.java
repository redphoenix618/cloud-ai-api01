package com.xyy.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Data
@Slf4j
public class FileUtils {

    private final static String FILE_PATH = "E:\\test_yun\\audio2file\\";

    public static void writeFile(String fileName,String content){
        FileOutputStream fos = null;
        try {
            File file = new File(FILE_PATH + fileName);
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            log.info("****文件写出成功*****");
        }catch (Exception e){
            e.printStackTrace();
            log.error("*****文件写出失败：{}*****",e.getMessage());
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("*****异常信息：{}*****",e.getMessage());
            }
        }


    }
}
