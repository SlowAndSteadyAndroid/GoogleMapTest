package com.example.googlemaptest.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.googlemaptest.ext.uiScope


abstract class BaseViewModel : ViewModel() {

    private val _viewStateLiveData = MutableLiveData<ViewState>()
    val viewStateLiveData: LiveData<ViewState> = _viewStateLiveData

    protected fun viewStateChanged(viewState: ViewState) {
        uiScope {
            _viewStateLiveData.value = viewState
            _viewStateLiveData.value = null
        }
    }

}

interface ViewState