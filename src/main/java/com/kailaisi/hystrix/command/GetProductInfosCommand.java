package com.kailaisi.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.kailaisi.http.HttpClientUtils;
import com.kailaisi.model.ProductInfo;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 描述：商品信息使用的hystrix
 * <p/>作者：wu
 * <br/>创建时间：2019/4/10 15:50
 */
public class GetProductInfosCommand extends HystrixObservableCommand<ProductInfo> {
    private String[] productIds;

    public GetProductInfosCommand(String[] productIds) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfosCommand"));
        this.productIds = productIds;
    }

    @Override
    protected Observable<ProductInfo> construct() {
        return Observable.from(productIds)
                .map(new Func1<String, ProductInfo>() {
                    @Override
                    public ProductInfo call(String productId) {
                        String url = "http://192.168.11.129:8083/getProductInfo?productId=" + productId;
                        String response = HttpClientUtils.sendGetRequest(url);
                        return JSONObject.parseObject(response, ProductInfo.class);
                    }
                }).subscribeOn(Schedulers.io());
    }
}
