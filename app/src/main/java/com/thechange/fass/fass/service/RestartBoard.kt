package com.thechange.fass.fass.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by user on 2017. 4. 20..
 */

class RestartBroad: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("000 RestartService" , "RestartService called : " + intent?.getAction());

        /**
         * 서비스 죽일때 알람으로 다시 서비스 등록
         */
        if(intent?.getAction().equals("ACTION.RESTART.PersistentService")){

            Log.i("000 RestartService" ,"ACTION.RESTART.PersistentService " );

            var i = Intent(context,ClipboardService::class.java)
            context?.startService(i);
        }

        /**
         * 폰 재시작 할때 서비스 등록
         */
        if(intent?.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            Log.i("RestartService" , "ACTION_BOOT_COMPLETED" );
            var i = Intent(context,ClipboardService::class.java)
            context?.startService(i);

        }

    }
}
