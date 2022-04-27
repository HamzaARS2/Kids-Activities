package com.example.kidsactivities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kidsactivities.R
import com.example.kidsactivities.interfaces.RecentPostListener
import com.example.kidsactivities.model.Post

class PostAdapter(
    private val postList: ArrayList<Post>,
    private val listener: RecentPostListener
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recent_post_item_layout, parent, false)
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

    fun setPosts(posts: ArrayList<Post>) {
        postList.addAll(posts)
        notifyItemRangeInserted(0, itemCount)
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val postImage: ImageView = itemView.findViewById(R.id.post_recent_item_imageView)
        private val postQuoteTv: TextView = itemView.findViewById(R.id.post_recent_item_tv)
        private val postQuoteTitleTv: TextView = itemView.findViewById(R.id.post_recent_title_tv)
        private val postQuoteSubtitleTv: TextView =
            itemView.findViewById(R.id.post_recent_subTitle_tv)
        private val postCardView: CardView = itemView.findViewById(R.id.post_recent_item_cv)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(post: Post) {
            Glide.with(itemView).load(post.image)
                .dontAnimate()
                .into(postImage)
            postQuoteTitleTv.text = post.title
            postQuoteSubtitleTv.text = post.subtitle
            postQuoteTv.text = post.quote
            //postCardView.setCardBackgroundColor(Color.WHITE)

        }

        override fun onClick(p0: View?) {
            listener.onPostClicked(postList[adapterPosition], postImage, postQuoteTv)
        }

    }

    fun filterList(filteredList: ArrayList<Post>) {
        val count = itemCount
        postList.clear()
        notifyItemRangeRemoved(0,count)
        postList.addAll(filteredList)
        notifyItemRangeInserted(0,itemCount)

    }

}