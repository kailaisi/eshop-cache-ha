package com.kailaisi;

import com.kailaisi.http.HttpClientUtils;

public class StubbedFallbackTest {
    public static void main(String[] args) {
        String response = HttpClientUtils.sendGetRequest("http://192.168.11.129:8081/getProductInfo?id=-1");
        System.out.println(response);
    }
}
