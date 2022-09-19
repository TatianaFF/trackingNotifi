package com.example.trackingnotifi.screens.ListOfModes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R
import com.example.trackingnotifi.adapters.ModeAdapter
import com.example.trackingnotifi.databinding.ModesFragmentBinding
import com.example.trackingnotifi.models.AppInstaledModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.service.NLService
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ModesFragment() : Fragment() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ModesFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ModeAdapter
    private var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"
    private val packAppsOnBlock = ArrayList<String>()
    private var allModes = ArrayList<ModeModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModesFragmentBinding.inflate(layoutInflater, container, false)

        recyclerView = binding.rvModes
        floatingButton = binding.floatingActionButton
        progressBar = binding.progressBar


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

     private fun init(){
        val viewModel = ViewModelProvider(this)[ModesViewModel::class.java]
         viewModel.initDatabase()

         adapter = ModeAdapter(view)
         recyclerView.adapter = adapter

         //получение всех режимов и отправление его в адаптер
        viewModel.getAllModes().observe(viewLifecycleOwner) { listModes ->
            allModes = listModes as ArrayList<ModeModel>
            adapter.setList(listModes)
        }

         //обработка нажатия на switch
         //присваивание значения функциональному типу
         adapter.onItemClick = { mode ->
             //активация режима
            if (mode.status){
                //деактивировать статусы режимов
                allModes.forEach { if (it.status && it.id != mode.id){
                    it.status = false
                    viewModel.updateMode(it)
                }}

                stopServ()
                packAppsOnBlock.clear()

                viewModel.getAppsByTitleMode(mode.title).observe(viewLifecycleOwner) { listApps ->
                    //добавить имя пакета если в листе с пакетами приложений на блокировку нет пакета принадлежащий режиму
                    listApps.forEach { if (!packAppsOnBlock.contains(it.pack)){
                        packAppsOnBlock.add(it.pack)
                        Log.e(TAG, packAppsOnBlock[0])
                    } }
                    //запуск сервиса
                    startServ()
                }

            }//деактивация режима
            else if (!mode.status){
                //остановка сервиса
                stopServ()
            }
            viewModel.updateMode(mode)
        }

        floatingButton.setOnClickListener{
            //ObjectAnimation
            progressBar.visibility = ProgressBar.VISIBLE

            APP.navController.navigate(R.id.createChangeFragment)
        }
    }

    private fun startServ(){
        val intent = Intent(context, NLService::class.java)
        intent.putStringArrayListExtra("onStartService", packAppsOnBlock)
        context?.startForegroundService(intent)
    }

    private fun stopServ(){
        val intent1 = Intent(BROADCAST_NAME_ACTION)
        intent1.putExtra("onStopService", "onStopService")
        LocalBroadcastManager.getInstance(activity!!.applicationContext).sendBroadcast(intent1)
    }

    companion object{
        fun clickMode(modeModel: ModeModel, view: View?){
            //progress bar при нажатии на карточку режима
            val fragmModes = view?.let { FragmentManager.findFragment<ModesFragment>(it) }
            val progressBar = fragmModes?.view?.findViewById<ProgressBar>(R.id.progressBar)
            progressBar?.visibility = ProgressBar.VISIBLE

            //progress bar при нажатии на создание режима
            val bundle = Bundle()
            bundle.putSerializable("mode", modeModel)

            APP.navController.navigate(R.id.createChangeFragment, bundle)
        }
    }
}