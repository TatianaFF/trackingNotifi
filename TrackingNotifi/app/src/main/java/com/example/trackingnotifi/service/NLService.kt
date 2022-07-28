package com.example.trackingnotifi.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build

import android.annotation.TargetApi
import android.app.NotificationManager
import android.content.ComponentName
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.ModeModel
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R


class NLService : NotificationListenerService() {

    private val TAG = this.javaClass.simpleName
    private var nlservicereciver: NLServiceReceiver? = null
    var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    val packApps = ArrayList<String>()
    var pack: String = "null"


    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    @Override
    override fun onCreate() {
        super.onCreate()
        nlservicereciver = NLServiceReceiver()
        val filter = IntentFilter()
        filter.addAction(BROADCAST_NAME_ACTION)
        registerReceiver(nlservicereciver, filter)

        Toast.makeText(this, "enable service", Toast.LENGTH_SHORT).show()
        Log.e("service ", "enabled")
    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show()
        Log.e("start", "service")

        val intentStart = intent?.getStringArrayListExtra("onStartCommand")
        for (itemPack in intentStart!!) {
            packApps.add(itemPack)

            Log.e("pack", itemPack)
            Log.e("packApps", packApps.count().toString())
        }

        return super.onStartCommand(intent, flags, startId)
    }

//    @Override
//    override fun startForegroundService(service: Intent?): ComponentName? {
//        Toast.makeText(this, "ForegroundService service", Toast.LENGTH_SHORT).show()
//        Log.e("ForegroundService", "service")
//        return super.startForegroundService(service)
//    }


    class NLServiceReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onReceive(context: Context, intent: Intent) {
//            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
            val stopIntent = intent.getStringExtra("stop_MF")
            if (stopIntent!=null) {
                Log.e("STOP_S", stopIntent.toString())
                context.stopService(Intent(context, NLService::class.java))
            }
//
//            val blockIntent = intent.getStringArrayListExtra("block_pack")
//            if (blockIntent!=null) {
//                Log.e("blockPack_S", blockIntent.toString())
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    notificationManager.cancelAll()
//
//                }
//            }

        }

    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.i(TAG, "onNotificationPosted")
        Log.i(TAG, "ID: " + sbn.key + "t" + sbn.notification.tickerText + "t" + sbn.packageName)
//        val i = Intent(BROADCAST_NAME_ACTION)
//        i.putExtra("notification_event", sbn.packageName)
//        val listIntent = ArrayList<String>()
//        listIntent.add(sbn.packageName)
//        listIntent.add(sbn.key.toString())
//        i.putStringArrayListExtra("notification_event", listIntent)
//        sendBroadcast(i)

        Log.e("count_S", packApps.count().toString())
        if (packApps.contains(sbn.packageName)) cancelNotification(sbn.key)

    }

    @Override
    override fun stopService(name: Intent?): Boolean {
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show()
        Log.e("stop", "service")
        //очистить packApps
        return super.stopService(name)
    }


    @Override
    override fun onDestroy() {
        Toast.makeText(this, "disabled service", Toast.LENGTH_SHORT).show()
        Log.e("service ", "disabled")
        unregisterReceiver(nlservicereciver)
    }

}