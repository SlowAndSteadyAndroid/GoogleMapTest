package com.example.googlemaptest.bookmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.googlemaptest.R
import com.example.googlemaptest.adapter.BookmarkAdapter
import com.example.googlemaptest.base.BaseFragment
import com.example.googlemaptest.databinding.FragmentBookmarkBinding
import com.example.googlemaptest.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {

    private val parentViewModel by activityViewModels<HomeViewModel>()

    private val viewModel by viewModels<BookmarkViewModel>()

    private val bookmarkAdapter = BookmarkAdapter(
        onDelete = {
            viewModel?.deleteBookmark(it)
        },
        onRoute = {
            parentViewModel?.onRoute(it)
        }
    )

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvBookmark.adapter = bookmarkAdapter

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? BookmarkViewState)?.let {
                onChangedBookmarkViewState(it)
            }
        }

        viewLifecycleOwner.launchWhenResumed {
            viewModel.bookmarkList.collect {
                bookmarkAdapter.submitList(it)
            }
        }
    }

    private fun onChangedBookmarkViewState(viewState: BookmarkViewState) {
        when (viewState) {
            BookmarkViewState.DeleteBookmark -> {
//                lifecycleScope.launch {
//                    bookmarkAdapter.submitList(viewModel.bookmarkList.first())
//                }
            }
        }
    }
}

fun LifecycleOwner.launchWhenResumed(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}
