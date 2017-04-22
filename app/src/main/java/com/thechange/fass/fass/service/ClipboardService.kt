package com.thechange.fass.fass.service

import android.annotation.TargetApi
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.thechange.fass.fass.activity.SaveActivity
import java.util.regex.Pattern

/**
 * Created by user on 2017. 4. 20..
 */


class ClipboardService : Service() {
    internal var mBinder: IBinder? = null
    internal var mStartMode: Int = 1


    override fun onCreate() {
        super.onCreate()


        //만약 항상 살아있게하려면
        //RestartService.startForeground(this)
        //val localIntent = Intent(this, RestartService::class.java)
        //startService(localIntent)
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onStartCommand(intent2: Intent?, flags: Int, startId: Int): Int {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as (ClipboardManager)

        cm.addPrimaryClipChangedListener(ClipboardManager.OnPrimaryClipChangedListener {
            Log.i("clipboard", "changed to:" + cm.getPrimaryClip())
            //Toast.makeText(applicationContext, "copied " + cm.getPrimaryClip().getItemAt(0).getText(), Toast.LENGTH_SHORT).show()

            val text = cm.primaryClip.getItemAt(0).getText().toString()
            val texts = text.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (temp in texts) {
                if (isValidURL(temp)) {
                    val intent = Intent(this, SaveActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)


                    val startHttp = temp.indexOf("http")
                    val endHttp = temp.indexOf(" ", startHttp)
                    var link : String?
                    if( endHttp <= 0)
                        link = temp.substring(startHttp)
                    else{
                        link = temp.substring(startHttp,endHttp)
                    }

                    intent.putExtra("link", link)
                    startActivity(intent)
                }
            }

        })

        return mStartMode
    }


    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    //Valid한 url인지 체크한다.
    public fun isValidURL(urlStr:String): Boolean  {
        val URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        val p = Pattern.compile(URL_REGEX)
        val m = p.matcher(urlStr)
        return m.find()
    }
}
