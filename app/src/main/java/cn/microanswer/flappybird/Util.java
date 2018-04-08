package cn.microanswer.flappybird;

import android.content.Context;
import android.content.Intent;

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

    /**
     * 跳转到成绩排行界面
     * @param context
     */
    public static void jump2ScoreActivity(MainActivity context) {
        context.startActivity(new Intent(context, ScoreActivity.class));
        context.overridePendingTransition(R.anim.score_activity_in, R.anim.score_activity_out);
    }
}
