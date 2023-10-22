package com.example.googlemaptest.home

import com.example.googlemaptest.base.ViewState
import com.example.googlemaptest.room.bookmark.BookmarkEntity

sealed class HomeViewState : ViewState {
    data class Route(val entity: BookmarkEntity) : HomeViewState()
}
