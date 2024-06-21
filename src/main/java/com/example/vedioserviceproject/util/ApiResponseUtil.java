package com.example.vedioserviceproject.util;

import com.alibaba.fastjson2.JSONObject;

public class ApiResponseUtil {

    public static String success(String message, Object data) {
        return createResponse(200, message, data);
    }

    public static String failure(String message) {
        return createResponse(444, message, null);
    }

    public static String error(String message) {
        return createResponse(555, message, null);
    }

    private static String createResponse(int code, String message, Object data) {
        JSONObject response = new JSONObject();
        response.put("code", code);
        response.put("message", message);
        response.put("data", data);
        return response.toJSONString();
    }
}
