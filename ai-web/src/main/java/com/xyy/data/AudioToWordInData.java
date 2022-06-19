package com.xyy.data;

import lombok.Data;

@Data
public class AudioToWordInData {

    /**
     *  云平台类型，1-阿里云 2-百度云 3-华为云 4-科大讯飞 5-腾讯云
     */
    private Integer cloudType;

    /**
     *  录音文件地址
     */
    private String filePath;


}
