package com.segihovav.mylinks_android

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ManageInstanceLinksRecyclerviewAdapter internal constructor(private val mContext: Context, private val myLinks: MutableList<String>) : RecyclerView.Adapter<ManageInstanceLinksRecyclerviewAdapter.MyViewHolder>() {
     private var darkMode: Boolean = false
     private var rowFG: LinearLayout? = null

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
          val view: View = LayoutInflater.from(mContext).inflate(R.layout.manage_instance_link_item, parent, false)

          rowFG=view.findViewById(R.id.rowFG)

          return MyViewHolder(view)
     }

     override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          if (position >= DataService.instanceURLs.size)
               return

          holder.linkName.text = DataService.instanceURLs[position].displayName
          //holder.linkName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80F) // tried to set text size
          holder.linkInfo.text = DataService.instanceURLs[position].url

          if (darkMode)
               rowFG?.setBackgroundColor(Color.GRAY)
     }

     override fun getItemCount(): Int {
          return myLinks.size
     }

     @JvmName("setDarkMode1")
     fun setDarkMode(_darkMode: Boolean) {
          this.darkMode=_darkMode
     }

     inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val linkName: TextView = itemView.findViewById(R.id.link_name)
          val linkInfo: TextView = itemView.findViewById(R.id.link_info)
     }
}
