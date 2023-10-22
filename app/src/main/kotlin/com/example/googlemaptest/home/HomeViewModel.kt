package com.example.googlemaptest.home

import com.example.googlemaptest.base.BaseViewModel
import com.example.googlemaptest.room.bookmark.BookmarkEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel(){

    fun onRoute(entity: BookmarkEntity) {
        viewStateChanged(HomeViewState.Route(entity))
    }
}