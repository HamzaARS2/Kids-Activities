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
        return PostOneViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.post_type_item, parent, false),
                listener
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = posts.size



    fun setPosts(posts: ArrayList<Post>){
        posts.addAll(posts)
        notifyItemRangeInserted(0,itemCount)
    }


    inner class PostOneViewHolder(itemView: View, private val mListener: PostClickListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {





        override fun onClick(view: View?) {
            mListener.onPostTypeClicked(Constants.POST_TYPE_ONE)
        }
    }

    inner class PostTwoViewHolder(itemView: View, private val mListener: PostClickListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {



        override fun onClick(view: View?) {
            mListener.onPostTypeClicked(Constants.POST_TYPE_TWO)
        }
    }

    interface PostClickListener {
        fun onPostTypeClicked(postType: Int)
    }
}