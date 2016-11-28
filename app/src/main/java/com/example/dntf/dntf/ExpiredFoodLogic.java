package com.example.dntf.dntf;

import android.graphics.Color;

import java.util.concurrent.TimeUnit;

/**
 * Created by franblas on 26/11/16.
 */
public class ExpiredFoodLogic {

    private static final int CRITICAL_THRESHOLD = 10; // in days
    private static final int SEVERE_THRESHOLD = 7; // in days
    private static final int INTERMEDIATE_THRESHOLD = 3; // in days

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
}
