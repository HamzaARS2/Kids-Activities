package com.example.bal_mukund.ui.fragments

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
import com.example.bal_mukund.R
import com.example.bal_mukund.model.Post
import com.example.bal_mukund.viewmodels.MainViewModel

class TodayFragment : Fragment() {

    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var postCV: CardView
    private lateinit var todayTopicTv: TextView
    private lateinit var titleTv: TextView
    private lateinit var bodyTv: TextView
    private lateinit var smallImage: ImageView
    private lateinit var bgImage: ImageView

    private lateinit var todayPost: Post

    companion object {
        const val POST_TYPE_ONE = 1
        const val POST_TYPE_TWO = 2

        const val INTENT_POST_TYPE = "postType"
        const val INTENT_POST_TOPIC = "postTopic"
        const val INTENT_POST_TITLE = "postTitle"
        const val INTENT_POST_BODY = "postBody"
        const val INTENT_POST_BG_IMAGE = "bgImage"
        const val INTENT_POST_SMALL_IMAGE = "smallImage"
    }

    fun newInstance(post: Post): TodayFragment {
        val fragment = TodayFragment()
        val bundle = Bundle()
        bundle.putInt(INTENT_POST_TYPE, post.type)
        bundle.putString(INTENT_POST_TOPIC, post.topic)
        bundle.putString(INTENT_POST_TITLE, post.title)
        bundle.putString(INTENT_POST_BODY, post.body)
        bundle.putString(INTENT_POST_BG_IMAGE, post.backgroundImg)
        bundle.putString(INTENT_POST_SMALL_IMAGE, post.imageUrl)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = arguments?.get(INTENT_POST_TYPE) as Int
        val topic = arguments?.get(INTENT_POST_TOPIC) as String
        val title = arguments?.get(INTENT_POST_TITLE) as String
        val body = arguments?.get(INTENT_POST_BODY) as String
        val bgImage = arguments?.get(INTENT_POST_BG_IMAGE) as String
        val smallImage = arguments?.get(INTENT_POST_SMALL_IMAGE) as String
        todayPost = Post(type, bgImage, smallImage, topic, title, body)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return initViews(inflater, container, todayPost.type)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bgImage.load(todayPost.backgroundImg)
        smallImage.load(todayPost.imageUrl)
        todayTopicTv.text = todayPost.topic
        titleTv.text = todayPost.title
        bodyTv.text = todayPost.body
    }

    private fun initViews(inflater: LayoutInflater, container: ViewGroup?, type: Int): View {
        val view: View?
        if (type == 1) {
            view = inflater.inflate(R.layout.post_type_item, container, false)
            postCV = view.findViewById(R.id.post_one_cardView)
            todayTopicTv = view.findViewById(R.id.post_one_topic)
            titleTv = view.findViewById(R.id.post_one_title)
            bodyTv = view.findViewById(R.id.post_one_body)
            smallImage = view.findViewById(R.id.post_one_kidsImage)
            bgImage = view.findViewById(R.id.post_one_bg_image)
        } else {
            view = inflater.inflate(R.layout.post_type_item_two, container, false)
            postCV = view.findViewById(R.id.post_two_cardView)
            todayTopicTv = view.findViewById(R.id.post_two_today)
            titleTv = view.findViewById(R.id.post_two_title)
            bodyTv = view.findViewById(R.id.post_two_body)
            smallImage = view.findViewById(R.id.post_two_kidsImage)
            bgImage = view.findViewById(R.id.post_two_bg_image)
        }

        return view

    }


}