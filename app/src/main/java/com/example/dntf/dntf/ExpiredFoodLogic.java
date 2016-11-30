package com.example.dntf.dntf;

import android.graphics.Color;

import java.util.concurrent.TimeUnit;

/**
 * Created by franblas on 26/11/16.
 */
public class ExpiredFoodLogic {

    public static final int CRITICAL_STATUS = 0;
    public static final int SEVERE_STATUS = 1;
    public static final int INTERMEDIATE_STATUS = 2;
    public static final int GOOD_STATUS = 3;

    private static final int CRITICAL_THRESHOLD = 9; // in days
    private static final int SEVERE_THRESHOLD = 6; // in days
    private static final int INTERMEDIATE_THRESHOLD = 2; // in days

    static public int getColor(long now, long addedTime) {
        long diff = TimeUnit.MILLISECONDS.toDays(now - addedTime);
        if (diff >= CRITICAL_THRESHOLD) {
            return Color.rgb(231, 76, 60); // red
        } else if (diff < CRITICAL_THRESHOLD && diff >= SEVERE_THRESHOLD) {
            return Color.rgb(230, 126, 34); // orange
        } else if (diff < SEVERE_THRESHOLD && diff >= INTERMEDIATE_THRESHOLD) {
            return Color.rgb(241, 196, 15); // yellow
        } else {
            return Color.rgb(46, 204, 113); // green
        }
    }

    static public int getStatus(long now, long addedTime) {
        long diff = TimeUnit.MILLISECONDS.toDays(now - addedTime);
        if (diff >= CRITICAL_THRESHOLD) {
            return CRITICAL_STATUS;
        } else if (diff < CRITICAL_THRESHOLD && diff >= SEVERE_THRESHOLD) {
            return SEVERE_STATUS;
        } else if (diff < SEVERE_THRESHOLD && diff >= INTERMEDIATE_THRESHOLD) {
            return INTERMEDIATE_STATUS;
        } else {
            return GOOD_STATUS;
        }
    }
}
