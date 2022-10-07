package com.example.trackingnotifi.screens.ListOfNotifi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.adapters.NotifiAdapter
import com.example.trackingnotifi.databinding.NotifiFragmentBinding
import com.example.trackingnotifi.models.NotifiModel
import com.example.trackingnotifi.models.NotifiModelList
import android.graphics.Color
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class NotifiFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: NotifiFragmentBinding
    private lateinit var btnClearNotifi: Button
    private lateinit var tvCountNotifi: TextView
    private lateinit var recyclerView: RecyclerView
    private var adapter: NotifiAdapter = NotifiAdapter()
    private var notifiListDB = ArrayList<NotifiModel>()
    private var notifiListRV = ArrayList<NotifiModelList>()
    private var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotifiFragmentBinding.inflate(layoutInflater, container, false)

        btnClearNotifi = binding.btnClearNotifi
        recyclerView = binding.rvNotifi
        tvCountNotifi = binding.tvCount
        recyclerView.adapter = adapter
        adapter.setContext(requireActivity())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init(){
        val viewModel = ViewModelProvider(this)[NotifiViewModel::class.java]

        viewModel.getAllNotifi().observe(viewLifecycleOwner){ listAllNotifi ->
            notifiListDB = listAllNotifi as ArrayList<NotifiModel>
            notifiListRV = viewModel.notifiDBListTONotifiRVList(notifiListDB)
            notifiListRV.reverse()
            adapter.setListNotifi(notifiListRV)
            tvCountNotifi.text = "${notifiListDB.size}/5"
            Log.e(TAG, listAllNotifi.size.toString())

            if (listAllNotifi.size >= 5){
                tvCountNotifi.setTextColor(Color.parseColor("#FF0000"))
                Toast.makeText(context, "Превышено максимальное количество уведомлений (5), уведомления не будут сохранятся", Toast.LENGTH_SHORT).show()
            }
        }

        btnClearNotifi.setOnClickListener{
            notifiListDB.forEach { viewModel.deleteNotifi(it) }
            notifiListRV.clear()
            adapter.setListNotifi(notifiListRV)
            tvCountNotifi.setTextColor(Color.parseColor("#FF000000"))

            //отправить инфу в сервис что уведомления очищены и можно сохранять новые
            val intent = Intent(BROADCAST_NAME_ACTION)
            intent.putExtra("onClearCountNotifi", "onClearCountNotifi")
            LocalBroadcastManager.getInstance(activity!!.applicationContext).sendBroadcast(intent)
        }
    }

}