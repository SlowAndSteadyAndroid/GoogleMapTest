package com.example.googlemaptest.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.googlemaptest.R
import com.example.googlemaptest.adapter.FragmentPagerAdapter
import com.example.googlemaptest.search.SearchFragment
import com.example.googlemaptest.base.BaseActivity
import com.example.googlemaptest.bookmark.BookmarkFragment
import com.example.googlemaptest.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel by viewModels<HomeViewModel>()

    private var backWait: Long = INIT_TIME

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = resources.getStringArray(R.array.array_home)[position]
            tab.icon = resources.obtainTypedArray(R.array.array_home_icon).getDrawable(position)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.viewStateLiveData.observe(this) { viewState ->
            (viewState as? HomeViewState)?.let { onChangedHomeViewState(viewState) }
        }
    }

    private fun onChangedHomeViewState(viewState: HomeViewState) {
        when(viewState){
            is HomeViewState.Route -> {
                binding.viewpager.currentItem = 0
            }
        }
    }

    private fun initUi() {

        val list = listOf(
            SearchFragment(),
            BookmarkFragment(),
        )
        val pagerAdapter = FragmentPagerAdapter(list, this)

        with(binding) {
            viewpager.adapter = pagerAdapter
            viewpager.offscreenPageLimit = 3
            viewpager.isUserInputEnabled = false
            TabLayoutMediator(tab, viewpager, tabConfigurationStrategy).attach()
        }

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backWait >= LIMIT_TIME) {
            backWait = System.currentTimeMillis()
            Toast.makeText(this, "Press the Back button again to exit.", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val INIT_TIME = 0L
        private const val LIMIT_TIME = 2000
    }

}