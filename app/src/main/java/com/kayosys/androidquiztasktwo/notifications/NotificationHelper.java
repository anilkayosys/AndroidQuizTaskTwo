package com.kayosys.androidquiztasktwo.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.kayosys.androidquiztasktwo.R;
import com.kayosys.androidquiztasktwo.views.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Notification Helper class
 */
public class NotificationHelper extends ContextWrapper {
    public NotificationHelper(Context base) {
        super(base);
    }

    private NotificationManager notificationManager;
    private Notification.Builder nBuilder;
    public static final String CHANNEL_ID_TIMER = "menu_timer";
    public static final String CHANNEL_NAME_TIMER = "Timer App Timer";
    public static final int TIMER_ID = 0;

    /*-- function to show notification --*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showTimerRunning(Context context, Long wakeUpTime) {
        System.out.println("---long----" + wakeUpTime);
        DateFormat df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
        nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true);
        nBuilder.setContentTitle("Timer is Running.")
                .setContentText("Time Left: " + df.format(new Date(wakeUpTime)))
                .setContentIntent(getPendingIntentWithStack(context, MainActivity.class))
                .setOngoing(true);
        createNotificationChannel();
        getManager().notify(TIMER_ID, nBuilder.build());
    }

    /*-- creating NotificationManager instance --*/
    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /*-- creating notification channel--*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            getManager().createNotificationChannel(notificationChannel);
        }
    }

    /*-- notification builder --*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    Notification.Builder getBasicNotificationBuilder(Context context, String channelId, Boolean playSound) {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new Notification.Builder(context, channelId).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(false)
                .setDefaults(0)
                .setOnlyAlertOnce(true)
                .setSound(notificationSound);
    }

    /*-- getting pending intent with stack --*/
    private PendingIntent getPendingIntentWithStack(Context context, Class javaClass) {
        Intent resultIntent = new Intent(context, javaClass);
        resultIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(javaClass);
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /*-- hide timer notification --*/
    void hideTimerNotification(Context context) {
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(TIMER_ID);
    }

    public void update(Long wakeupTime) {
        DateFormat df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
        nBuilder.setContentText("Time Left: " + df.format(new Date(wakeupTime)));
        nBuilder.setUsesChronometer(true);
        notificationManager.notify(TIMER_ID, nBuilder.build());
    }
}
