package com.xyy.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class HttpClients {

    public static void main(String[] args) {
//        HttpClient client = new HttpClient();
////        client.sendGetRequestNoParam();
//        client.sendPostRequestWithParam();

        Date date = new Date();
//        date.setTime();
        System.out.println(date);
    }

    /**
     *  没有参数的get请求
     * @return
     */
    public static String sendGetRequestWithParam(String url,Map<String,Object> urlParamMap){
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            URIBuilder builder = new URIBuilder(url);
            // 在路径后拼接参数
            if (Objects.nonNull(urlParamMap) && !urlParamMap.isEmpty()){
                for (Map.Entry<String,Object> entry : urlParamMap.entrySet()){
                    builder.setParameter(entry.getKey(),entry.getValue().toString());
                }
            }
            HttpGet httpGet = new HttpGet(builder.build());

            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(false).build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse httpResponse = client.execute(httpGet);

//            // 获取协议的类型和版本，http,https或者其他
//            ProtocolVersion protocolVersion = httpResponse.getProtocolVersion();
//            String protocol = protocolVersion.getProtocol();
//
//            // 做国际化用的
//            Locale locale = httpResponse.getLocale();
//            String country = locale.getCountry();
//
//            // 获取返回结果的状态码 200表示正常
//            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            // 获取所有的头信息
//            Header[] allHeaders = httpResponse.getAllHeaders();
//            // 获取响应实体
//            HttpEntity entity = httpResponse.getEntity();
//            // 从响应实体拿到响应内容的流信息
//            InputStream content = entity.getContent();
//            // 获取响应实体的contentType
//            Header contentType = entity.getContentType();
//            String contentTypeValue = contentType.getValue();
//            // 获取响应实体的contentType的编码
//            Header contentEncoding = entity.getContentEncoding();
//            String contentEncodingValue = contentEncoding.getValue();
//            // 获取响应实体的大小
//            long contentLength = entity.getContentLength();
            // 响应结果如果是字符串，可直接将响应实体转为字符串
            String entityResult = EntityUtils.toString(httpResponse.getEntity());

            // 将响应实体转为字节数组，针对文件处理
            // byte[] bytes = EntityUtils.toByteArray(entity);
            // ByteArrayInputStream byteinput = new ByteArrayInputStream(bytes);
            return entityResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  有参数的get请求 todo
     */
    public void sendGetRequestWithParam(){

    }

    /**
     *  带参数的post请求
     */
    /*public void sendPostRequestWithParam(){
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("http://localhost:8088/test/user/query3");
            User user = new User();
            user.setAge(20);
            user.setUserName("海绵宝宝");
            String jsonString = JSON.toJSONString(user);
            StringEntity entity = new StringEntity(jsonString,"UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json;charset=utf8");
            CloseableHttpResponse httpResponse = client.execute(httpPost);

            HttpEntity responseEntity = httpResponse.getEntity();
            String s = EntityUtils.toString(responseEntity);
            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/



    /**
     *  https请求
     */
    public void sendHttpsRequest(){

    }

    /**
     *  详细的post请求，包括请求体传参，路径传参，请求头传参
     * @param url
     * @param urlParamMap
     * @param bodyParamMap
     * @param headerMap
     * @return
     */
    public static String doPostBody(String url, Map<String,Object> urlParamMap, Map<String,Object> bodyParamMap, Map<String,Object> headerMap){
        CloseableHttpClient httpClient = null;
        try {
             httpClient = HttpClientBuilder.create().build();
            URIBuilder builder = new URIBuilder(url);
            // 在路径后拼接参数
            if (Objects.nonNull(urlParamMap) && !urlParamMap.isEmpty()){
                for (Map.Entry<String,Object> entry : urlParamMap.entrySet()){
                    builder.setParameter(entry.getKey(),entry.getValue().toString());
                }
            }
            // 参数添加到请求体中
            HttpPost httpPost = new HttpPost(builder.build());
            String bodyParamJson = JSON.toJSONString(bodyParamMap);
            StringEntity paramEntity = new StringEntity(bodyParamJson,"UTF-8");
            httpPost.setEntity(paramEntity);
            httpPost.setHeader("Content-Type","application/json;charset=utf8");
            // 添加自定义的请求头
            if (Objects.nonNull(headerMap) && !headerMap.isEmpty()){
                for (Map.Entry<String,Object> entry: headerMap.entrySet()){
                    httpPost.addHeader(entry.getKey(),entry.getValue().toString());
                }
            }
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *  formdata传参
     */
    public void doPostFormData(){

    }

}
