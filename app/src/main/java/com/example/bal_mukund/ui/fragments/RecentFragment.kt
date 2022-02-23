package com.example.bal_mukund.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bal_mukund.R
import com.example.bal_mukund.adapters.PostAdapter
import com.example.bal_mukund.databinding.FragmentRecentBinding
import com.example.bal_mukund.interfaces.FirestoreListener
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.ui.RecentPostPagerActivity
import com.example.bal_mukund.viewmodels.MainViewModel
import com.google.android.material.shape.MaterialShapeUtils


class RecentFragment : Fragment(),FirestoreListener, PostAdapter.RecentPostListener {

    lateinit var postAdapter: PostAdapter
    private lateinit var binding: FragmentRecentBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
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
        val binding = FragmentRecentBinding.inflate(inflater, container, false)

        val postRvLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        //postRvLayoutManager.stackFromEnd = true
            binding.postsRecyclerView.apply {
                layoutManager = postRvLayoutManager
                adapter = postAdapter
            }

        return binding.root
    }

    override fun onDataReceived(posts: ArrayList<Post>) {
        postAdapter.setPosts(posts)
    }

    override fun onPostClicked(position: Int) {
        val intent = Intent(activity,RecentPostPagerActivity::class.java)
        intent.putExtra("position",position)
        startActivity(intent)
    }


}