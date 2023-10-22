package com.example.googlemaptest.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.googlemaptest.R
import com.example.googlemaptest.databinding.FragmentSearchBinding
import com.example.googlemaptest.ext.showToast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var googleMap: GoogleMap

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )
        binding.mapview.onCreate(savedInstanceState)
        binding.mapview.getMapAsync(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapview.onStop()
    }


    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapview.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapview.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapview.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? SearchViewState)?.let {
                onChangedSearchViewState(
                    viewState
                )
            }
        }

        binding.ivSearch.setOnClickListener {
            viewModel.search(
                binding.etSearch.text.toString(),
                googleMap.projection.visibleRegion.latLngBounds.center
            )
        }
    }

    private fun onChangedSearchViewState(viewState: SearchViewState) {
        when (viewState) {
            is SearchViewState.ShowToast -> {
                showToast(message = viewState.message)
            }

            is SearchViewState.GetSearchResult -> {
                googleMap.clear()
                viewState.list.forEach {
                    googleMap.addMarker(it)
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        with(map) {
            googleMap = this
            isMyLocationEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isMapToolbarEnabled = true
            initMapSetting()
        }
    }

    private fun initMapSetting() {
        googleMap.setOnMarkerClickListener {
            viewModel.getPlaceDetails(it.position)
            false
        }
    }

}