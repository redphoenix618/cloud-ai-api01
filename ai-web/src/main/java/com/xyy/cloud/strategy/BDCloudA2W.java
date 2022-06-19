package com.xyy.cloud.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyy.cloud.strategy.abst.CloudA2W;
import com.xyy.common.result.ResultCloud;
import com.xyy.data.AudioToWordOutData;
import com.xyy.util.DateUtils;
import com.xyy.util.FileUtils;
import com.xyy.utils.HttpClients;
import sun.net.www.http.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BDCloudA2W extends CloudA2W {

    @Override
    public ResultCloud audio2Word(String filePath){

        // 个人账号信息
        String APP_ID = "";
        String API_KEY = "";
        String SECRET_KEY = "";

        Long before = DateUtils.nowDate();
        // 1、获取token
        String tokenUrl = "https://aip.baidubce.com/oauth/2.0/token";
        Map<String,Object> urlParamMap = new HashMap<>();
        urlParamMap.put("grant_type","client_credentials");
        urlParamMap.put("client_id",API_KEY);
        urlParamMap.put("client_secret",SECRET_KEY);
        String tokenResult = HttpClients.sendGetRequestWithParam(tokenUrl, urlParamMap);
        String accessToken = JSON.parseObject(tokenResult).getString("access_token");
        //String accessToken = "24.c5c6aa76d25b1fedf5ec360984f15a71.2592000.1657585747.282335-26426306";

        // 2、创建音频转写任务
        String createTaskUrl = "https://aip.baidubce.com/rpc/2.0/aasr/v1/create";
        Map<String,Object> urlParamMap1 = new HashMap<>();
        urlParamMap1.put("access_token",accessToken);
        Map<String,Object> bodyParamMap = new HashMap<>();
        bodyParamMap.put("speech_url",filePath);
        bodyParamMap.put("format","mp3");
        bodyParamMap.put("pid",1537);
        bodyParamMap.put("rate",16000);
        String result = HttpClients.doPostBody(createTaskUrl, urlParamMap1, bodyParamMap, null);
        String taskId = JSON.parseObject(result).getString("task_id");
        System.out.println("taskId : " + taskId);
        // 3、查询音频转写任务结果
        String queryTaskUrl = "https://aip.baidubce.com/rpc/2.0/aasr/v1/query";
        Map<String,Object> urlParamMap2 = new HashMap<>();
        urlParamMap2.put("access_token",accessToken);
        Map<String,Object> bodyParamMap2 = new HashMap<>();
        bodyParamMap2.put("task_ids",new String[]{taskId});
        while (true){
            String taskResult = HttpClients.doPostBody(queryTaskUrl, urlParamMap2, bodyParamMap2, null);
            JSONObject tasksInfo = JSON.parseObject(taskResult).getJSONArray("tasks_info").getJSONObject(0);
            String taskStatus = tasksInfo.getString("task_status");
            Long costTime = DateUtils.costTime(before);
            if (taskStatus.equals("Success")){
                String content = tasksInfo.getJSONObject("task_result").getJSONArray("result").getString(0);
                AudioToWordOutData outData = handleResult(content, costTime);
                return ResultCloud.success(outData);
            }
            System.out.println("结果正在解析中，5秒后重试");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
