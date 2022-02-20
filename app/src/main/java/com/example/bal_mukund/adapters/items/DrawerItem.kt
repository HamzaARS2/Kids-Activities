package com.example.bal_mukund.adapters.items

import android.view.ViewGroup
import com.example.bal_mukund.adapters.DrawerAdapter


abstract class DrawerItem<T : DrawerAdapter.ViewHolder?> {
    var isChecked = false
        protected set

    abstract fun createViewHolder(parent: ViewGroup?): T
    abstract fun bindViewHolder(holder: DrawerAdapter.ViewHolder)
    fun setChecked(isChecked: Boolean): DrawerItem<T> {
        this.isChecked = isChecked
        return this
    }

    open val isSelectable: Boolean
        get() = true
}