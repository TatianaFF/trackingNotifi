package com.example.trackingnotifi.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.trackingnotifi.MainActivity
import com.example.trackingnotifi.R
import com.example.trackingnotifi.models.NotifiModelList
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class NLService : NotificationListenerService() {

    private val TAG = this.javaClass.simpleName
    var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    val packApps = ArrayList<String>()
    val notifiList = ArrayList<NotifiModelList>()
    private val CHANNEL_ID = "ForegroundService"

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
//                stopService(Intent(context, NLService::class.java))
//                stopSelf()
                stopForeground(true)
                packApps.clear()
                notifiList.clear()
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
//        startForegroundService(intent)

        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Application blocking service")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        startForegroundService(notificationIntent)
        //stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        Log.e("start", "FORservice")


        return super.startForegroundService(service)
    }

    @Override
    override fun stopService(name: Intent?): Boolean {
        Log.e("stop", "service")

        return super.stopService(name)
    }

    @Override
    override fun onDestroy() {
        Log.e("service ", "disabled")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NLServiceReceiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

}