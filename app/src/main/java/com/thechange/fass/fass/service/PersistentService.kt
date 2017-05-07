package com.thechange.fass.fass.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.thechange.fass.fass.activity.SaveActivity
import java.util.regex.Pattern

/**
 * Created by user on 2017. 5. 7..
 */




class PersistentService : Service(), Runnable {

    val TAG = "PersistentService";

    // 서비스 종료시 재부팅 딜레이 시간, activity의 활성 시간을 벌어야 한다.
    val REBOOT_DELAY_TIMER = 10 * 1000L;

    // GPS를 받는 주기 시간
    val LOCATION_UPDATE_DELAY = 20 * 1000L; // 5 * 60 * 1000

    var mHandler : Handler? = null
    var mIsRunning = false
    var mStartId = 0


    val clipboardListener = object : ClipboardManager.OnPrimaryClipChangedListener {

        override fun onPrimaryClipChanged() {
            //Toast.makeText(applicationContext, "copied " + cm.getPrimaryClip().getItemAt(0).getText(), Toast.LENGTH_SHORT).show()
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as (ClipboardManager)

            try {
                val text = cm?.primaryClip?.getItemAt(0)?.text.toString()
                val texts = text.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                for (temp in texts) {
                    if (isValidURL(temp)) {
                        val intent = Intent(this@PersistentService, SaveActivity::class.java)
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
                Log.d("AAA","none")
            }
        }


    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("PersistentService", "onBind()")
        return null
    }

    override fun onCreate() {

        // 등록된 알람은 제거
        Log.d("PersistentService", "onCreate()")
        unregisterRestartAlarm()
        super.onCreate()
        mIsRunning = false
    }

    override fun onDestroy() {
        // 서비스가 죽었을때 알람 등록
        Log.d("PersistentService", "onDestroy()")
        registerRestartAlarm()

        super.onDestroy()
        mIsRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PersistentService", "onStart()")
        super.onStartCommand(intent, flags, startId)

        mStartId = startId

        // 5분후에 시작
        mHandler = Handler()
        mHandler?.postDelayed(this, LOCATION_UPDATE_DELAY)
        mIsRunning = true

        return START_STICKY
    }


    override fun run() {
        Log.e(TAG, "run()");

        if(!mIsRunning)
        {
            Log.d("PersistentService", "run(), mIsRunning is false");
            Log.d("PersistentService", "run(), alarm service end");
            return;

        } else {

            Log.d("PersistentService", "run(), mIsRunning is true");
            Log.d("PersistentService", "run(), alarm repeat after five minutes");

            function()

            mHandler?.postDelayed(this, LOCATION_UPDATE_DELAY);
            mIsRunning = true;
        }
    }
    fun function() {
        Log.d(TAG, "========================");
        Log.d(TAG, "function()");
        Log.d(TAG, "========================");

        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as (ClipboardManager)

        cm.removePrimaryClipChangedListener(clipboardListener)
        cm.addPrimaryClipChangedListener(clipboardListener)

    }

    /**
     * 서비스가 시스템에 의해서 또는 강제적으로 종료되었을 때 호출되어
     * 알람을 등록해서 10초 후에 서비스가 실행되도록 한다.
     */
    fun registerRestartAlarm() {

        Log.d("PersistentService", "registerRestartAlarm()")

        val intent = Intent(this, RestartService::class.java)
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)

        var firstTime = SystemClock.elapsedRealtime()
        firstTime += REBOOT_DELAY_TIMER // 10초 후에 알람이벤트 발생

        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, REBOOT_DELAY_TIMER, sender)
    }


    /**
     * 기존 등록되어있는 알람을 해제한다.
     */
    fun unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()")
        val intent = Intent(this, RestartService::class.java)
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)

        val am =  getSystemService(ALARM_SERVICE) as (AlarmManager)
        am.cancel(sender)
    }

    //Valid한 url인지 체크한다.
    public fun isValidURL(urlStr:String): Boolean  {
        val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        val p = Pattern.compile(URL_REGEX)
        val m = p.matcher(urlStr)
        return m.find()
    }
}