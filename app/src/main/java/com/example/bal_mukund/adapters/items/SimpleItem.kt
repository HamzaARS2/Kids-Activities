package com.example.bal_mukund.adapters.items

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.bal_mukund.R
import com.example.bal_mukund.adapters.DrawerAdapter


/**
 * Created by yarolegovich on 25.03.2017.
 */
 class SimpleItem(private val icon: Drawable, private val title: String) :
    DrawerItem<SimpleItem.ViewHolder?>() {
    private var selectedItemIconTint = 0
    private var selectedItemTextTint = 0
    private var normalItemIconTint = 0
    private var normalItemTextTint = 0
    override fun createViewHolder(parent: ViewGroup?): ViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val v: View = inflater.inflate(R.layout.nav_drawer_item, parent, false)
        return ViewHolder(v)
    }

    private fun bindMyViewHolder(holder: ViewHolder) {
        holder.title.text = title
        holder.icon.setImageDrawable(icon)
        holder.title.setTextColor(if (isChecked) selectedItemTextTint else normalItemTextTint)
        holder.icon.setColorFilter(if (isChecked) selectedItemIconTint else normalItemIconTint)
    }

    fun withSelectedIconTint(selectedItemIconTint: Int): SimpleItem {
        this.selectedItemIconTint = selectedItemIconTint
        return this
    }

    fun withSelectedTextTint(selectedItemTextTint: Int): SimpleItem {
        this.selectedItemTextTint = selectedItemTextTint
        return this
    }

    fun withIconTint(normalItemIconTint: Int): SimpleItem {
        this.normalItemIconTint = normalItemIconTint
        return this
    }

    fun withTextTint(normalItemTextTint: Int): SimpleItem {
        this.normalItemTextTint = normalItemTextTint
        return this
    }

    inner class ViewHolder(itemView: View) : DrawerAdapter.ViewHolder(itemView) {
         val icon: ImageView
         val title: TextView

        init {
            icon = itemView.findViewById(R.id.drawer_item_img)
            title = itemView.findViewById(R.id.drawer_item_tv)
        }
    }


    override fun bindViewHolder(holder: DrawerAdapter.ViewHolder) {
        val myHolder = holder as ViewHolder
       bindMyViewHolder(myHolder)
    }
}