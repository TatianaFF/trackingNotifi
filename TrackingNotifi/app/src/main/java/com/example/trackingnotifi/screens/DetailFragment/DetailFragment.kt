package com.example.trackingnotifi.screens.DetailFragment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R
import com.example.trackingnotifi.adapters.AppAdapter
import com.example.trackingnotifi.databinding.DetailFragmentBinding
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.models.Mode_AppModel

class DetailFragment : Fragment() {         //, CoroutineScope

    lateinit var binding: DetailFragmentBinding
    lateinit var currentMode: ModeModel
    var listAppsByTitleMode = listOf<AppModel>()
    lateinit var recyclerView: RecyclerView
    lateinit var etTitleModeDetail: EditText
    lateinit var adapter: AppAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(layoutInflater, container, false)
        currentMode = arguments?.getSerializable("mode") as ModeModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("LongLogTag")
    private fun init() {
        val viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val listPacksAppsByTitleMode = mutableListOf<String>()
        viewModel.initDatabase()

        recyclerView = binding.rvAppsDetail
        adapter = AppAdapter()
        recyclerView.adapter = adapter

        //packs app by title mode
        viewModel.getAppsByTitleMode(currentMode.title).observe(viewLifecycleOwner,{listPackageAppByTitleMode ->
            for (itemPackApp in listPackageAppByTitleMode) {
                listPacksAppsByTitleMode.add(itemPackApp.pack)
                Log.e("PackApp: ", itemPackApp.pack)
            }
//            Log.e("AppPack: ", listAppsByTitleMode.size.toString())
//            for (itemPackApp in listAppsByTitleMode) Log.e("AppPack: ", itemPackApp.pack)
        })

        adapter.setList(viewModel.getInstaledApps())
        adapter.setListPack(listPacksAppsByTitleMode)
        //костыль, без прокрутки не выделяет checkbox (значения ischecked с list) у первых элементов
        recyclerView.scrollToPosition(10)

        etTitleModeDetail = binding.etTitleModeDetail
        etTitleModeDetail.setText(currentMode.title)


        binding.btnDeleteDetail.setOnClickListener{
            viewModel.deleteMode(currentMode)

            for (itemApp in listAppsByTitleMode) viewModel.deleteApp(itemApp)

            viewModel.getAllModeAppByTitleMode(currentMode.title).observe(viewLifecycleOwner, {listAllModeApp ->
                for (itemModeApp in listAllModeApp) viewModel.deleteModeApp(itemModeApp)
            })

            APP.navController.navigate(R.id.modesFragment)
        }

        binding.btnSearchDetail.setOnClickListener{
            val txtSearch = binding.etTitleAppDetail.text.toString()
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
            }else{
                Toast.makeText(context, "Похожих приложений не найдено", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpdateDetail.setOnClickListener{
            //написать нормальное обновление таблиц БД

            viewModel.getAllModeAppByTitleMode(currentMode.title).observe(viewLifecycleOwner, {listAllModeApp ->
                for (itemModeApp in listAllModeApp) viewModel.deleteModeApp(itemModeApp)
            })
            viewModel.getAppsByTitleMode(currentMode.title).observe(viewLifecycleOwner, {listAllApps ->
                for (itemApp in listAllApps) viewModel.deleteApp(itemApp)
            })

            viewModel.deleteMode(currentMode)

            //создание объектов
            //ADD MODE IN DB
            val titleMode = binding.etTitleModeDetail.text.toString()
            //сделать проверку на уникальность имени
            viewModel.insertMode(ModeModel(title = titleMode))
            //

            //ADD APPS IN DB
            //получение списка с измененными состояниями cb сохранение в БД AppModel на сонове значений cb из AppInstaledModel
            val listAppInstaledAdapter = adapter.getList()

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
        }
    }
}