package com.kailaisi.hystrix.command;

import com.kailaisi.cache.local.BrandCache;
import com.netflix.hystrix.*;

/**
 * 描述：通过信号量获取品牌名称信息
 * <p/>作者：wu
 * <br/>创建时间：2019/4/11 10:54
 */
public class GetBrandNameCommand extends HystrixCommand<String> {
    private Long brandId;

    public GetBrandNameCommand(Long brandId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetBrandNameCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("Hystrix"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(20)
                        .withQueueSizeRejectionThreshold(10))
        );
        this.brandId = brandId;
    }

    @Override
    protected String run() throws Exception {
        throw new Exception("异常");
    }

    @Override
    protected String getFallback() {
        System.out.println("从本地获取缓存品牌数据：" + brandId);
        return BrandCache.getBrandName(brandId);
    }
}
