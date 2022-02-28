package com.example.bal_mukund.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bal_mukund.R
import com.example.bal_mukund.databinding.FragmentTodayBinding
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.viewmodels.MainViewModel
import com.google.android.material.shape.MaterialShapeUtils

class TodayFragment : Fragment(), RequestListener<Drawable> {



    private val binding by lazy {
        FragmentTodayBinding.inflate(layoutInflater)
    }

    private lateinit var mListener:PostLoadingCompleteListener
    private lateinit var todayPost: Post

    companion object {
       const val TODAY_POST_CODE = "today's Post"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as PostLoadingCompleteListener
    }

    fun newInstance(post: Post) =
        TodayFragment().apply {
            val bundle = Bundle()
            bundle.putSerializable(TODAY_POST_CODE,post)
            arguments = bundle
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todayPost = arguments?.getSerializable(TODAY_POST_CODE) as Post


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return binding.root
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            Glide.with(this@TodayFragment).load(todayPost.imageUrl).listener(this@TodayFragment).into(binding.todayPostImage)
            binding.todayPostQuoteTv.apply {
                text = todayPost.quote
                textSize = todayPost.textSize
                setTextColor(Color.parseColor(todayPost.textColor))
            }

        }
    }

    interface PostLoadingCompleteListener {
        fun onFullPostLoadingCompleted()
        fun onFullPostLoadingFailed(e:Exception)
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        mListener.onFullPostLoadingFailed(e!!)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        mListener.onFullPostLoadingCompleted()
        return false
    }


}