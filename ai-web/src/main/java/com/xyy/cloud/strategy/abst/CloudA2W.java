package com.xyy.cloud.strategy.abst;

import com.xyy.common.result.ResultCloud;
import com.xyy.data.AudioToWordOutData;

public abstract class CloudA2W {

    public abstract ResultCloud audio2Word(String filePath);

    public AudioToWordOutData handleResult(String content,Long costTime){
        AudioToWordOutData outData = AudioToWordOutData.builder()
                .content(content)
                .costTime(costTime)
                .build();
        return outData;
    }
}
