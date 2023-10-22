package com.example.googlemaptest.search

import com.example.googlemaptest.base.ViewState
import com.google.android.gms.maps.model.MarkerOptions

sealed class SearchViewState : ViewState {

    data class GetSearchResult(val list : List<MarkerOptions>) : SearchViewState()
    data class ShowToast(val message: String) : SearchViewState()

}