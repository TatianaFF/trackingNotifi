package com.example.trackingnotifi.screens.ListOfModes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R
import com.example.trackingnotifi.adapters.ModeAdapter
import com.example.trackingnotifi.databinding.ModesFragmentBinding
import com.example.trackingnotifi.models.AppInstaledModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.screens.CreateChangeMode.CreateChangeFragment
import com.example.trackingnotifi.service.NLService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.io.Serializable


class ModesFragment() : Fragment() {

    lateinit var binding: ModesFragmentBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ModeAdapter
    var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    val packAppsOnBlock = ArrayList<String>()
    var allModesObs = ArrayList<ModeModel>()
    lateinit var floatingButton: FloatingActionButton
    lateinit var listAppInstaled: ArrayList<AppInstaledModel>
    lateinit var progress: ProgressBar
    val TAG_APPS = "apps"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        val viewModel = ViewModelProvider(this).get(ModesViewModel::class.java)
        viewModel.initDatabase()
        recyclerView = binding.rvModes
        adapter = ModeAdapter()
        recyclerView.adapter = adapter

        viewModel.getAllModes().observe(viewLifecycleOwner) { listModes ->
            allModesObs = listModes as ArrayList<ModeModel>
            adapter.setList(listModes)
        }

        progress = binding.progressBar

        adapter.onItemClick = { mode ->
            val intent = Intent(context, NLService::class.java)

            if (mode.status){
                //деактивировать статусы режимов
                for (itemMode in allModesObs){
                    if (itemMode.status && itemMode.id!=mode.id){
                        itemMode.status = false
                        viewModel.updateMode(itemMode)
                    }
                }

                // вкл сервис
                //переписать, заполнение листа на блокировку
                viewModel.getAppsByTitleMode(mode.title).observe(viewLifecycleOwner) { listApps ->
                    for (itemApp in listApps) if (!packAppsOnBlock.contains(itemApp.pack)) packAppsOnBlock.add(
                        itemApp.pack
                    )
                    intent.putStringArrayListExtra("onStartCommand", packAppsOnBlock)
                    Log.e("MFS_count_block", packAppsOnBlock.count().toString())
                    context?.startForegroundService(intent)
                }
            }else if (!mode.status){
                //отправить интент на блокировку в сервис
                val intent1 = Intent(BROADCAST_NAME_ACTION)
                intent1.putExtra("stop_MF", "STOP")
                //отсюда метод onStop не вызываестя
//                context?.sendBroadcast(intent1)
                LocalBroadcastManager.getInstance(activity!!.applicationContext).sendBroadcast(intent1)
            }
            viewModel.updateMode(mode)
//            for (itemMode in allModesObs) Log.e("MF_status", itemMode.status.toString())
        }


        floatingButton = binding.floatingActionButton
        floatingButton.setOnClickListener{
            progress.visibility = ProgressBar.VISIBLE
            APP.navController.navigate(R.id.createChangeFragment)
        }
    }

    companion object{
        fun clickMode(modeModel: ModeModel){
            val bundle = Bundle()
            bundle.putSerializable("mode", modeModel)
            APP.navController.navigate(R.id.detailFragment, bundle)
        }
    }
}