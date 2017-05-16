package com.thechange.fass.fass.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.Nullable
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.thechange.fass.fass.R

/**
 * Created by user on 2017. 4. 20..
 */

class RestartService : BroadcastReceiver() {

    companion object {
        val ACTION_RESTART_PERSISTENTSERVICE = "ACTION.Restart.PersistentService";
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        //Log.d("RestartService", "RestartService called!!!!!!!!!!!!!!!!!!!!!!!");


        /* 서비스 죽일때 알람으로 다시 서비스 등록 */
        if (intent?.getAction().equals(ACTION_RESTART_PERSISTENTSERVICE)) {

            //Log.d("RestartService", "Service dead, but resurrection");

            val i = Intent(context, PersistentService::class.java)
            context?.startService(i)
        }

        /* 폰 재부팅할때 서비스 등록 */
        if (intent?.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            //Log.d("RestartService", "ACTION_BOOT_COMPLETED");

            val i = Intent(context, PersistentService::class.java)
            context?.startService(i)
        }
    }
}