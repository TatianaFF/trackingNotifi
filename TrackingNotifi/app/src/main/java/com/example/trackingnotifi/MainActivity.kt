package com.example.trackingnotifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.trackingnotifi.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trackingnotifi.models.AppInstaledModel
import java.lang.Exception
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    val context = application
//    var listAppInstaled: ArrayList<AppInstaledModel> = arrayListOf()


    //Экран создается с помощью биндингов
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")

        if (!isNotificationServiceEnabled(this)) {
            startActivity(intent)
        }

        APP = this
        navController = Navigation.findNavController(this, R.id.nav_fragment)

//        val bundle = Bundle()
//        listAppInstaled = getInstaledApps() as ArrayList<AppInstaledModel>
//        bundle.putSerializable("apps", listAppInstaled)

        binding.navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_notifi -> {
                    navController.navigate(R.id.notifiFragment)
                }
                R.id.navigation_modes -> {
                    navController.navigate(R.id.modesFragment)
                }
                R.id.navigation_settings -> {
//                    navController.navigate(R.id.createChangeFragment, bundle)
                }
            }
            true
        }
    }

    private fun isNotificationServiceEnabled(c: Context): Boolean {
        val pkgName = c.packageName
        val flat = Settings.Secure.getString(
            c.contentResolver,
            "enabled_notification_listeners"
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

//    class modesFrReceiver : BroadcastReceiver(){
//        @Override
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val postIntent = intent?.getStringExtra("notification_event")
//            Log.e("PACKPOSTED_modesFr", postIntent.toString())\
//
//        }
//
//    }
}