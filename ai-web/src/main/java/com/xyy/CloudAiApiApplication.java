package com.xyy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

// 排除gson自动配置类，腾讯云中有gson，此自动配置类会报错
@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class CloudAiApiApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(CloudAiApiApplication.class);
    }
}
