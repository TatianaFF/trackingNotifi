package com.example.trackingnotifi.screens.CreateChangeMode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R
import com.example.trackingnotifi.adapters.AppAdapter
import com.example.trackingnotifi.databinding.CreateChangeFragmentBinding
import com.example.trackingnotifi.models.AppInstaledModel
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel

class CreateChangeFragment : Fragment() {

    lateinit var binding: CreateChangeFragmentBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AppAdapter
//    var allModesObs = ArrayList<ModeModel>()
    lateinit var listAppInstaled: ArrayList<AppInstaledModel>
    val TAG_APPS = "apps"
    var resultReq:String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateChangeFragmentBinding.inflate(layoutInflater, container, false)
//        listAppInstaled = arguments?.getSerializable(TAG_APPS) as ArrayList<AppInstaledModel>
//        listAppInstaled = arguments?.getParcelableArrayList<AppInstaledModel>("List") as ArrayList<AppInstaledModel>



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        val viewModel = ViewModelProvider(this).get(CreateChangeViewModel::class.java)
        var listAppInstaledAdapter: List<AppInstaledModel>
        val listNameMode = ArrayList<String>()
        viewModel.initDatabase()

        recyclerView = binding.rvAppsCreate
        adapter = AppAdapter()
        recyclerView.adapter = adapter

        listAppInstaled = viewModel.getInstaledApps() as ArrayList<AppInstaledModel>

        adapter.setList(listAppInstaled)   //viewModel.getInstaledApps()
//        adapter.setList(viewModel.getInstaledApps())
//        adapter.setListPack(listPackApps)
        //костыль, без прокрутки не выделяет checkbox (значения ischecked с list) у первых элементов
        recyclerView.scrollToPosition(10)

        //слушатель на кнопку сохр
        binding.btnSaveCreate.setOnClickListener{
            //ADD MODE IN DB
            val titleMode = binding.etTitleModeCreate.text.toString()
            //проверка на уникальность имени
            if (!listNameMode.contains(titleMode)){
                viewModel.insertMode(ModeModel(title = titleMode))

                //ADD APPS IN DB
                //получение списка с измененными состояниями cb сохранение в БД AppModel на сонове значений cb из AppInstaledModel
                listAppInstaledAdapter = adapter.getList()

                //формирование списка id приложений для сохранения в БД
                for (itemApp in listAppInstaledAdapter){
                    if (itemApp.ischecked){
                        val pack = itemApp.pack
                        //проверку на существование записи в БД по title
                        //ADD APP
                        viewModel.insertApp(AppModel(pack = pack))

                        //ADD MODE_APP IN DB
                        viewModel.insertModeApp(Mode_AppModel(title_mode = titleMode, pack_app = pack))
                    }
                }
                APP.navController.navigate(R.id.modesFragment)
            } else Toast.makeText(context, "Такое название режима уже существует", Toast.LENGTH_SHORT).show()
        }

        binding.btnSearchCreate.setOnClickListener{
            val txtSearch = binding.etTitleAppCreate.text.toString()
            Log.e("app title", txtSearch)

            val listInstalledApps = adapter.getList()

            val listTitleAppsLower = mutableListOf<String>()
            for (itemInsApps in listInstalledApps) listTitleAppsLower.add(itemInsApps.title.lowercase())

            val result = listTitleAppsLower.filter { it.contains(txtSearch) }

            if (result.isNotEmpty()) Log.e("result search", result[0])

            var count = 0
            if(result.size>0){
                for (itemInstApps in listInstalledApps){
                    if (itemInstApps.title.lowercase().equals(result[0])){
                        recyclerView.scrollToPosition(count+6)
                    }
                    count++
                }
            }else Toast.makeText(context, "Похожих приложений не найдено", Toast.LENGTH_SHORT).show()
        }
    }
}