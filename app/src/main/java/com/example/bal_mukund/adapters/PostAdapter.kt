package com.example.bal_mukund.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bal_mukund.R
import com.example.bal_mukund.model.Post

class PostAdapter(private val postList:ArrayList<Post>,private val listener:RecentPostListener): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder
    = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recent_post_layout,parent,false))

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

    fun setPosts(posts:ArrayList<Post>){
        postList.addAll(posts)
        notifyItemRangeInserted(0,itemCount)
    }
    inner class PostViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val postImageView:ImageView
        val postTitleTv:TextView
        val postBodyTv:TextView
        val bgImage:ImageView
        init {
            postImageView = itemView.findViewById(R.id.post_imageView)
            postTitleTv = itemView.findViewById(R.id.post_title_tv)
            postBodyTv = itemView.findViewById(R.id.recent_post_body_tv)
            bgImage = itemView.findViewById(R.id.recent_post_item_bgImage)

            itemView.setOnClickListener(this)
        }

        fun bind(post:Post){
            postImageView.load(post.imageUrl)
            bgImage.load(post.backgroundImg)
            postTitleTv.text = post.title
            postBodyTv.text = post.body
        }

        override fun onClick(p0: View?) {
            listener.onPostClicked(adapterPosition)
        }
    }

    interface RecentPostListener{
        fun onPostClicked(position:Int)
    }
}