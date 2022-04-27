package com.example.kidsactivities.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kidsactivities.R
import com.example.kidsactivities.databinding.FragmentTodayBinding
import com.example.kidsactivities.model.Post

import android.graphics.drawable.GradientDrawable

import android.util.TypedValue
import android.view.*
import com.example.kidsactivities.interfaces.FragmentInteraction
import com.example.kidsactivities.viewmodels.TodayFragmentViewModel


class TodayFragment : Fragment(), RequestListener<Drawable>, View.OnClickListener {


    private val binding by lazy {
        FragmentTodayBinding.inflate(layoutInflater)
    }

    private val todayViewModel by lazy {
        ViewModelProvider(this)[TodayFragmentViewModel::class.java]
    }

    private lateinit var mListener: PostLoadingCompleteListener
    private lateinit var scrollingListener: FragmentInteraction
    private lateinit var todayPost: Post

    companion object {
        const val TODAY_POST_CODE = "today's Post"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as PostLoadingCompleteListener
        scrollingListener = context as FragmentInteraction
    }

    fun newInstance(post: Post) =
        TodayFragment().apply {
            val bundle = Bundle()
            bundle.putSerializable(TODAY_POST_CODE, post)
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
            Glide.with(this@TodayFragment).load(todayPost.image).listener(this@TodayFragment)
                .into(todayPostImage)
            todayPostTitleTv.apply {
                text = todayPost.subtitle
            }
            todayPostQuoteTv.apply {
                text = todayPost.quote

            }

            todayPostZoomIn.setOnClickListener(this@TodayFragment)
            todayPostZoomOut.setOnClickListener(this@TodayFragment)

        }



        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_nav_menu)
            setNavigationOnClickListener {
                scrollingListener.onNavIconClicked()
            }
        }
        binding.todayPostCollapsingToolbar.apply {
            title = todayPost.title
            setCollapsedTitleTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.fredoka_one
                )
            )
            setExpandedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.fredoka_one))
            title = todayPost.title


        }


        binding.todayPostNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            scrollingListener.onScrolling(scrollY)
        })
    }


    interface PostLoadingCompleteListener {
        fun onFullPostLoadingCompleted()
        fun onFullPostLoadingFailed(e: Exception)
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

        val bitmap: Bitmap = (resource as BitmapDrawable).bitmap
        Palette.from(bitmap)
            .generate { palette ->
                val bgColor = palette?.getDominantColor(Color.WHITE)
                val textColor = palette?.dominantSwatch?.bodyTextColor
                if (bgColor != null) {
                    when (val background = binding.todayPostCollapsingToolbar.background) {
                        is GradientDrawable -> {
                            // cast to 'GradientDrawable'
                            background.setColor(bgColor)
                        }

                    }
                }
                if (textColor != null) {
                    binding.run {
                        toolbar.setNavigationIconTint(textColor)
                        todayPostCollapsingToolbar.apply {
                            setExpandedTitleColor(textColor)
                            setCollapsedTitleTextColor(textColor)
                        }

                    }

                }
            }
        mListener.onFullPostLoadingCompleted()
        return false
    }

    override fun onClick(view: View?) {
        val pixels = binding.todayPostQuoteTv.textSize
        var textSize = convertToSp(pixels)
        when(view?.id){
            R.id.today_post_zoom_in -> {
                textSize = todayViewModel.zoomIn(textSize)
            }
            R.id.today_post_zoom_out -> {
                textSize = todayViewModel.zoomOut(textSize)
            }
        }
        binding.todayPostQuoteTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize)
    }

    private fun convertToSp(px:Float) :Float
    = px/(resources.displayMetrics.scaledDensity)


}