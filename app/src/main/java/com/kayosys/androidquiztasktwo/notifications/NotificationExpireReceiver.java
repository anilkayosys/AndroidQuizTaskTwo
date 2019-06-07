package com.kayosys.androidquiztasktwo.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kayosys.androidquiztasktwo.utils.SharePref;
import com.kayosys.androidquiztasktwo.utils.TimerState;


/**
 * Broadcast Receiver for expire notification
 */
public class NotificationExpireReceiver extends BroadcastReceiver {
    SharePref sharePref;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharePref = new SharePref(context);
        sharePref.setTimerState(TimerState.Stopped);
        sharePref.setAlarmSetTime(0L, context);
        NotificationHelper helper = new NotificationHelper(context);
        helper.hideTimerNotification(context);
    }
}
