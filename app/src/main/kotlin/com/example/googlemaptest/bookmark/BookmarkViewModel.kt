package com.example.googlemaptest.bookmark

import androidx.lifecycle.viewModelScope
import com.example.googlemaptest.base.BaseViewModel
import com.example.googlemaptest.data.repo.GoogleMapRepository
import com.example.googlemaptest.room.bookmark.BookmarkEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val googleMapRepository: GoogleMapRepository) :
    BaseViewModel() {

    val bookmarkList = googleMapRepository.bookmarkList

    fun deleteBookmark(entity: BookmarkEntity) {
        viewModelScope.launch {
            googleMapRepository.deleteBookmark(entity)
            viewStateChanged(BookmarkViewState.DeleteBookmark)
        }
    }
}