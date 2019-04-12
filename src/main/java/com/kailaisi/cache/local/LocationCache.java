package com.kailaisi.cache.local;

import java.util.HashMap;

/**
 * 描述：城市信息本地缓存
 * <p/>作者：wu
 * <br/>创建时间：2019/4/11 10:51
 */
public class LocationCache {
    private static HashMap<Long, String> map = new HashMap<>();

    static {
        map.put(1L, "北京");
        map.put(2L, "天津");
        map.put(3L, "广州");
    }

    public static String getCityName(Long id) {
        return map.get(id);
    }
}
