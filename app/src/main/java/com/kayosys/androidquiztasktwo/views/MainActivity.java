package com.kayosys.androidquiztasktwo.views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.kayosys.androidquiztasktwo.R;
import com.kayosys.androidquiztasktwo.notifications.NotificationExpireReceiver;
import com.kayosys.androidquiztasktwo.notifications.NotificationHelper;
import com.kayosys.androidquiztasktwo.utils.SharePref;
import com.kayosys.androidquiztasktwo.utils.TimerState;

import java.util.Calendar;


/**
 * Main View when view open notification trigger and shows until it's time finished
 */
public class MainActivity extends AppCompatActivity {
    private NotificationHelper notificationHelper;
    private TimerState timerState;
    private SharePref sharePref;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharePref = new SharePref(this);
        timerState = sharePref.getTimerState();
        notificationHelper = new NotificationHelper(getBaseContext());

        /*-- trigger notification if timer state is not running  --*/
        if (timerState != TimerState.Running) {
            timerState = TimerState.Running;
            sharePref.setTimerState(timerState);
            int minutesRemaining = sharePref.getTimerLength(this);
            Long secondsRemaining = minutesRemaining * 60L;
            Long wakeUpTime = setAlarm(MainActivity.this, nowSeconds(), secondsRemaining);
            notificationHelper.showTimerRunning(MainActivity.this, wakeUpTime);
        }
    }

    /*-- get current time mills --*/
    private Long nowSeconds() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    /*-- set time for timer notification --*/
    private Long setAlarm(Context context, Long nowSeconds, Long secondsRemaining) {
        long wakeUpTime = (nowSeconds + secondsRemaining) * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationExpireReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent);
        sharePref.setAlarmSetTime(nowSeconds, this);
        return wakeUpTime;
    }

}
