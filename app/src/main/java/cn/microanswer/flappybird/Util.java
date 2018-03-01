package cn.microanswer.flappybird;

import java.util.Date;

/**
 * Created by Micro on 2018-2-10.
 */

public class Util {

    /**
     * 返回是白天还是夜晚
     *
     * @return
     */
    public static boolean isDay() {
        int hours = new Date().getHours();
        return 8 <= hours && hours <= 19;
    }

}
