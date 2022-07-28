package com.example.trackingnotifi.screens.ListOfModes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R
import com.example.trackingnotifi.adapters.ModeAdapter
import com.example.trackingnotifi.databinding.ModesFragmentBinding
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.service.NLService
import android.net.ConnectivityManager

import android.content.IntentFilter
import android.os.Build


class ModesFragment() : Fragment() {

    lateinit var binding: ModesFragmentBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ModeAdapter
    var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    val packAppsOnBlock = ArrayList<String>()


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

        viewModel.getAllModes().observe(viewLifecycleOwner,{listModes ->
            adapter.setList(listModes)
        })

        adapter.onItemClick = { mode ->
            val intent = Intent(context, NLService::class.java)
            viewModel.updateMode(mode)
            if (mode.status){
                // вкл сервис
                viewModel.getAppsByTitleMode(mode.title).observe(viewLifecycleOwner, {listApps->
                    for (itemApp in listApps) if (!packAppsOnBlock.contains(itemApp.pack)) packAppsOnBlock.add(itemApp.pack)
                    intent.putStringArrayListExtra("onStartCommand", packAppsOnBlock)
                    Log.e("MFS_count_block", packAppsOnBlock.count().toString())
                    //мб лучше отправить в сервис
                    context?.startService(intent)
                })

            }else if (!mode.status){
                //отправить интент на блокировку в сервис
                val intent1 = Intent(BROADCAST_NAME_ACTION)
                Log.e("MFD_count_block", packAppsOnBlock.count().toString())
                intent1.putExtra("stop_MF", "STOP")
                //отсюда метод onStop не вызываестя
                context?.sendBroadcast(intent1)
            }
        }
    }

//    private var myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            Log.d("MyBroadcastReceiver", " ПРИШЛО УВЕДОМЛЕНИЕ!!!")
//
//            Log.e("count packAppsOnBlock", packAppsOnBlock.toString())
//            val notifi = intent.getStringArrayListExtra("notification_event")
//            if (notifi!=null){
//                val notifiPack = notifi[0]
//                val notifiId = notifi[1]
//                Log.e("notification_event", notifiPack+notifiId)
//
////                if(packAppsOnBlock.contains(notifiPack)) {
////                    Log.e("contains_MF", "TRUE")
////                    val intent = Intent(BROADCAST_NAME_ACTION)
////                    intent.putStringArrayListExtra("block_pack", notifi)
////                    context.sendBroadcast(intent)
////                }
//            }
//        }
//    }

//    @Override
//    override fun onResume() {
//        super.onResume()
////        myBroadcastReceiver = MyBroadcastReceiver()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(BROADCAST_NAME_ACTION)
//        context?.registerReceiver(myBroadcastReceiver, intentFilter)
//    }

//    @Override
//    override fun onPause() {
//        super.onPause()
//        context?.unregisterReceiver(myBroadcastReceiver);
//    }

    companion object{
        fun clickMode(modeModel: ModeModel){
            val bundle = Bundle()
            bundle.putSerializable("mode", modeModel)
//            APP.navController.navigate(R.id.action_modesFragment_to_detailFragment, bundle)
            APP.navController.navigate(R.id.detailFragment, bundle)
        }
    }
}