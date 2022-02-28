package com.example.bal_mukund.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Scene
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.example.bal_mukund.R
import com.example.bal_mukund.adapters.PostAdapter
import com.example.bal_mukund.databinding.FragmentRecentBinding
import com.example.bal_mukund.interfaces.FirestoreListener
import com.example.bal_mukund.interfaces.RecentPostListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.ui.RecentPostActivity
import com.example.bal_mukund.viewmodels.MainViewModel


class RecentFragment : Fragment(),FirestoreListener, RecentPostListener {

    private lateinit var postAdapter: PostAdapter
    private val binding by lazy {
        FragmentRecentBinding.inflate(layoutInflater)
    }
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var mListener: RecentPostListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as RecentPostListener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getAllPosts(this)
        postAdapter = PostAdapter(arrayListOf(),this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postRvLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.postsRecyclerView.apply {
            layoutManager = postRvLayoutManager
            adapter = postAdapter
            hasFixedSize()
        }

    }

    override fun onDataReceived(posts: ArrayList<Post>) {
        postAdapter.setPosts(posts)
        (view?.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onPostClicked(post: Post, image: ImageView, quoteTv:TextView) {
        mListener.onPostClicked(post,image,quoteTv)
    }





}