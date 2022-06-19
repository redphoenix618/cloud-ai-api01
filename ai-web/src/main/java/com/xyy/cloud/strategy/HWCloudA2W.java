package com.xyy.cloud.strategy;

import com.huawei.sis.bean.AuthInfo;
import com.huawei.sis.bean.SisConfig;
import com.huawei.sis.bean.SisConstant;
import com.huawei.sis.bean.base.AsrcLongSentence;
import com.huawei.sis.bean.request.AsrCustomLongRequest;
import com.huawei.sis.bean.response.AsrCustomLongResponse;
import com.huawei.sis.client.AsrCustomizationClient;
import com.huawei.sis.exception.SisException;
import com.xyy.cloud.strategy.abst.CloudA2W;
import com.xyy.common.result.ResultCloud;
import com.xyy.data.AudioToWordOutData;
import com.xyy.util.DateUtils;

public class HWCloudA2W extends CloudA2W {

    @Override
    public ResultCloud audio2Word(String filePath) {
        Long before = DateUtils.nowDate();
        HWCloudA2W demo = new HWCloudA2W();
        // 录音文件识别
        String content = demo.longDemo(filePath);
        Long costTime = DateUtils.costTime(before);
        AudioToWordOutData outData = handleResult(content, costTime);
        return ResultCloud.success(outData);
    }

    private static final int SLEEP_TIME = 500;
    private static final int MAX_POLLING_NUMS = 1000;

    // 个人账号信息
    private String ak = "";
    private String sk = "";
    private String projectId = ""; // 项目id。登录管理控制台，鼠标移动到右上角的用户名上，在下拉列表中选择我的凭证，在项目列表中查看项目id。多项目时，展开“所属区域”，从“项目ID”列获取子项目ID。

    private String region = "cn-east-3";    // 区域，如cn-north-1、cn-north-4


    /**
     *   todo 请正确填写音频格式和模型属性字符串
     *   1. 音频格式一定要相匹配.
     *      例如obs url是xx.wav, 则在录音文件识别格式是auto。
     *      例如音频是pcm格式，并且采样率为8k，则格式填写pcm8k16bit。
     *      如果返回audio_format is invalid 说明该文件格式不支持。
     *
     *   2. 音频采样率要与属性字符串的采样率要匹配。
     *      例如格式选择pcm16k16bit，属性字符串却选择chinese_8k_common, 则属于采样率不匹配
     *      例如wav本身是16k采样率，属性选择chinese_8k_common, 同样属于采样率不匹配
     *
     *   3. 用户可以通过使用热词，识别专业术语，增加语句识别准确率。
     */

    // 录音文件识别参数
    // 音频文件OBS链接，录音文件识别目前仅支持传入OBS音频连接，或公网可访问url
    // private String obsUrl = "https://footbaln.oss-cn-hangzhou.aliyuncs.com/yun-test.mp3?Expires=1655062013&OSSAccessKeyId=TMP.3KeQ3eFWKyAhsJQhZx9k1BTH7C9FnkCVwnWfPoUrRF4NF82FxAYxqFto1XrA2ryxX461v5U998hbfCRcesVre8ELWK2bBy&Signature=H5T7qoC3QpzXL7crYSp2QDLEj84%3D";
    // private String obsUrl = "https://footbaln.oss-cn-hangzhou.aliyuncs.com/audio.mp3?Expires=1655073520&OSSAccessKeyId=TMP.3KeQ3eFWKyAhsJQhZx9k1BTH7C9FnkCVwnWfPoUrRF4NF82FxAYxqFto1XrA2ryxX461v5U998hbfCRcesVre8ELWK2bBy&Signature=tMcypql%2BNgwEGJUXUfa1u5QsFME%3D";
    private String obsAudioFormat = "auto";   // 文件格式，如auto等
    private String obsProperty = "chinese_16k_conversation";      // 属性字符串，如chinese_8k_common等
    /**
     * 设置录音文件识别参数，所有参数均有默认值，不配置也可使用
     *
     * @param request 录音文件识别请求
     */
    private void setLongParameter(AsrCustomLongRequest request) {
        // 设置否是添加标点，yes 或no， 默认是no
        request.setAddPunc("yes");
        // 设置是否将语音中的数字转写为阿拉伯数字，yes或no，默认yes
        request.setDigitNorm("no");
        // 设置声道，MONO/LEFT_AGENT/RIGHT_AGENT, 默认是单声道MONO
        request.setChannel("MONO");
        // 设置是否需要分析，默认为false。当前仅支持8k采样率音频。当其设置为true时，话者分离、情绪检测，速度、声道才生效。
        request.setNeedAnalysis(true);
        // 设置是否需要话者分离，若是，则识别结果包含role，默认true
        request.setDirization(true);
        // 设置是否需要情绪检测，默认true。
        request.setEmotion(true);
        // 设置是否需要速度。默认true。
        request.setSpeed(true);
        // 设置回调地址，设置后音频转写结果将直接发送至回调地址。请务必保证地址可联通,不支持ip地址。
        // request.setCallbackUrl("");
        // 设置热词id，不使用则不用填写
        // request.setVocabularyId("");
    }

