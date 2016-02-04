package com.baranau.sergey.scarlett_w.Entity;

import com.baranau.sergey.scarlett_w.R;

/**
 * Created by sergey on 2/3/16.
 */
public class Emotions {
    private static final int[] sIconIds = {
            R.drawable.em_1f600,
            R.drawable.em_1f605,
            R.drawable.em_1f606,
            R.drawable.em_1f607,
            R.drawable.em_1f608,
            R.drawable.em_1f610,
            R.drawable.em_1f611,
            R.drawable.em_1f615,
            R.drawable.em_1f617,
            R.drawable.em_1f619,
            R.drawable.em_1f626,
            R.drawable.em_1f627,
            R.drawable.em_1f629,
            R.drawable.em_1f634,
            R.drawable.em_1f635,
            R.drawable.em_1f636,
            R.drawable.em_1f638,
            R.drawable.em_1f639,
            R.drawable.em_1f640
    };

    public static int HAPPY = 0;
    public static int VERY_HAPPY = 1;
    public static int VERY_VERY_HAPPY = 2;
    public static int ANGEL = 3;
    public static int DEVIL = 4;
    public static int NORMAL = 5;
    public static int NORMAL_2 = 6;
    public static int SAD = 7;
    public static int NO_MATTER = 8;
    public static int NO_MATTER_2 = 9;
    public static int VERY_SAD = 10;
    public static int VERY_VERY_SAD = 11;
    public static int CRYING = 12;
    public static int SLEEPING = 13;
    public static int OGO = 14;
    public static int NO_EMOTIONS = 15;
    public static int CAT_LAUGHING = 16;
    public static int CAT_CRYING = 17;
    public static int CAT_CONFUSED = 18;

    public static int getSmileyResource(int which) {
        return sIconIds[which];
    }
}
