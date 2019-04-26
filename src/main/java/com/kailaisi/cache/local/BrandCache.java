package com.kailaisi.cache.local;

import java.util.HashMap;
import java.util.Map;

/**
 * 品牌缓存
 *
 * @author Administrator
 */
public class BrandCache {

    private static Map<Long, String> brandMap = new HashMap<Long, String>();
    private static Map<Long, Long> brandIdMap = new HashMap<Long, Long>();

    static {
        brandMap.put(1L, "iphone");
        brandIdMap.put(1L, 1L);
    }

    public static String getBrandName(Long brandId) {
        brandMap.putIfAbsent(brandId, "品牌" + brandId);
        return brandMap.get(brandId);
    }

    public static Long getBrandId(Long brandId) {
        brandIdMap.putIfAbsent(brandId, brandId);
        return brandIdMap.get(brandId);
    }
}
