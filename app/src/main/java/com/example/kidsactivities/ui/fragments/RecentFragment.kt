package com.example.kidsactivities.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidsactivities.R
import com.example.kidsactivities.adapters.PostAdapter
import com.example.kidsactivities.databinding.FragmentRecentBinding
import com.example.kidsactivities.interfaces.FirestoreListener
import com.example.kidsactivities.interfaces.FragmentInteraction
import com.example.kidsactivities.interfaces.RecentPostListener
import com.example.kidsactivities.model.Post
import com.example.kidsactivities.viewmodels.MainViewModel


class RecentFragment : Fragment(),FirestoreListener, RecentPostListener, TextWatcher {

    private lateinit var postAdapter: PostAdapter
    private val binding by lazy {
        FragmentRecentBinding.inflate(layoutInflater)
    }
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var mListener: RecentPostListener
    private lateinit var mInteractionListener: FragmentInteraction
    private lateinit var allPosts:ArrayList<Post>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as RecentPostListener
        mInteractionListener = context as FragmentInteraction
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

        binding.recentPostSearchEdt.addTextChangedListener(this)
        binding.postsRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mInteractionListener.onScrolling(dy)
            }
        })

        binding.recentMToolbar.apply {
            setNavigationIcon(R.drawable.ic_nav_menu)
            setNavigationIconTint(resources.getColor(R.color.nav_icon_onPrimaryContainer,resources.newTheme()))
            setNavigationOnClickListener {
                mInteractionListener.onNavIconClicked()
            }
        }
    }

    override fun onDataReceived(posts: ArrayList<Post>) {
        allPosts = posts
        postAdapter.setPosts(allPosts)
        binding.postsRecyclerView.scheduleLayoutAnimation()
    }

    override fun onPostClicked(post: Post, image: ImageView, quoteTv:TextView) {
        mListener.onPostClicked(post,image,quoteTv)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val txt = p0.toString().trim()
        if (txt.isNotEmpty())
        filter(txt)
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    private fun filter(text:String){
        val postsList = arrayListOf<Post>()
        for (item in allPosts) {
            val txt = (item.title + item.subtitle + item.quote).lowercase()
            if (txt.contains(text.lowercase().trim())) {
                postsList.add(item)
            }
        }

        postAdapter.filterList(postsList)
    }



}