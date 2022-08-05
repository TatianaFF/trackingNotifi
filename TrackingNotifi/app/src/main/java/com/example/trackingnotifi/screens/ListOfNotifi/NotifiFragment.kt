package com.example.trackingnotifi.screens.ListOfNotifi

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.adapters.NotifiAdapter
import com.example.trackingnotifi.databinding.NotifiFragmentBinding
import com.example.trackingnotifi.models.NotifiModelList
import com.google.android.material.internal.ContextUtils.getActivity


class NotifiFragment : Fragment() {

    lateinit var binding: NotifiFragmentBinding
    var allNotifi = ArrayList<NotifiModelList>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NotifiAdapter
    var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"


    override fun onResume() {
        super.onResume()

        val i = Intent(BROADCAST_NAME_ACTION)
        i.putExtra("getNotifi", "onResume")
        LocalBroadcastManager.getInstance(activity!!.applicationContext).sendBroadcast(i)
    }

    val valBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            Log.e("push_notifi", "")
            //************//
            val servIntent = intent?.getStringArrayListExtra("push_notifi") as? ArrayList<NotifiModelList>

            if (servIntent != null) {
                allNotifi = servIntent
                for (itemNotifi in allNotifi) Log.e("push_notifi", itemNotifi.message)
                recyclerView = binding.rvNotifi
                adapter = NotifiAdapter()
                recyclerView.adapter = adapter
                adapter.setList(allNotifi)
                adapter.setContext(requireActivity())
                Log.e("allNotifi.count", allNotifi.count().toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val filter = IntentFilter(BROADCAST_NAME_ACTION)
        LocalBroadcastManager.getInstance(activity!!.applicationContext).registerReceiver(valBroadcastReceiver, filter)

        binding = NotifiFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        val viewModel = ViewModelProvider(this).get(NotifiViewModel::class.java)

        viewModel.initDatabase()



    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(activity!!.applicationContext).unregisterReceiver(valBroadcastReceiver)
    }

    companion object{
        fun clickNotifi(notifi: NotifiModelList){
//            val packageManager: PackageManager =
//            val pm: PackageManager =  packageManager
//            val bundle = Bundle()
//            bundle.putSerializable("notifi", notifi)
////            APP.navController.navigate(R.id.detailFragment, bundle)
//            val launchIntent: Intent =
//                getPackageManager().getLaunchIntentForPackage(notifi.pack)
//            if (launchIntent != null) {
//                startActivity(launchIntent) //null pointer check in case package name was not found
//            }
        }
    }

}






















