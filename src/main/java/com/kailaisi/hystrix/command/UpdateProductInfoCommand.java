package com.kailaisi.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * 描述：商品信息使用的hystrix
 * <p/>作者：wu
 * <br/>创建时间：2019/4/10 15:50
 */
public class UpdateProductInfoCommand extends HystrixCommand<Boolean> {
    public static final HystrixCommandKey KEY = HystrixCommandKey.Factory.asKey("GetProductInfoCommand");
    private Long productId;

    public UpdateProductInfoCommand(Long productId) {
        super(HystrixCommandGroupKey.Factory.asKey("UpdateProductInfoCommand"));
        this.productId = productId;
    }

    @Override
    protected Boolean run() throws Exception {
        GetProductInfoCommand.flushCache(productId);
        return true;
    }

}
