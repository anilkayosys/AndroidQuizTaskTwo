package com.kayosys.androidquiztasktwo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences class to hold timer state of the timer and it's time
 */
public class SharePref {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String TIMER_LENGTH_ID = "com.kayosys.androidquiztasktwo.timer_length";
    private String PREFS_FILENAME = "com.kayosys.androidquiztasktwo.prefs";
    private String TIMER_STATE_ID = "com.kayosys.androidquiztasktwo.timer_state";
    private String ALARM_SET_TIME_ID = "com.kayosys.androidquiztasktwo.backgrounded_time";


    public SharePref(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILENAME, 0);
    }

    public int getTimerLength(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(TIMER_LENGTH_ID, 5);
    }


    public TimerState getTimerState() {
        int ordinal = prefs.getInt(TIMER_STATE_ID, 0);
        return TimerState.values()[ordinal];
    }

    public void setTimerState(TimerState state) {
        editor = prefs.edit();
        int ordinal = state.ordinal();
        System.out.println("--value" + ordinal);
        editor.putInt(TIMER_STATE_ID, ordinal);
        editor.apply();
    }


    public Long getAlarmSetTime() {
        return prefs.getLong(ALARM_SET_TIME_ID, 0);
    }

    public void setAlarmSetTime(Long time, Context context) {
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(ALARM_SET_TIME_ID, time);
        editor.apply();
    }
}
