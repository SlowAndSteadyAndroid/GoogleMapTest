package com.example.googlemaptest.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.googlemaptest.base.BaseViewModel
import com.example.googlemaptest.data.repo.GoogleMapRepository
import com.example.googlemaptest.room.allergie.AllergieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val googleMapRepository: GoogleMapRepository) :
    BaseViewModel() {

    val inputAllergyLiveData = MutableLiveData("")

    val allergyList = googleMapRepository.allergyList

    fun addAllergie() {
        if(!inputAllergyLiveData.value.isNullOrEmpty()){
            viewModelScope.launch {
                googleMapRepository.addAllergie(inputAllergyLiveData.value!!)
            }
        }
    }

    fun deleteAllergie(allergieEntity: AllergieEntity) {
        viewModelScope.launch {
            googleMapRepository.deleteAllergie(allergieEntity)
        }
    }
}