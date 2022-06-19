package com.xyy.service;

import com.xyy.data.AudioToWordInData;
import com.xyy.common.result.ResultCloud;

public interface AudioToWordService {
    ResultCloud audioToWord(AudioToWordInData inData);
}
