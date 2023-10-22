package com.example.googlemaptest.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.googlemaptest.base.BaseViewModel
import com.example.googlemaptest.data.model.place.Result
import com.example.googlemaptest.data.model.toMarkerOption
import com.example.googlemaptest.data.repo.GoogleMapRepository
import com.example.googlemaptest.room.bookmark.BookmarkEntity
import com.example.googlemaptest.room.bookmark.toBookmarkEntity
import com.example.googlemaptest.room.bookmark.toLatLng
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val googleMapRepository: GoogleMapRepository,
) : BaseViewModel() {

    private val cacheSearchList = mutableMapOf<LatLng, String>()

    val inputSearchLiveData = MutableLiveData("")

    private val bookmarkList = googleMapRepository.bookmarkList

    fun search(latLng: LatLng) {
        viewModelScope.launch {
            if (!inputSearchLiveData.value.isNullOrEmpty()) {
                googleMapRepository.nearbySearch(
                    inputSearchLiveData.value!!,
                    latLng.toLocationString()
                ).body()
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
                        viewStateChanged(
                            SearchViewState.GetDetail(
                                result = response.result,
                                isAllergy = inputSearchLiveData.value?.let { input ->
                                    googleMapRepository.allergyList.first()
                                        .any { it.name.contains(input) }
                                } ?: false,
                                isBookmark = bookmarkList.first()
                                    .any { it.placeId == response.result.place_id }
                            )
                        )
                    } else {
                        viewStateChanged(SearchViewState.ShowToast("No results found."))
                    }
                } ?: viewStateChanged(SearchViewState.ShowToast("No results found."))
            } ?: viewStateChanged(SearchViewState.ShowToast("No results found."))
        }
    }

    fun toggleBookmark(result: Result, isBookmark: Boolean) {
        if (isBookmark) {
            addBookmark(result)
        } else {
            deleteBookmark(result)
        }
    }

    private fun addBookmark(result: Result) {
        viewModelScope.launch(IO) {
            googleMapRepository.addBookmark(result.toBookmarkEntity())
            viewStateChanged(SearchViewState.ShowToast("Add Bookmark!"))
        }
    }

    private fun deleteBookmark(result: Result) {
        viewModelScope.launch(IO) {
            googleMapRepository.deleteBookmark(result.toBookmarkEntity())
            viewStateChanged(SearchViewState.ShowToast("Delete Bookmark!"))
        }
    }

    fun addDetail(entity: BookmarkEntity) {
        cacheSearchList[entity.toLatLng()] = entity.placeId
    }
}

fun LatLng.toLocationString(): String {
    return "$latitude,$longitude"
}