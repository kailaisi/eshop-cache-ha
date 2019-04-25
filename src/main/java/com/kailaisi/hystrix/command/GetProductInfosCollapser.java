package com.kailaisi.hystrix.command;

import com.alibaba.fastjson.JSONArray;
import com.kailaisi.http.HttpClientUtils;
import com.kailaisi.model.ProductInfo;
import com.netflix.hystrix.*;

import java.util.Collection;
import java.util.List;

/**
 * 描述：将多个请求，放到一个command中进行请求。
 * <p/>作者：wu
 * <br/>创建时间：2019/4/25 15:23
 */
public class GetProductInfosCollapser extends HystrixCollapser<List<ProductInfo>, ProductInfo, Long> {
    private long productId;

    public GetProductInfosCollapser(long productId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("GetProductInfosCollapser"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter()
                        .withMaxRequestsInBatch(100)
                        .withTimerDelayInMilliseconds(20)));
        this.productId = productId;
    }

    @Override
    public Long getRequestArgument() {
        return productId;
    }

    @Override
    protected HystrixCommand<List<ProductInfo>> createCommand(Collection<CollapsedRequest<ProductInfo, Long>> collection) {
        return new BatchCommand(collection);
    }

    @Override
    protected String getCacheKey() {
        return "product_info_" + productId;
    }

    @Override
    protected void mapResponseToRequests(List<ProductInfo> batchResponse, Collection<CollapsedRequest<ProductInfo, Long>> requests) {
        int count = 0;
        for (CollapsedRequest<ProductInfo, Long> request : requests) {
            request.setResponse(batchResponse.get(count++));
        }
    }

    private static class BatchCommand extends HystrixCommand<List<ProductInfo>> {
        public final Collection<CollapsedRequest<ProductInfo, Long>> requests;

        public BatchCommand(Collection<CollapsedRequest<ProductInfo, Long>> requests) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfosCollapserBatchCommand")));
            this.requests = requests;
        }

        @Override
        protected List<ProductInfo> run() throws Exception {
            StringBuilder sb = new StringBuilder();
            for (CollapsedRequest<ProductInfo, Long> request : requests) {
                sb.append(request.getArgument()).append(",");
            }
            String params = sb.toString();
            params = params.substring(0, params.length() - 1);
            // 在这里，我们可以做到什么呢，将多个商品id合并在一个batch内，直接发送一次网络请求，获取到所有的结果
            String url = "http://192.168.11.129:8083/getProductInfos?productIds=" + params;
            String response = HttpClientUtils.sendGetRequest(url);
            List<ProductInfo> list = JSONArray.parseArray(response, ProductInfo.class);
            for (ProductInfo info : list) {
                System.out.println("BatchCommand内部，productInfo="+info.toString());
            }
            return list;
        }
    }
}
