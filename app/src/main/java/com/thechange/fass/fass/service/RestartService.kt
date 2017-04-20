package com.thechange.fass.fass.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.Nullable
import android.support.v7.app.NotificationCompat
import com.thechange.fass.fass.R

/**
 * Created by user on 2017. 4. 20..
 */


class RestartService : Service() {
    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(this)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    companion object {
        fun startForeground(service: Service?) {
            if (service != null) {
                try {
                    val notification = getNotification(service)
                    if (notification != null) {
                        service!!.startForeground(1208, notification)
                    }
                } catch (e: Exception) {

                }

            }
        }

        fun getNotification(paramContext: Context): Notification? {
            var smallIcon = R.mipmap.ic_launcher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                smallIcon = R.mipmap.ic_launcher
            }

            val nm = paramContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notification = NotificationCompat.Builder(paramContext)
                    .setSmallIcon(smallIcon)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setAutoCancel(true)
                    .setWhen(0)
                    .setTicker("").build()
            notification.flags = 1208

            nm.notify(1208, notification)
            nm.cancel(1208)


            return notification
        }
    }
}