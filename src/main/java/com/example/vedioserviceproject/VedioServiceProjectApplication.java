package com.example.vedioserviceproject;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VedioServiceProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(VedioServiceProjectApplication.class, args);
    }
    static {
        // 配置 JSON 库以启用 LargeObject 功能
        JSON.config(JSONWriter.Feature.LargeObject, true);
    }
}
