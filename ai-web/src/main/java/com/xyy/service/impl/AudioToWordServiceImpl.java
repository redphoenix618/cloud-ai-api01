package com.xyy.service.impl;

import com.xyy.cloud.factory.CloudFactory;
import com.xyy.data.AudioToWordInData;
import com.xyy.common.result.ResultCloud;
import com.xyy.service.AudioToWordService;
import org.springframework.stereotype.Service;

@Service
public class AudioToWordServiceImpl implements AudioToWordService {

    @Override
    public ResultCloud audioToWord(AudioToWordInData inData) {
        ResultCloud resultCloud = CloudFactory.createCloudByType(inData).audio2Word(inData.getFilePath());
        return resultCloud;
    }
}
