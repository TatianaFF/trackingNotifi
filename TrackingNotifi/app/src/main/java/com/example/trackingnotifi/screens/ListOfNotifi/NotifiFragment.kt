package com.example.trackingnotifi.screens.ListOfNotifi

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.adapters.NotifiAdapter
import com.example.trackingnotifi.databinding.NotifiFragmentBinding
import com.example.trackingnotifi.models.NotifiModel
import com.example.trackingnotifi.models.NotifiModelList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NotifiFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: NotifiFragmentBinding
    private lateinit var btnClearNotifi: Button
    private var allNotifi = ArrayList<NotifiModelList>()
    private lateinit var recyclerView: RecyclerView
    private var adapter: NotifiAdapter = NotifiAdapter()
    private var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    private var notifiListDB = ArrayList<NotifiModel>()
    private var notifiListRV = ArrayList<NotifiModelList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotifiFragmentBinding.inflate(layoutInflater, container, false)

//        val filter = IntentFilter(BROADCAST_NAME_ACTION)
//        LocalBroadcastManager.getInstance(activity!!.applicationContext).registerReceiver(valBroadcastReceiver, filter)

        btnClearNotifi = binding.btnClearNotifi
        recyclerView = binding.rvNotifi
        recyclerView.adapter = adapter
        adapter.setContext(requireActivity())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        val viewModel = ViewModelProvider(this)[NotifiViewModel::class.java]

        viewModel.getAllNotifi().observe(viewLifecycleOwner){ listAllNotifi ->
            notifiListDB = listAllNotifi as ArrayList<NotifiModel>
            notifiListRV = viewModel.notifiDBListTONotifiRVList(notifiListDB)
            adapter.setListNotifi(notifiListRV)
            Log.e(TAG, listAllNotifi.size.toString())
        }

        btnClearNotifi.setOnClickListener{
            notifiListDB.forEach { viewModel.deleteNotifi(it) }
            notifiListRV.clear()
            adapter.setListNotifi(notifiListRV)
        }
    }

//    private val valBroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(contxt: Context?, intent: Intent?) {
//            //получение заблокированных уведомлений из сервиса
////            val intentNotifiService = intent?.getStringArrayListExtra("pushNotifi") as? ArrayList<NotifiModelList>
////
////            intentNotifiService?.let {
////                allNotifi = intentNotifiService
////                adapter.setListNotifi(allNotifi)
////                adapter.setContext(requireActivity())
////            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()

//        LocalBroadcastManager.getInstance(activity!!.applicationContext).unregisterReceiver(valBroadcastReceiver)
    }

}