package com.kailaisi.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.http.HttpClientUtils;
import com.kailaisi.model.ProductInfo;
import com.netflix.hystrix.*;

/**
 * 描述：商品信息使用的hystrix
 * <p/>作者：wu
 * <br/>创建时间：2019/4/10 15:50
 */
public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {
    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter())
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("Hystrix"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(20)
                        .withQueueSizeRejectionThreshold(10)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        String url = "http://192.168.11.129:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        System.out.println("从服务器获取数据");
        return JSONObject.parseObject(response, ProductInfo.class);
    }

    @Override
    protected String getCacheKey() {
        return "product_info_" + productId;
    }
}
