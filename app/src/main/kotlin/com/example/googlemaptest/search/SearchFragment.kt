package com.example.googlemaptest.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.googlemaptest.R
import com.example.googlemaptest.base.ViewState
import com.example.googlemaptest.databinding.FragmentSearchBinding
import com.example.googlemaptest.ext.hidePOIInfoContainer
import com.example.googlemaptest.ext.showPOIInfoContainer
import com.example.googlemaptest.ext.showToast
import com.example.googlemaptest.filter.FilterActivity
import com.example.googlemaptest.home.HomeViewModel
import com.example.googlemaptest.home.HomeViewState
import com.example.googlemaptest.room.bookmark.toLatLng
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var googleMap: GoogleMap

    private val parentViewModel by activityViewModels<HomeViewModel>()

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
        binding.viewModel = this@SearchFragment.viewModel
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
        binding.executePendingBindings()

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? SearchViewState)?.let {
                onChangedViewState(
                    viewState
                )
            }
        }
        parentViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? HomeViewState)?.let {
                onChangedViewState(
                    viewState
                )
            }
        }

        binding.ivSearch.setOnClickListener {
            viewModel.search(
                googleMap.projection.visibleRegion.latLngBounds.center
            )
        }

        binding.ivFilter.setOnClickListener {
            startActivity(Intent(requireContext(), FilterActivity::class.java))
        }

    }

    private fun onChangedViewState(viewState: ViewState) {
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

            is SearchViewState.GetDetail -> {
                binding.containerDetail.isVisible = true
                binding.place = viewState.result

                binding.ivBookmark.isChecked = viewState.isBookmark
                binding.ivAllergie.isVisible = viewState.isAllergy
                binding.executePendingBindings()

                binding.ivBookmark.setOnClickListener {
                    viewModel.toggleBookmark(viewState.result, binding.ivBookmark.isChecked)
                }
            }

            is HomeViewState.Route -> {
                val markerOptions = MarkerOptions().apply {
                    position(viewState.entity.toLatLng())
                    title(viewState.entity.name)
                }
                googleMap.addMarker(markerOptions)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        viewState.entity.toLatLng(),
                        14f
                    )
                )
                viewModel.addDetail(viewState.entity)

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
            binding.executePendingBindings()
        }
    }

    private fun initMapSetting() {
        googleMap.setOnMarkerClickListener {
            viewModel.getPlaceDetails(it.position)
            false
        }

        googleMap.setOnMapClickListener {
            binding.containerDetail.hidePOIInfoContainer(requireContext())
        }
    }

}