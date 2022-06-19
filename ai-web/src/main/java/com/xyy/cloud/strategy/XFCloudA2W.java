package com.xyy.cloud.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;
import com.xyy.cloud.strategy.abst.CloudA2W;
import com.xyy.common.result.ResultCloud;
import com.xyy.data.AudioToWordOutData;
import com.xyy.data.XFResultOutData;
import com.xyy.util.DateUtils;
import com.xyy.util.FileUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class XFCloudA2W extends CloudA2W {

    // 个人账号信息
    private final static String APP_ID = "";
    private final static String SECRET_KEY = "";
    // private final static String AUDIO_FILE_PATH = "E:\\test_yun\\audio2file\\audio.mp3";

    @Override
    public ResultCloud audio2Word(String filePath) {

        Long before = DateUtils.nowDate();

        //1、创建客户端实例
        LfasrClient lfasrClient = LfasrClient.getInstance(APP_ID, SECRET_KEY);

        //2、上传音频文件
        Message task = lfasrClient.upload(filePath);
        String taskId = task.getData();
        System.out.println("转写任务 taskId：" + taskId);

        //3、查看转写进度
        int status = 0;
        while (status != 9) {
            Message message = lfasrClient.getProgress(taskId);
            JSONObject object = JSON.parseObject(message.getData());
            status = object.getInteger("status");
            System.out.println(message.getData());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //4、获取结果
        Message result = lfasrClient.getResult(taskId);
        String str = result.getData();
        List<XFResultOutData> resultDTOS = JSONArray.parseArray(str, XFResultOutData.class);
        StringBuilder builder = new StringBuilder();
        for (XFResultOutData resultDTO : resultDTOS){
            builder.append(resultDTO.getOnebest());
        }
        Long costTime = DateUtils.costTime(before);
        AudioToWordOutData outData = handleResult(builder.toString(), costTime);
        return ResultCloud.success(outData);
    }
}
