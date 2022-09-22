package com.example.trackingnotifi.screens.CreateChangeMode

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: CreateChangeFragmentBinding
    private var adapter: AppAdapter = AppAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnDelete: Button
    private lateinit var btnSave: Button
    private lateinit var etTitleMode: EditText
    private lateinit var etTitleAppSearch: EditText
    private var currentMode: ModeModel? = null
    private var listAppsByTitleMode = ArrayList<AppModel>()
    private var listAppChangedAdapter = ArrayList<AppInstaledModel>()
    private var listNamesMode = ArrayList<String>()
    private var listPacksAppsByTitleMode = ArrayList<String>()
    private var listModeAppByTitleMode = ArrayList<Mode_AppModel>()
    private var BROADCAST_NAME_ACTION = "com.example.trackingnotifi.NOTIFICATION_LISTENER_SERVICE"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateChangeFragmentBinding.inflate(layoutInflater, container, false)

        //инициализация view компонентов
        btnDelete = binding.btnDelete
        btnSave = binding.btnSave
        etTitleMode = binding.etTitleMode
        etTitleAppSearch = binding.etTitleApp
        recyclerView = binding.rvApps
        recyclerView.adapter = adapter

        //получение режима(ModeModel)
        currentMode = arguments?.let {
            it.getSerializable("mode") as ModeModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[CreateChangeViewModel::class.java]

        init(viewModel)

        when(currentMode){
            null -> initCreate(viewModel)
            else -> initDetail(viewModel, currentMode!!)
        }
    }

    //общая инициализация
    private fun init(viewModel: CreateChangeViewModel){
        //получение списка режимов из БД, заполнение listNameMode названиями режимов
        viewModel.getAllModes().observe(viewLifecycleOwner) { listModes ->
            listModes.forEach{ listNamesMode.add(it.title) }
        }

        //получение установленных приложений
        val listInstalledApps = viewModel.getInstaledApps()

        //отправление адаптеру приложений установленных на телефоне
        adapter.setListAppInstaled(listInstalledApps as ArrayList<AppInstaledModel>)


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val editText = s.toString()
                val editTextLower = editText.lowercase()

                //лист с названиями приложений в которых содержится строка txtSearch
//                val result = listTitleAppsLower.filter { it.contains(editTextLower) }
                val resultListSearch = listInstalledApps.filter { it.title.lowercase().contains(editTextLower) } as ArrayList<AppInstaledModel>

                if (resultListSearch.isEmpty()) Toast.makeText(context, "Приложение не найдено", Toast.LENGTH_SHORT).show()

                adapter.setListAppInstaled(resultListSearch)

            }
        }
        etTitleAppSearch.addTextChangedListener(textWatcher)

    }

    //создание (выполняется при нажатии на создание режима)
    private fun initCreate(viewModel: CreateChangeViewModel) {
        //скрыть кнопку удаления
        btnDelete.visibility = Button.INVISIBLE

        btnSave.setOnClickListener{
            val titleMode = etTitleMode.text.toString()
            //проверка имени на уникальность
            if (!listNamesMode.contains(titleMode)){
                //сохранение режима в БД
                viewModel.insertMode(ModeModel(title = titleMode))

                //получение списка с измененными состояниями checkbox
                listAppChangedAdapter = adapter.getChangedListInstaledApps() as ArrayList<AppInstaledModel>

                listAppChangedAdapter.forEach{ if (it.ischecked){
                    val pack = it.pack

                    //сохранение приложения в БД
                    viewModel.insertApp(AppModel(pack = pack))

                    //сохранение связи режим_приложение в БД
                    viewModel.insertModeApp(Mode_AppModel(title_mode = titleMode, pack_app = pack))
                } }
                APP.navController.navigate(R.id.modesFragment)
            } else printMessageNotUnique()
        }
    }

    //редактирование (выполняется при нажатии на карточку режима)
    private fun initDetail(viewModel: CreateChangeViewModel, currentMode: ModeModel) {
        //отобразить кнопку удаления
        btnDelete.visibility = Button.VISIBLE

        //костыль, без прокрутки не выделяет checkbox (значения ischecked с list) у первых элементов
        recyclerView.scrollToPosition(10)

        //отображение названия режима в edit
        etTitleMode.setText(currentMode.title)

        //заполнение listPacksAppsByTitleMode пакетами приложений и listAppsByTitleMode приложениями, принадлежащих режиму
        viewModel.getAppsByTitleMode(currentMode.title).observe(viewLifecycleOwner) { listPackageApp ->
            listPackageApp.forEach {
                listAppsByTitleMode.add(it)
                listPacksAppsByTitleMode.add(it.pack)}
        }
        //отправление адаптеру пакетов приложений принадлежащих режиму
        adapter.setListPack(listPacksAppsByTitleMode)

        //получение связей режима и приложений и заполнение листа
        viewModel.getAllModeAppByTitleMode(currentMode.title).observe(viewLifecycleOwner){ listAllModeAppByTitleMode ->
            listAllModeAppByTitleMode.forEach{ listModeAppByTitleMode.add(it) }
        }

        //костыль, без прокрутки не выделяет checkbox (значения ischecked с list) у первых элементов
        recyclerView.scrollToPosition(10)
        Log.d(TAG, "костыль scrollToPosition")

        btnDelete.setOnClickListener{
            //удаление режима
            viewModel.deleteMode(currentMode)

            //удаление приложений принадлежащих режиму
            listAppsByTitleMode.forEach{ viewModel.deleteApp(it) }

            //удаление связей режим_приложение
            listModeAppByTitleMode.forEach{ viewModel.deleteModeApp(it) }

            //если нет режимов остановить сервис
            stopServ()

            APP.navController.navigate(R.id.modesFragment)
        }

        btnSave.setOnClickListener{
            val titleMode = etTitleMode.text.toString()

            //проверка названия режима на уникальность
            if (!listNamesMode.contains(titleMode) || titleMode == currentMode.title){
                //удаление связи режим_приложение
                listModeAppByTitleMode.forEach{ viewModel.deleteModeApp(it) }

                //удаление приложений принадлежащих режиму
                listAppsByTitleMode.forEach { viewModel.deleteApp(it) }

                //удаление режима
                viewModel.deleteMode(currentMode)

                //сохранение режима
                viewModel.insertMode(ModeModel(title = titleMode))

                //получение списка с измененными состояниями checkbox
                val listAppInstaledAdapter = adapter.getChangedListInstaledApps()

                //формирование списка id приложений для сохранения в БД
                for (itemApp in listAppInstaledAdapter){
                    if (itemApp.ischecked){
                        val pack = itemApp.pack
                        //сохранение выбранных приложений
                        viewModel.insertApp(AppModel(pack = pack))

                        //сохранение связи режим_приложение
                        viewModel.insertModeApp(Mode_AppModel(title_mode = titleMode, pack_app = pack))
                    }
                }
                APP.navController.navigate(R.id.modesFragment)
            } else printMessageNotUnique()
        }
    }

    private fun stopServ(){
        val intent1 = Intent(BROADCAST_NAME_ACTION)
        intent1.putExtra("onStopService", "onStopService")
        LocalBroadcastManager.getInstance(activity!!.applicationContext).sendBroadcast(intent1)
    }

    private fun printMessageNotUnique(){
        Toast.makeText(context, "Название режима должно быть уникальным", Toast.LENGTH_SHORT).show()
    }
}

