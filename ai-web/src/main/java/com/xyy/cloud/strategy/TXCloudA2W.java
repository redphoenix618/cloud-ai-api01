package com.xyy.cloud.strategy;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskResponse;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.xyy.cloud.strategy.abst.CloudA2W;
import com.xyy.common.result.ResultCloud;
import com.xyy.data.AudioToWordOutData;
import com.xyy.util.DateUtils;

import java.util.concurrent.TimeUnit;

public class TXCloudA2W extends CloudA2W {

    // 个人的账号信息
    private final static String SecretId  = "";
    private final static String SecretKey  = "";

    @Override
    public ResultCloud audio2Word(String filePath) {

        Long before = DateUtils.nowDate();
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(SecretId, SecretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            AsrClient client = new AsrClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CreateRecTaskRequest req = new CreateRecTaskRequest();
            req.setEngineModelType("16k_zh");
            req.setChannelNum(1L);
            req.setSpeakerDiarization(0L);
            req.setResTextFormat(2L);
            req.setSourceType(0L);
            req.setUrl(filePath);
            //返回的resp是一个CreateRecTaskResponse的实例，与请求对象对应
            CreateRecTaskResponse resp = client.CreateRecTask(req);
            // 打印任务id
            Long taskId = resp.getData().getTaskId();
            // Long taskId = 2055443601L;
            System.out.println("taskId: " + taskId);
            while (true){
                // 实例化一个请求对象,每个接口都会对应一个request对象
                DescribeTaskStatusRequest req2 = new DescribeTaskStatusRequest();
                req2.setTaskId(taskId);
                // 返回的resp是一个DescribeTaskStatusResponse的实例，与请求对象对应
                DescribeTaskStatusResponse resp2 = client.DescribeTaskStatus(req2);
                Long costTime = DateUtils.costTime(before);
                // 解析完毕
                if (resp2.getData().getStatusStr().equals("success")){
                    String content = resp2.getData().getResult();
                    AudioToWordOutData outData = handleResult(content, costTime);
                    return ResultCloud.success(outData);
                }
                System.out.println("结果未解析完成，5后重试");
                TimeUnit.SECONDS.sleep(5);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
