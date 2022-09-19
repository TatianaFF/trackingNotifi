package com.example.trackingnotifi.screens.ListOfNotifi

import android.app.Application
import android.app.Notification
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.trackingnotifi.REPOSITORY
import com.example.trackingnotifi.db.ModeDatabase
import com.example.trackingnotifi.db.repository.RealizationRepDao
import com.example.trackingnotifi.models.NotifiModel
import com.example.trackingnotifi.models.NotifiModelList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotifiViewModel(application: Application) : AndroidViewModel(application) {
    val context = application

    fun getAllNotifi(): LiveData<List<NotifiModel>> {
        return REPOSITORY.allNotifi
    }

    fun deleteNotifi(notifi: NotifiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteNotifi(notifi)
        }
    }

    fun notifiDBListTONotifiRVList(notifiDBList: ArrayList<NotifiModel>): ArrayList<NotifiModelList> {
        val notifiRVList = ArrayList<NotifiModelList>()

        val pm: PackageManager = context.packageManager
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        notifiDBList.forEach { val applicationInfo = pm.getApplicationInfo(it.pack, 1)
            notifiRVList.add( NotifiModelList(
                name_app = pm.getApplicationLabel(applicationInfo) as String,
                icon = pm.getApplicationIcon(applicationInfo),
                from = it.from,
                message = it.message,
                date = formatter.format(date),
                pack = it.pack
        ) ) }

        return notifiRVList
    }
}