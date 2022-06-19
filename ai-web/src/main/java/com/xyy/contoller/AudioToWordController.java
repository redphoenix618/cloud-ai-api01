package com.xyy.contoller;


import com.xyy.common.exception.CloudA2WException;
import com.xyy.data.AudioToWordInData;
import com.xyy.common.result.ResultCloud;
import com.xyy.data.AudioToWordOutData;
import com.xyy.service.AudioToWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AudioToWordController {

    @Autowired
    private AudioToWordService audioToWordService;

    @PostMapping("/transfer")
    public ResultCloud audioToWord(AudioToWordInData inData){
         ResultCloud resultCloud = audioToWordService.audioToWord(inData);
        if (inData.getFilePath().contains("audio.mp3")){
            ((AudioToWordOutData)resultCloud.getData()).setFileType(1);
        }else {
            ((AudioToWordOutData)resultCloud.getData()).setFileType(2);
        }
        return resultCloud;


        // 调试
//        AudioToWordOutData outData = AudioToWordOutData.builder()
//                .content("display:none和visible:hidden都能把网页上某个元素隐藏起来，在视觉效果上没有区别，但是在一些DOM操作中两者有区别 display:none ---不为被隐藏的对象保留其物理空间，即该对象在页面上彻底消失，通俗来说就是看不见也摸不到")
//                .costTime(12234L)
//                .build();
//        if (inData.getFilePath().contains("audio.mp3")){
//            outData.setFileType(1);
//        }else {
//            outData.setFileType(2);
//        }
//        int i  = 1/0;
//        return ResultCloud.success(outData);
    }

    @GetMapping("/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index.html");
        return mv;
    }
}
