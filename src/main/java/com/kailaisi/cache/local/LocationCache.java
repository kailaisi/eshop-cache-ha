package com.kailaisi.cache.local;

import java.util.HashMap;

/**
 * 描述：城市信息本地缓存
 * <p/>作者：wu
 * <br/>创建时间：2019/4/11 10:51
 */
public class LocationCache {
    private static HashMap<Long, String> map = new HashMap<>();
    private static HashMap<Long, Long> cityIdmap = new HashMap<>();

    static {
        map.put(1L, "北京");
        map.put(2L, "天津");
        map.put(3L, "广州");
        cityIdmap.put(1L, 1L);
        cityIdmap.put(2L, 2L);
        cityIdmap.put(3L, 3L);
    }

    public static String getCityName(Long id) {
        map.putIfAbsent(id,"地址："+id);
        return map.get(id);
    }

    public static Long getCityId(Long id) {
        cityIdmap.putIfAbsent(id, id);
        return cityIdmap.get(id);
    }
}