    /**
     * 定义config，所有参数可选，设置超时时间等。
     *
     * @return SisConfig
     */
    private SisConfig getConfig() {
        SisConfig config = new SisConfig();
        // 设置连接超时，默认10000ms
        config.setConnectionTimeout(SisConstant.DEFAULT_CONNECTION_TIMEOUT);
        // 设置读取超时，默认10000ms
        config.setReadTimeout(SisConstant.DEFAULT_READ_TIMEOUT);
        // 设置代理, 一定要确保代理可用才启动此设置。 代理初始化也可用不加密的代理，new ProxyHostInfo(host, port);
        // ProxyHostInfo proxy = new ProxyHostInfo(host, port, username, password);
        // config.setProxy(proxy);
        return config;
    }


    /**
     * 录音文件识别demo
     */
    private String longDemo(String filePath) {
        try {
            // 1. 初始化AsrCustomizationClient
            // 定义authInfo，根据ak，sk，region,projectId.
            AuthInfo authInfo = new AuthInfo(ak, sk, region, projectId);
            // 设置config，主要与超时有关
            SisConfig config = getConfig();
            // 根据authInfo和config，构造AsrCustomizationClient
            AsrCustomizationClient asr = new AsrCustomizationClient(authInfo, config);

            // 2. 生成请求
            AsrCustomLongRequest request = new AsrCustomLongRequest(filePath, obsAudioFormat, obsProperty);
            // 设置请求参数，所有参数均为可选
            setLongParameter(request);

            // 3. 提交任务，获取jobId
            String jobId = asr.submitJob(request);

            // 4 轮询jobId，获取最终结果。
            int count = 0;
            int successFlag = 0;
            AsrCustomLongResponse response = null;
            while (count < MAX_POLLING_NUMS) {
                System.out.println("正在进行第" + count + "次尝试");
                response = asr.getAsrLongResponse(jobId);
                String status = response.getStatus();
                if (status.equals("FINISHED")) {
                    successFlag = 1;
                    break;
                } else if (status.equals("ERROR")) {
                    System.out.println("执行失败, 无法根据jobId获取结果");
                    return "error";
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }
            // 打印结果
            if (successFlag == 0) {
                System.out.println("已进行" + count + "次尝试，无法获取识别结果。 jobId为 " + jobId);
                return "已进行" + count + "次尝试，无法获取识别结果。 jobId为 " + jobId;
            }
            //System.out.println(response.getSentenceList().get(0).getResult());
            StringBuilder builder = new StringBuilder();
            for (AsrcLongSentence sentence: response.getSentenceList()){
                builder.append(sentence.getResult().getText());
            }
            return builder.toString();
        } catch (SisException e) {
            e.printStackTrace();
            System.out.println("error_code:" + e.getErrorCode() + "\nerror_msg:" + e.getErrorMsg());
        }
        return null;
    }
}
