package com.example.bal_mukund.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.bal_mukund.R
import com.example.bal_mukund.databinding.ActivityOnBoardingBinding
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage

class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding:ActivityOnBoardingBinding
    lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager = supportFragmentManager

        val onBoardingFrag = PaperOnboardingFragment.newInstance(getElements())

        val ft = fragmentManager.beginTransaction()
        ft.add(R.id.onBoardingFragments_container,onBoardingFrag)
        ft.commit()

        onBoardingFrag.setOnRightOutListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }

    private fun getElements(): ArrayList<PaperOnboardingPage> {

        return arrayListOf(
            PaperOnboardingPage("Be Healthy!",
                "Being healthy is so important for us children",
                Color.parseColor("#FFA000"),
                R.drawable.kid_doctors,R.drawable.ic_healthcare_onboarding),
            PaperOnboardingPage("Learn new things!",
                "To be active learner and learn interesting things, you have to visit our app daily!",
                Color.parseColor("#FFA000"),
                R.drawable.onboarding2,R.drawable.ic_learn_onboarding),
            PaperOnboardingPage("Play with us!",
                "Every child should play.Come to play with us!",
                Color.parseColor("#FFA000"),
                R.drawable.onboarding3,R.drawable.ic_play_onboarding),
        )
    }
}