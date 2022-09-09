package com.example.trackingnotifi.screens.CreateChangeMode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import kotlinx.android.synthetic.main.create_change_fragment.*

class CreateChangeFragment : Fragment() {

    lateinit var binding: CreateChangeFragmentBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AppAdapter
    lateinit var listAppInstaled: ArrayList<AppInstaledModel>
    var currentMode: ModeModel? = null
    var listAppsByTitleMode = listOf<AppModel>()
    lateinit var etTitleMode: EditText
    var allModesObs = ArrayList<ModeModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateChangeFragmentBinding.inflate(layoutInflater, container, false)
//        listAppInstaled = arguments?.getSerializable(TAG_APPS) as ArrayList<AppInstaledModel>
//        listAppInstaled = arguments?.getParcelableArrayList<AppInstaledModel>("List") as ArrayList<AppInstaledModel>

        currentMode = arguments?.let {
            it.getSerializable("mode") as ModeModel
        }

        if (currentMode!=null) binding.btnDelete.visibility = Button.VISIBLE

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
        val listPacksAppsByTitleMode = mutableListOf<String>()
        viewModel.initDatabase()



        // почитать про obs
        viewModel.getAllModes().observe(viewLifecycleOwner) { listModes ->
            allModesObs = listModes as ArrayList<ModeModel>
            for (itemMode in listModes){
                Log.e("DF_", itemMode.title)
                listNameMode.add(itemMode.title)
            }
        }

        recyclerView = binding.rvApps
        adapter = AppAdapter()
        recyclerView.adapter = adapter

        //packs app by title mode
        currentMode?.let {
            viewModel.getAppsByTitleMode(it.title).observe(viewLifecycleOwner) { listPackageAppByTitleMode ->
                for (itemPackApp in listPackageAppByTitleMode) {
                    listPacksAppsByTitleMode.add(itemPackApp.pack)
                    Log.e("PackApp: ", itemPackApp.pack)
                }
    //            Log.e("AppPack: ", listAppsByTitleMode.size.toString())
    //            for (itemPackApp in listAppsByTitleMode) Log.e("AppPack: ", itemPackApp.pack)
            }
        }

        adapter.setList(viewModel.getInstaledApps())

        //let
        adapter.setListPack(listPacksAppsByTitleMode)

        //костыль, без прокрутки не выделяет checkbox (значения ischecked с list) у первых элементов
        recyclerView.scrollToPosition(10)

        etTitleMode = binding.etTitleMode
        etTitleMode.setText(currentMode?.title)


        binding.btnDelete.setOnClickListener{
            currentMode?.let { it1 -> viewModel.deleteMode(it1) }

            for (itemApp in listAppsByTitleMode) viewModel.deleteApp(itemApp)

            currentMode?.let { it1 ->
                viewModel.getAllModeAppByTitleMode(it1.title).observe(viewLifecycleOwner) { listAllModeApp ->
                    for (itemModeApp in listAllModeApp) viewModel.deleteModeApp(itemModeApp)
                }
            }

            APP.navController.navigate(R.id.modesFragment)
        }

        //слушатель на кнопку сохр
        binding.btnSave.setOnClickListener{
            //update
            if (currentMode!=null){
                val titleMode = binding.etTitleMode.text.toString()
                var countEquals = 0
                for (itemNameMode in listNameMode) if (itemNameMode == titleMode) countEquals++
                if (!listNameMode.contains(titleMode) || titleMode == currentMode!!.title){
                    Log.e("contains", "no")
                    viewModel.getAllModeAppByTitleMode(currentMode!!.title).observe(viewLifecycleOwner) { listAllModeApp ->
                        for (itemModeApp in listAllModeApp) viewModel.deleteModeApp(itemModeApp)
                    }
                    viewModel.getAppsByTitleMode(currentMode!!.title).observe(viewLifecycleOwner) { listAllApps ->
                        for (itemApp in listAllApps) viewModel.deleteApp(itemApp)
                    }

                    viewModel.deleteMode(currentMode!!)

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
                } else Toast.makeText(context, "Такое название режима уже существует", Toast.LENGTH_SHORT).show()

            }else{
                //save
                //ADD MODE IN DB
                val titleMode = binding.etTitleMode.text.toString()
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

        }

        binding.btnSearch.setOnClickListener{
            val txtSearch = binding.etTitleApp.text.toString()
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

