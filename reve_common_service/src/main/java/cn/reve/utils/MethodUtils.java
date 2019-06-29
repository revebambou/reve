package cn.reve.utils;

import javax.persistence.Table;
import java.util.Random;

public class MethodUtils {

    public static String generalRandomNum(int len, int bound){
        String[] nums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(nums[generalSingleNum(bound)]);
        }
        return sb.toString();
    }

    private static int generalSingleNum(int bound){
        Random random = new Random();
        return random.nextInt(bound);
    }
}
