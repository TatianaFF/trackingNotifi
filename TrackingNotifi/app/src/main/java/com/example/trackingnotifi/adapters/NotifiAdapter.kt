package com.example.trackingnotifi.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingnotifi.R
import com.example.trackingnotifi.models.NotifiModelList
import kotlinx.android.synthetic.main.item_notifi_layout.view.*


class NotifiAdapter: RecyclerView.Adapter<NotifiAdapter.NotifiViewHolder> (){

    var listNotifi = ArrayList<NotifiModelList>()
    lateinit var contextAd: Context

    class NotifiViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifi_layout, parent, false)
        return NotifiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifiViewHolder, position: Int) {
        val name_app = holder.itemView.name_app_notifi
        val app_icon = holder.itemView.app_icon_notifi
        val from = holder.itemView.from
        val message = holder.itemView.message
        val date = holder.itemView.date

        val notifiCurrent = listNotifi[position]

        name_app.text = notifiCurrent.name_app
        app_icon.setImageDrawable(notifiCurrent.icon)
        from.text = notifiCurrent.from
        message.text = notifiCurrent.message
        date.text = notifiCurrent.date
    }

    override fun getItemCount(): Int {
        return listNotifi.size
    }

    @JvmName("setListNotifi1")
    fun setListNotifi(listNotifi: ArrayList<NotifiModelList>){
        this.listNotifi = listNotifi
        notifyDataSetChanged()
    }

    fun setContext(context: Context){
        contextAd = context
        notifyDataSetChanged()
    }

    //при нажатии на уведомление переходить к приложению
    override fun onViewAttachedToWindow(holder: NotifiViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            val launchIntent: Intent? = contextAd.packageManager.getLaunchIntentForPackage(listNotifi[holder.adapterPosition].pack)
            contextAd.startActivity(launchIntent)
        }
    }
}