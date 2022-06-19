package com.xyy.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AudioToWordOutData {

    /**
     *  转译后的内容
     */
    private String content;

    private Long costTime;

    /**
     *  用于展示是哪种原文件，1、足球解说原内容 2、录制的原内容
     */
    private Integer fileType;
}
