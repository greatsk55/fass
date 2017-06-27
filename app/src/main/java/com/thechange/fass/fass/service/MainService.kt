package com.thechange.fass.fass.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import com.thechange.fass.fass.R
import com.thechange.fass.fass.activity.MainActivity
import com.thechange.fass.fass.activity.SaveActivity
import com.thechange.fass.fass.application.Fass
import java.util.regex.Pattern


class MainService : Service(){

    /**
     * Created by user on 2017. 6. 27..
     */

    private lateinit var notifi: Notification

    val clipboardListener = object : ClipboardManager.OnPrimaryClipChangedListener {

        override fun onPrimaryClipChanged() {

            if (Fass.isRun) {
                //Toast.makeText(applicationContext, "copied " + cm.getPrimaryClip().getItemAt(0).getText(), Toast.LENGTH_SHORT).show()
                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as (ClipboardManager)

                try {
                    val text = cm?.primaryClip?.getItemAt(0)?.text.toString()
                    val texts = text.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    for (temp in texts) {
                        if (isValidURL(temp)) {
                            val intent = Intent(this@MainService, SaveActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)


                            val startHttp = temp.indexOf("http")
                            val endHttp = temp.indexOf(" ", startHttp)
                            var link: String?
                            if (endHttp <= 0)
                                link = temp.substring(startHttp)
                            else {
                                link = temp.substring(startHttp, endHttp)
                            }

                            intent.putExtra("link", link)
                            startActivity(intent)
                        }
                    }
                } catch (e: Exception) {
                    //Log.d("AAA","none")
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intent = Intent( this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notifi = Notification.Builder(getApplicationContext())
                .setAutoCancel(false)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.serviceRun))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_circle_logo))
                .setSmallIcon(R.drawable.ic_noti)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build()


        startForeground(1, notifi)


        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as (ClipboardManager)
        cm.removePrimaryClipChangedListener(clipboardListener)
        cm.addPrimaryClipChangedListener(clipboardListener)

        return START_STICKY
    }


//서비스가 종료될 때 할 작업

    override fun onDestroy() {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as (ClipboardManager)
        cm.removePrimaryClipChangedListener(clipboardListener)
        stopForeground(true)
    }


    fun isValidURL(urlStr:String): Boolean  {
        val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        val p = Pattern.compile(URL_REGEX)
        val m = p.matcher(urlStr)
        return m.find()
    }
}