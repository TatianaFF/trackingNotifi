package com.example.trackingnotifi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.R
import com.example.trackingnotifi.models.ModeModel
import com.example.trackingnotifi.screens.ListOfModes.ModesFragment
import kotlinx.android.synthetic.main.item_mode_layout.view.*


class ModeAdapter(val view: View?) : RecyclerView.Adapter<ModeAdapter.ModeViewHolder>() {
    private var listModes = emptyList<ModeModel>()
    //функциональный тип
    var onItemClick: ((ModeModel) -> (Unit))? = null

    class ModeViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mode_layout, parent, false)
        return ModeViewHolder(view)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onBindViewHolder(holder: ModeViewHolder, position: Int) {
        val switch = holder.itemView.switch1
        val modeCurrent = listModes[position]

        holder.itemView.name_mode.text = modeCurrent.title

        switch.isChecked = modeCurrent.status
        switch.tag = position
        switch.setOnClickListener(View.OnClickListener { v ->
            val currentPos = v.tag as Int
            val curMode = listModes[currentPos]
            var isChecked = false
            if (!curMode.status) {
                isChecked = true
            }
            curMode.status = isChecked

            //вызов значения экземпляра функционального типа и передача текущего режима
            onItemClick?.invoke(curMode)
        })
    }

    override fun getItemCount(): Int {
        return listModes.size
    }

    //присваивание и обновление списка режимов
    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ModeModel>){
        listModes = list
        notifyDataSetChanged()
    }

    //вызов метода ModesFragment.clickMode при нажатии на карточку режима
    override fun onViewAttachedToWindow(holder: ModeViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            ModesFragment.clickMode(listModes[holder.adapterPosition], view)
        }
    }
}