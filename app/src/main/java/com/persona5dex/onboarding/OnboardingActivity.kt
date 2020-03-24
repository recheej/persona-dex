package com.persona5dex.onboarding

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.persona5dex.R
import com.persona5dex.activities.BaseActivity
import com.persona5dex.extensions.toPersonaApplication
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator

class OnboardingActivity : BaseActivity() {

    private lateinit var viewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager = findViewById<ViewPager>(R.id.onboarding_view_pager)

        viewModel = OnboardingViewModelFactory(defaultSharedPreferences, toPersonaApplication()).let {
            ViewModelProvider(this, it).get(OnboardingViewModel::class.java)
        }

        viewModel.nextStepState.observe(this, Observer {
            when (it) {
                is OnboardingViewModel.OnboardingPagerState.NextStepPagerState -> {
                    viewPager.currentItem++
                }
                is OnboardingViewModel.OnboardingPagerState.OnboardingComplete -> {
                    finish()
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewPager.currentItem == 0) {
                    viewModel.setOnboardingComplete()
                } else {
                    viewPager.currentItem--
                }
            }
        })

        val adapter = OnboardingViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        val springDotsIndicator = findViewById<SpringDotsIndicator>(R.id.spring_dots_indicator)
        springDotsIndicator.setViewPager(viewPager)
    }

    class OnboardingViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment =
                when (position) {
                    0 -> OnboardingChooseGameFragment.newInstance()
                    1 -> OnboardingThemeChooserFragment.newInstance()
                    else -> error("cannot get fragment for position $position")
                }

        override fun getCount(): Int = 2
    }
}