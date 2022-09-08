package com.example.trackingnotifi.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.R
import com.example.trackingnotifi.models.AppInstaledModel
import kotlinx.android.synthetic.main.item_app_layout.view.*


class AppAdapter: RecyclerView.Adapter<AppAdapter.AppViewHolder>(
) {

    var listInstaledApp = emptyList<AppInstaledModel>()
    var listPacks = emptyList<String>()

    //создание холдера
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

        val appCurrent = listInstaledApp[position]
        //если pack есть в БД cb = true
        if (appCurrent.pack in listPacks){
            appCurrent.ischecked = true
        }
        //установить флажок на cb
        checkbox.setChecked(appCurrent.ischecked)
        name_app.text = appCurrent.title

        app_icon.setImageDrawable(appCurrent.icon)
//        app_icon.setImageDrawable(appCurrent.icon)


        // Tag is important to get position clicked checkbox
        checkbox.setTag(position)
        checkbox.setOnClickListener(View.OnClickListener { v ->
            val currentPos = v.tag as Int
            var isChecked = false
            //если не отмечен, отметить
            if (!listInstaledApp.get(currentPos).ischecked) {
                isChecked = true
            }
            listInstaledApp.get(currentPos).ischecked = isChecked
        })

    }

    override fun getItemCount(): Int {
        return listInstaledApp.size
    }

    //изменять лист объектов извне и обновлять состояние UI
    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<AppInstaledModel>){
        listInstaledApp = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setListPack1")
    fun setListPack(list: List<String>){
        listPacks = list
        notifyDataSetChanged()
    }

    fun getList(): List<AppInstaledModel> {
        return listInstaledApp
    }

    //при нажатии на приложение выделять cb
    override fun onViewAttachedToWindow(holder: AppViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            //
        }
    }


}