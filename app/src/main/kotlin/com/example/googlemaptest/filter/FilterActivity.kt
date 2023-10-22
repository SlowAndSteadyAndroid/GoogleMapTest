package com.example.googlemaptest.filter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.googlemaptest.R
import com.example.googlemaptest.adapter.FilterAdapter
import com.example.googlemaptest.base.BaseActivity
import com.example.googlemaptest.databinding.ActivityFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : BaseActivity<ActivityFilterBinding>(R.layout.activity_filter) {

    private val viewModel by viewModels<FilterViewModel>()

    private val filterAdapter = FilterAdapter {
        viewModel?.deleteAllergie(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        binding.rvFilter.adapter = filterAdapter

        lifecycleScope.launchWhenResumed {
            viewModel.allergyList.collect {
                binding.rvFilter.isVisible = it.isNotEmpty()
                binding.tvFilter.isVisible = it.isEmpty()
                filterAdapter.submitList(it)
            }
        }
    }
}