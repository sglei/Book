package com.sglei.basemodule.net.model;


import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.Map;

/**
 * @creator zane
 * @time 2018/12/19 15:14
 */
public class MapParam {
    private HashMap<String, Object> mapParam;

    private MapParam() {
        mapParam = new HashMap<>();
        mapParam.put("token", Hawk.get("key"));
    }

    public static MapParam createParams() {
        return new MapParam();
    }


    public MapParam putParam(String key, Object value) {
        mapParam.put(key, value);
        return this;
    }

    public MapParam putAll(Map<String, Object> stringStringMap) {
        mapParam.putAll(stringStringMap);
        return this;
    }

    public HashMap<String, Object> build() {
        return mapParam;
    }
}
