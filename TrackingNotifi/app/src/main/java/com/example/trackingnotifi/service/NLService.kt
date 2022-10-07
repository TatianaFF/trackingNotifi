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
import android.os.CountDownTimer
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.trackingnotifi.MainActivity
import com.example.trackingnotifi.R
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.NotifiModel
import com.example.trackingnotifi.models.NotifiModelList
import com.example.trackingnotifi.screens.ListOfNotifi.NotifiFragment
import com.example.trackingnotifi.screens.ListOfNotifi.NotifiViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import java.util.Observer
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext


class NLService : NotificationListenerService() {

    private val TAG = this.javaClass.simpleName
    private val BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    private val CHANNEL_ID = "ForegroundService"
    private val packAppsOnBlock = ArrayList<String>()
    private var countNotifi: Int = 0


    private fun insertNotifi(notifi: NotifiModel) {
        GlobalScope.launch(Dispatchers.IO) {
            REPOSITORY.insertNotifi(notifi)
        }
    }

    @Override
    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter(BROADCAST_NAME_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(NLServiceReceiver, filter)

    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        Log.i(TAG, "onNotificationPosted")
        Log.i(TAG, "ID: " + sbn.key + "t" + sbn.notification.tickerText + "t" + sbn.packageName)


        if (packAppsOnBlock.contains(sbn.packageName)) {

            cancelNotification(sbn.key)
            countNotifi++

            val date = Date()
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

            //проверка на кол во уведомлений
            if (countNotifi <= 5){
                insertNotifi(
                    NotifiModel(
                        pack = sbn.packageName,
                        from = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE) as String,
                        date = formatter.format(date),
                        message = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT) as String,
                    ))
            }
        }
    }

    private val NLServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val stopIntent = intent.getStringExtra("onStopService")

            if (stopIntent!=null) {
                //остановка сервиса
                Log.e(TAG, "stopserv")
                stopForeground(true)
                //очищение листа с именами пакетов на блокировку
                packAppsOnBlock.clear()
            }

            val clearIntent = intent.getStringExtra("onClearCountNotifi")
            if (clearIntent!=null) countNotifi = 0
        }
    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")

        //получение списка пакетов приложений на блокировку
        val intentStartListPackOnBlock = intent?.getStringArrayListExtra("onStartService")
        intentStartListPackOnBlock?.forEach { packAppsOnBlock.add(it) }
        Log.e(TAG, "packAppsOnBlock ${packAppsOnBlock.size.toString()}")

        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("The app blocks notifications")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        //без этого должно работать
        startForegroundService(notificationIntent)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        Log.e(TAG, "startForegroundService")

        return super.startForegroundService(service)
    }

    @Override
    override fun stopService(name: Intent?): Boolean {
        Log.e(TAG, "stopService")
        countNotifi = 0

        return super.stopService(name)
    }

    @Override
    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
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
