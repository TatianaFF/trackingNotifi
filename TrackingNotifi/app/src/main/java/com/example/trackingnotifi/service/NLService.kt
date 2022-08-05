package com.example.trackingnotifi.service

import android.annotation.SuppressLint
import android.app.Notification
import android.content.*
import android.content.pm.PackageManager
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.trackingnotifi.models.NotifiModelList
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class NLService : NotificationListenerService() {

    private val TAG = this.javaClass.simpleName
    var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    val packApps = ArrayList<String>()
    val notifiList = ArrayList<NotifiModelList>()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.i(TAG, "onNotificationPosted")
        Log.i(TAG, "ID: " + sbn.key + "t" + sbn.notification.tickerText + "t" + sbn.packageName)

        Log.e("count_S", packApps.count().toString())
        if (packApps.contains(sbn.packageName)) {
            val pm: PackageManager = packageManager
            val applicationInfo = pm.getApplicationInfo(sbn.packageName, 1)
            val date = Date()
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            notifiList.add(NotifiModelList(
                name_app = pm.getApplicationLabel(applicationInfo) as String,
                icon = pm.getApplicationIcon(applicationInfo),
                from = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE) as String,
                message = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT) as String,
                date = formatter.format(date),
                pack = sbn.packageName
            ))
            Log.e("notifiList_cou", notifiList.count().toString())
            cancelNotification(sbn.key)
        }
    }

    val NLServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"

        override fun onReceive(context: Context, intent: Intent) {

            val stopIntent = intent.getStringExtra("stop_MF")
            if (stopIntent!=null) {
                Log.e("STOP_S", stopIntent.toString())
                stopService(Intent(context, NLService::class.java))
            }

            val getNotifi = intent.getStringExtra("getNotifi")
            if (getNotifi!=null) {
                Log.e("getNotifi", "")
                val i = Intent(BROADCAST_NAME_ACTION)
                Log.e("notifiList_count", notifiList.count().toString())
                i.putExtra("push_notifi", notifiList as Serializable)
                LocalBroadcastManager.getInstance(context).sendBroadcast(i)
            }
        }

    }


    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    @Override
    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter(BROADCAST_NAME_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(NLServiceReceiver, filter)

        val i = Intent(BROADCAST_NAME_ACTION)
        i.putExtra("onCr", "eeeOnCr")
        this.sendBroadcast(i)

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

    @Override
    override fun stopService(name: Intent?): Boolean {
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show()
        Log.e("stop", "service")
        //очистить packApps
        val i = Intent(BROADCAST_NAME_ACTION)
        i.putExtra("service", notifiList.count())
        sendBroadcast(i)
        return super.stopService(name)
    }


    @Override
    override fun onDestroy() {
        Toast.makeText(this, "disabled service", Toast.LENGTH_SHORT).show()
        Log.e("service ", "disabled")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NLServiceReceiver)
    }

}