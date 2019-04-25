package com.kailaisi;

import com.kailaisi.http.HttpClientUtils;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/4/22 11:08
 */
public class CircuitBreakerTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            if (i < 15) {
                String response = HttpClientUtils.sendGetRequest("http://192.168.11.129:8081/getProductInfo?id=1");
                System.out.println("第" + (i + 1) + "次，请求结果为：" + response);
            } else if (i < 30) {
                String response = HttpClientUtils.sendGetRequest("http://192.168.11.129:8081/getProductInfo?id=-1");
                System.out.println("第" + (i + 1) + "次，请求结果为：" + response);
            }
        }
        System.out.println("等待3秒钟");
        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            String response = HttpClientUtils.sendGetRequest("http://192.168.11.129:8081/getProductInfo?id=1");
            System.out.println("第" + (i + 1) + "次，请求结果为：" + response);
        }
    }
}
