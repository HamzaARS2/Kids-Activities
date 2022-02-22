package com.example.bal_mukund.adapters.onboarding

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bal_mukund.R

class OnBoardingAdapter(private val onBoardingItems:Array<OnBoardingItem>) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_layout_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(onBoardingItems[position])
    }

    override fun getItemCount(): Int  = onBoardingItems.size

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val onBoardingImg:ImageView
        val titleTv:TextView
        val descTv:TextView

        init {
            onBoardingImg = itemView.findViewById(R.id.onboarding_img)
            titleTv = itemView.findViewById(R.id.onboarding_title_tv)
            descTv = itemView.findViewById(R.id.onboarding_desc_tv)
        }

        fun bind(item:OnBoardingItem){
            onBoardingImg.setImageResource(item.imageRes)
            titleTv.text = item.title
            descTv.text = item.description
        }

    }
}