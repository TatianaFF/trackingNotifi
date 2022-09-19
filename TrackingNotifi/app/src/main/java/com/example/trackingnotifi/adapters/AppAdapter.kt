package com.example.trackingnotifi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.R
import com.example.trackingnotifi.models.AppInstaledModel
import kotlinx.android.synthetic.main.item_app_layout.view.*


class AppAdapter: RecyclerView.Adapter<AppAdapter.AppViewHolder>() {
    var listInstaledApps = ArrayList<AppInstaledModel>()
    var listPacks = ArrayList<String>()

    class AppViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_layout, parent, false)
        return AppViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val checkbox = holder.itemView.checkbox
        val name_app = holder.itemView.name_app
        val app_icon = holder.itemView.app_icon

        val appCurrent = listInstaledApps[position]

        //если приложение есть в БД ischecked = true
        if (appCurrent.pack in listPacks){
            appCurrent.ischecked = true
        }

        checkbox.isChecked = appCurrent.ischecked
        name_app.text = appCurrent.title
        app_icon.setImageDrawable(appCurrent.icon)

        // Tag is important to get position clicked checkbox
        checkbox.tag = position
        checkbox.setOnClickListener(View.OnClickListener { v ->
            val currentPos = v.tag as Int
            var isChecked = false
            //если не отмечен, отметить
            if (!listInstaledApps[currentPos].ischecked) {
                isChecked = true
            }
            listInstaledApps[currentPos].ischecked = isChecked
        })

    }

    override fun getItemCount(): Int {
        return listInstaledApps.size
    }

    //присваивание и обновление списка установленных приложений
    @SuppressLint("NotifyDataSetChanged")
    fun setListAppInstaled(list: ArrayList<AppInstaledModel>){
        listInstaledApps = list
        notifyDataSetChanged()
    }

    //присваивание и обновление списка пакетов приложений принадлежащих режиму
    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setListPack1")
    fun setListPack(list: ArrayList<String>){
        listPacks = list
        notifyDataSetChanged()
    }

    //возвращение измененного листа (состояния checkbox) установленных приложений
    fun getChangedListInstaledApps(): List<AppInstaledModel> {
        return listInstaledApps
    }
}