package com.xyy.cloud.factory;

import com.xyy.cloud.strategy.*;
import com.xyy.cloud.strategy.abst.CloudA2W;
import com.xyy.data.AudioToWordInData;

public class CloudFactory {

    private CloudA2W cloudA2W;

    public static CloudA2W createCloudByType(AudioToWordInData inData){
        Integer cloudType = inData.getCloudType();
        switch (cloudType){
            case 1:
                return new ALiCloudA2W();
            case 2:
                return new BDCloudA2W();
            case 3:
                return new HWCloudA2W();
            case 4:
                return new XFCloudA2W();
            case 5:
                return new TXCloudA2W();
            default:
                throw new RuntimeException("查无此云厂商");
        }
    }

}
