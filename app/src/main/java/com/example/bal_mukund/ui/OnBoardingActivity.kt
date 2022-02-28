package com.example.bal_mukund.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.bal_mukund.R
import com.example.bal_mukund.adapters.onboarding.OnBoardingAdapter
import com.example.bal_mukund.adapters.onboarding.OnBoardingItem
import com.example.bal_mukund.databinding.ActivityOnBoardingBinding


class OnBoardingActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy {
        ActivityOnBoardingBinding.inflate(layoutInflater)
    }

    private lateinit var onBoardingAdapter: OnBoardingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBoardingAdapter = OnBoardingAdapter(getOnBoardingItems())
        binding.onboardingViewpager.apply {
            adapter = onBoardingAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        binding.onboardingViewpager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.getStartedBtn.visibility = View.VISIBLE
                    binding.skipBtn.visibility = View.GONE
                    binding.nextBtn.visibility = View.GONE
                }
                else {
                    binding.getStartedBtn.visibility = View.GONE
                    binding.skipBtn.visibility = View.VISIBLE
                    binding.nextBtn.visibility = View.VISIBLE
                }

//                when(position){
//                    0 ->{binding.onBoardingCL.setBackgroundColor(Color.parseColor("#FAD8FF"))}
//                    1 ->{binding.onBoardingCL.setBackgroundColor(Color.parseColor("#FAD8FF"))}
//                    2 ->{binding.onBoardingCL.setBackgroundColor(Color.parseColor("#FAD8FF"))}
//                }
            }
        })
        binding.indicator.setViewPager2(binding.onboardingViewpager)

        binding.getStartedBtn.setOnClickListener(this)
        binding.skipBtn.setOnClickListener(this)
        binding.nextBtn.setOnClickListener(this)
    }



    private fun getOnBoardingItems(): Array<OnBoardingItem> {
        return arrayOf(
            OnBoardingItem(R.drawable.onboarding1,"Be Healthy!","Being healthy is so important for us children"),
            OnBoardingItem(R.drawable.onboarding2,"Learn new things!","To be active learner and learn interesting things, you have to visit our app daily!"),
            OnBoardingItem(R.drawable.onboarding3,"Play with us!","Every child should play. Come to play with us!")
        )
    }

    override fun onClick(view: View?) {
        val viewPager = binding.onboardingViewpager
        if (view?.id == R.id.next_btn){
            if (viewPager.currentItem != 2){
                viewPager.currentItem = viewPager.currentItem +1
            }
            return
        }
        startActivity(Intent(this,MainActivity::class.java))
        finish()



    }

}