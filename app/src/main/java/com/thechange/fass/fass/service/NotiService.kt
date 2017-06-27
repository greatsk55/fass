package com.thechange.fass.fass.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.widget.RemoteViews
import com.thechange.fass.fass.R
import com.thechange.fass.fass.activity.MainActivity
import com.thechange.fass.fass.activity.SaveActivity
import com.thechange.fass.fass.application.Fass.isRun
import java.util.regex.Pattern

/**
 * Created by user on 2017. 6. 23..
 */

class NotiService {

    val GOFASS = "GOFASS"
    val QUITFASS = "QUITFASS"
    val ONOFFFASS = "ONOFFFASS"

    private val mContext : Context
    public val mNotificationManager : NotificationManager
    private var notiBroadcastReceiver : BroadcastReceiver? = null
    private lateinit var componentName : ComponentName

    constructor( context : Context) {
        mContext = context;
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun setBroadcast() {
        if (notiBroadcastReceiver == null) {
            notiBroadcastReceiver = object : BroadcastReceiver() {

                 override fun onReceive( context: Context,  intent: Intent) { if(intent.action == GOFASS){
                        val intent = Intent(mContext, MainActivity::class.java)
                        mContext.startActivity(intent)
                    } else if (intent.action == QUITFASS) {
                        isRun = false
                        close()
                    }
                }
            };
            var intentFilter = IntentFilter()
            intentFilter.addAction(ONOFFFASS)
            intentFilter.addAction(GOFASS)
            intentFilter.addAction(QUITFASS)

            mContext.registerReceiver(notiBroadcastReceiver, intentFilter)
        }
    }

    fun show() : Notification {

        val remoteView = RemoteViews(
                mContext.getPackageName(), R.layout.notification_main)

        val intents = Intent(mContext, MainActivity::class.java)
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        intents.putExtra("noti",true);
        val pendingIntent = PendingIntent.getActivity(
                mContext, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT)

        remoteView.setOnClickPendingIntent(R.id.main1, pendingIntent)
        remoteView.setOnClickPendingIntent(R.id.main2, pendingIntent)
        remoteView.setOnClickPendingIntent(R.id.cancel, pendingIntent)

        remoteView.setTextViewText(R.id.service, mContext.getString(R.string.serviceRun))

        val intent1 = Intent(ONOFFFASS)
        val pendingIntent1 = PendingIntent.getBroadcast(mContext, 0, intent1, 0)
        val intent2 = Intent(GOFASS)
        val pendingIntent2 = PendingIntent.getBroadcast(mContext, 0, intent2, 0)
        val intent3 = Intent(QUITFASS)
        val pendingIntent3 = PendingIntent.getBroadcast(mContext, 0, intent3, 0)


        remoteView.setOnClickPendingIntent(R.id.main1, pendingIntent2)
        remoteView.setOnClickPendingIntent(R.id.main2, pendingIntent2)
        remoteView.setOnClickPendingIntent(R.id.on, pendingIntent1)
        remoteView.setOnClickPendingIntent(R.id.off, pendingIntent1)
        remoteView.setOnClickPendingIntent(R.id.cancel, pendingIntent3)

        val notification = Notification.Builder(mContext)
                .setAutoCancel(false)
                .setContent(remoteView)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_noti)
                .build()

        setBroadcast()

        isRun = true
        return notification
    }

    fun close() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(1)
            mContext.unregisterReceiver(notiBroadcastReceiver)
            notiBroadcastReceiver?.clearAbortBroadcast()
            notiBroadcastReceiver = null

            isRun = false
        }

    }




}