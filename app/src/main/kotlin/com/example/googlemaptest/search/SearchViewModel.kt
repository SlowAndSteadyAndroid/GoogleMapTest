package com.example.googlemaptest.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.googlemaptest.base.BaseViewModel
import com.example.googlemaptest.data.model.toMarkerOption
import com.example.googlemaptest.data.repo.GoogleMapRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val googleMapRepository: GoogleMapRepository
) : BaseViewModel() {

    private val cacheSearchList = mutableMapOf<LatLng, String>()

    fun search(keyword: String, latLng: LatLng) {
        viewModelScope.launch {
            if (keyword.isNotEmpty()) {
                googleMapRepository.nearbySearch(keyword, latLng.toLocationString()).body()
                    ?.let { response ->
                        if (response.status == "OK") {

                            cacheSearchList.putAll(response.results.map {
                                it.toMarkerOption().position to it.place_id
                            })

                            viewStateChanged(
                                SearchViewState.GetSearchResult(
                                    response.results.map { it.toMarkerOption() }
                                )
                            )

                        } else {
                            viewStateChanged(SearchViewState.ShowToast("No results found."))
                        }
                    } ?: viewStateChanged(SearchViewState.ShowToast("No results found."))
            } else {
                viewStateChanged(SearchViewState.ShowToast("Please enter a search term."))
            }
        }
    }

    fun getPlaceDetails(latLng: LatLng) {
        viewModelScope.launch {
            cacheSearchList[latLng]?.let { placeId ->
                googleMapRepository.getPlaceDetail(placeId).body()?.let { response ->
                    if (response.status == "OK") {
                        Log.d("결과", response.result.toString())
                    } else {
                        viewStateChanged(SearchViewState.ShowToast("No results found."))
                    }
                } ?: viewStateChanged(SearchViewState.ShowToast("No results found."))
            } ?: viewStateChanged(SearchViewState.ShowToast("No results found."))
        }
    }
}

fun LatLng.toLocationString(): String {
    return "$latitude,$longitude"
}