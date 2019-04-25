package com.kailaisi;

import com.kailaisi.http.HttpClientUtils;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/4/25 13:24
 */
public class RejectTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 25; i++) {
            new RejectThread(i).start();
        }
    }

    public static class RejectThread extends Thread{
        int index;

        public RejectThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            String response = HttpClientUtils.sendGetRequest("http://192.168.11.129:8081/getProductInfo?id=-2");
            System.out.println("第" + (index + 1) + "次，请求结果为：" + response);
        }
    }
}
