package com.example.googlemaptest.bookmark

import com.example.googlemaptest.base.ViewState

sealed class BookmarkViewState : ViewState{
    object DeleteBookmark : BookmarkViewState()
}
