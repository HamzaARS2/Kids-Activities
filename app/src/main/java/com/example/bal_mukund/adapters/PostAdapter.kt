package com.example.bal_mukund.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bal_mukund.R
import com.example.bal_mukund.interfaces.RecentPostListener
import com.example.bal_mukund.model.Post

class PostAdapter(private val postList:ArrayList<Post>,private val listener:RecentPostListener): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder
    = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recent_post_item_layout,parent,false))

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
        private val postImage:ImageView = itemView.findViewById(R.id.post_recent_item_imageView)
        private val postQuoteTv:TextView = itemView.findViewById(R.id.post_recent_item_tv)
        private val postCardView:CardView = itemView.findViewById(R.id.post_recent_item_cv)
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(post:Post){
            Glide.with(itemView).load(post.imageUrl).listener(object :RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val bitmap:Bitmap = (resource as BitmapDrawable).bitmap
                    Palette.from(bitmap)
                        .generate { palette ->
                            val bgColor = palette?.getDominantColor(Color.WHITE)
                            val textColor = palette?.dominantSwatch?.bodyTextColor
                            if (bgColor != null)
                                postCardView.setCardBackgroundColor(bgColor)
                            if (textColor != null)
                                postQuoteTv.setTextColor(textColor)
                        }
                    return false
                }

            }).into(postImage)
            postQuoteTv.text = post.quote
            postCardView.setCardBackgroundColor(Color.WHITE)

        }

        override fun onClick(p0: View?) {
            listener.onPostClicked(postList[adapterPosition],postImage,postQuoteTv)
        }
    }

}