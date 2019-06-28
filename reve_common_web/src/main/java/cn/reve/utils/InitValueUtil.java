package cn.reve.utils;

import java.util.Map;

public class InitValueUtil {

    /**
     * If the value of key is null, assign the value of ""
     * @param baseName
     * @param map
     * @return
     */
    public static Map<String, String> initKey(String baseName, Map<String, String> map){
        String value = "";
        String temp = map.get(baseName);
        if(temp!=null){
            return map;
        }
        map.put(baseName, value);
        return map;
    }

}
