package com.example.trackingnotifi.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.APP
import com.example.trackingnotifi.R
import com.example.trackingnotifi.models.AppModel
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.screens.ListOfModes.ModesFragment
import kotlinx.android.synthetic.main.item_mode_layout.view.*

import android.app.Activity
import android.content.Context
import java.lang.ClassCastException
import java.util.ArrayList


class ModeAdapter() : RecyclerView.Adapter<ModeAdapter.ModeViewHolder>() {
    var listModes = emptyList<ModeModel>()
//    val listStatus = ArrayList<Boolean>()
//    val isChecked = false

    var onItemClick: ((ModeModel) -> (Unit))? = null

    //создание холдера
    class ModeViewHolder(view: View): RecyclerView.ViewHolder(view)

    //какой шаблон элемента брать холдеру
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mode_layout, parent, false)
        return ModeViewHolder(view)
    }

    //присваивание значений из листа холдеру
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onBindViewHolder(holder: ModeViewHolder, position: Int) {
        val switch = holder.itemView.switch1
        val modeCurrent = listModes[position]

//        for (itemStatus in listStatus) Log.e("ModeAdaper", itemStatus.toString())

        holder.itemView.name_mode.text = modeCurrent.title

        switch.isChecked = modeCurrent.status
        switch.setTag(position)
        switch.setOnClickListener(View.OnClickListener { v ->
            val currentPos = v.tag as Int
            val curMode = listModes.get(currentPos)
            var isChecked = false
            if (!curMode.status) {
                isChecked = true
            }
            curMode.status = isChecked
            onItemClick?.invoke(curMode)
        })
    }

    override fun getItemCount(): Int {
        return listModes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ModeModel>){
        listModes = list
        notifyDataSetChanged()
    }

    fun getList(): List<ModeModel>{
        return listModes
    }

    override fun onViewAttachedToWindow(holder: ModeViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            ModesFragment.clickMode(listModes[holder.adapterPosition])
        }
    }
}