package com.kailaisi.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.cache.local.BrandCache;
import com.kailaisi.cache.local.LocationCache;
import com.kailaisi.http.HttpClientUtils;
import com.kailaisi.model.ProductInfo;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

import java.time.LocalDateTime;

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
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(30)
                        .withCircuitBreakerErrorThresholdPercentage(40)
                        .withCircuitBreakerSleepWindowInMilliseconds(3000)
                        .withExecutionTimeoutInMilliseconds(5000))
                .andCommandKey(KEY)
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("Hystrix"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)
                        .withMaxQueueSize(12)
                        .withQueueSizeRejectionThreshold(10)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        System.out.println("调用接口，查询商品信息，productId=" + productId);
        if (productId.equals(-1L)) {
            throw new Exception();
        }
        if (productId.equals(-2L)) {
            Thread.sleep(1000);
        }
        String url = "http://192.168.11.129:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, ProductInfo.class);
    }

    @Override
    protected String getCacheKey() {
        return "product_info_" + productId;
    }

    @Override
    protected ProductInfo getFallback() {
        return new FirstLevelFallbackCommand(productId).execute();
    }

    /**
     * 清除缓存
     *
     * @param id
     */
    public static void flushCache(Long id) {
        HystrixRequestCache.getInstance(KEY,
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }

    private static class FirstLevelFallbackCommand extends HystrixCommand<ProductInfo> {
        private Long productId;

        public FirstLevelFallbackCommand(Long productId) {
            //降级command需要使用自己的线程池
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FirstLevelFallbackCommand"))
                    .andCommandKey(KEY)
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FirstLevelFallbackCommandPool")));
            this.productId = productId;
        }

        @Override
        protected ProductInfo run() throws Exception {
            //第一级降级处理，所以需要从其他地址（备用地址）来获取数据
            String url = "http://192.168.11.129:8083/getProductInfo?productId=" + productId;
            String response = HttpClientUtils.sendGetRequest(url);
            return JSONObject.parseObject(response, ProductInfo.class);
        }

        @Override
        protected ProductInfo getFallback() {
            ProductInfo info = new ProductInfo();
            info.setId(productId);
            info.setBrandId(BrandCache.getBrandId(productId));
            info.setBrandName(BrandCache.getBrandName(info.getBrandId()));
            info.setCityId(LocationCache.getCityId(productId));
            info.setCityName(LocationCache.getCityName(info.getCityId()));
            info.setColor("默认颜色");
            info.setModifiedTime(LocalDateTime.now().toString());
            info.setName("默认产品");
            info.setPictureList("a.png");
            info.setPrice(0d);
            info.setShopId(-1L);
            info.setSize("默认大小");
            info.setService("默认服务");
            return info;
        }
    }
}
