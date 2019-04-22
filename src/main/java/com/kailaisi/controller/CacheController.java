package com.kailaisi.controller;

import com.kailaisi.http.HttpClientUtils;
import com.kailaisi.hystrix.command.GetBrandNameCommand;
import com.kailaisi.hystrix.command.GetCityNameCommand;
import com.kailaisi.hystrix.command.GetProductInfoCommand;
import com.kailaisi.hystrix.command.GetProductInfosCommand;
import com.kailaisi.model.ProductInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;
import rx.Observer;

@Controller
public class CacheController {
    @RequestMapping("/change/product")
    @ResponseBody
    public String changeProduct(Long id) {
        String url = "http://192.168.11.129:8083/getProductInfo?productId=" + id;
        String response = HttpClientUtils.sendGetRequest(url);
        System.out.println(response);
        return "success";
    }

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public String getProductInfo(Long id) {
        GetProductInfoCommand command = new GetProductInfoCommand(id);
        ProductInfo info = command.execute();
        GetCityNameCommand cityNameCommand = new GetCityNameCommand(info.getCityId());
        info.setCityName(cityNameCommand.execute());

        GetBrandNameCommand getBrandNameCommand = new GetBrandNameCommand(info.getBrandId());
        info.setBrandName(getBrandNameCommand.execute());
        System.out.println(info.toString());

        return "success";
    }

    @RequestMapping("/getProductInfos")
    @ResponseBody
    public String getProductInfos(String ids) {
        GetProductInfosCommand command = new GetProductInfosCommand(ids.split(","));
        Observable<ProductInfo> observe = command.observe();
        observe.subscribe(new Observer<ProductInfo>() {
            @Override
            public void onCompleted() {
                System.out.println("数据获取完成");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(ProductInfo productInfo) {
                System.out.println(productInfo.toString());
            }
        });
        return "success";
    }

    @RequestMapping("/getProductInfosForEvery")
    @ResponseBody
    public String getProductInfosForEvery(String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            GetProductInfoCommand command = new GetProductInfoCommand(Long.valueOf(id));
            ProductInfo info = command.execute();
            GetCityNameCommand cityNameCommand = new GetCityNameCommand(info.getCityId());
            info.setCityName(cityNameCommand.execute());
            System.out.println(info.toString());
            System.out.println("产品信息通过缓存获取到：" + command.isResponseFromCache());
        }
        return "success";
    }
}
