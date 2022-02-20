package com.example.bal_mukund.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bal_mukund.R
import com.example.bal_mukund.model.Post

class ViewPagerAdapter(
    private val posts: ArrayList<Post> = arrayListOf(),
    private val listener: PostClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    object Constants {
        const val POST_TYPE_ONE = 1
        const val POST_TYPE_TWO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.POST_TYPE_ONE) {
            PostOneViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.post_type_item, parent, false),
                listener
            )
        } else {
            PostTwoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.post_type_item_two, parent, false), listener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]
        if (holder is PostOneViewHolder){
            holder.bind(post)
        }else{
            val holderTwo = holder as PostTwoViewHolder
            holderTwo.bind(post)
        }
    }

    override fun getItemCount(): Int = posts.size

    override fun getItemViewType(position: Int): Int {
      return if (posts[position].type == 1){
          Constants.POST_TYPE_ONE
      }else
          Constants.POST_TYPE_TWO

    }

    fun setPosts(posts: ArrayList<Post>){
        posts.addAll(posts)
        notifyItemRangeInserted(0,itemCount)
    }


    inner class PostOneViewHolder(itemView: View, private val mListener: PostClickListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val topicTv: TextView
        val titleTv: TextView
        val bodyTv: TextView
        val kidsImgView: ImageView
        val bgImageview: ImageView

        init {
            topicTv = itemView.findViewById(R.id.post_one_topic)
            titleTv = itemView.findViewById(R.id.post_one_title)
            bodyTv = itemView.findViewById(R.id.post_one_body)
            kidsImgView = itemView.findViewById(R.id.post_one_kidsImage)
            bgImageview = itemView.findViewById(R.id.post_one_bg_image)

            itemView.setOnClickListener(this)
        }

        fun bind(post: Post) {
            kidsImgView.load(post.imageUrl)
            bgImageview.load(post.backgroundImg)
            topicTv.text = post.topic
            titleTv.text = post.title
            bodyTv.text = post.body
        }


        override fun onClick(view: View?) {
            mListener.onPostTypeClicked(Constants.POST_TYPE_ONE)
        }
    }

    inner class PostTwoViewHolder(itemView: View, private val mListener: PostClickListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val topicTv: TextView
        val titleTv: TextView
        val bodyTv: TextView
        val kidsImgView: ImageView
        val bgImageview: ImageView

        init {
            topicTv = itemView.findViewById(R.id.post_two_today)
            titleTv = itemView.findViewById(R.id.post_two_title)
            bodyTv = itemView.findViewById(R.id.post_two_body)
            kidsImgView = itemView.findViewById(R.id.post_two_kidsImage)
            bgImageview = itemView.findViewById(R.id.post_two_bg_image)

            itemView.setOnClickListener(this)
        }

        fun bind(post: Post){
            kidsImgView.load(post.imageUrl)
            bgImageview.load(post.backgroundImg)
            topicTv.text = post.topic
            titleTv.text = post.title
            bodyTv.text = post.body
        }

        override fun onClick(view: View?) {
            mListener.onPostTypeClicked(Constants.POST_TYPE_TWO)
        }
    }

    interface PostClickListener {
        fun onPostTypeClicked(postType: Int)
    }
}