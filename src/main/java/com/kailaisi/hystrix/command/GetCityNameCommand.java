package com.kailaisi.hystrix.command;

import com.kailaisi.cache.local.LocationCache;
import com.netflix.hystrix.*;

/**
 * 描述：通过信号量获取城市名称信息
 * <p/>作者：wu
 * <br/>创建时间：2019/4/11 10:54
 */
public class GetCityNameCommand extends HystrixCommand<String> {
    private Long cityId;

    public GetCityNameCommand(Long cityId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(3)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
        this.cityId = cityId;
    }

    @Override
    protected String run() throws Exception {
        return LocationCache.getCityName(cityId);
    }
}
