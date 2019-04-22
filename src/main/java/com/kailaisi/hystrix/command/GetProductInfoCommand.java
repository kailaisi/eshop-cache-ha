package com.kailaisi.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.http.HttpClientUtils;
import com.kailaisi.model.ProductInfo;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

/**
 * 描述：商品信息使用的hystrix
 * <p/>作者：wu
 * <br/>创建时间：2019/4/10 15:50
 */
public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {
    public static final HystrixCommandKey KEY = HystrixCommandKey.Factory.asKey("GetProductInfoCommand");
    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter())
                .andCommandKey(KEY)
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("Hystrix"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(20)
                        .withQueueSizeRejectionThreshold(10)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        String url = "http://192.168.11.129:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class);
        System.out.println("从服务器获取数据"+productInfo.toString());
        return productInfo;
    }

    @Override
    protected String getCacheKey() {
        return "product_info_" + productId;
    }

    @Override
    protected ProductInfo getFallback() {
        ProductInfo info = new ProductInfo();
        info.setName("降级产品");
        return info;
    }

    /**
     * 清除缓存
     * @param id
     */
    public static void flushCache(Long id) {
        HystrixRequestCache.getInstance(KEY,
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }
}
