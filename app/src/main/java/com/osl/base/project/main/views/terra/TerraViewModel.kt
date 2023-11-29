package com.osl.base.project.main.views.terra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel

class TerraViewModel : OslViewModel() {

    private val _onLibreClick = MutableLiveData<Event<Unit>>()
    val onLibreClick: LiveData<Event<Unit>> get() = _onLibreClick

    private val _onSamsungClick = MutableLiveData<Event<Unit>>()
    val onSamsungClick: LiveData<Event<Unit>> get() = _onSamsungClick

    fun onLibreClick() {
        _onLibreClick.value = Event(Unit)
    }

    fun onSamsungClick() {
        _onSamsungClick.value = Event(Unit)
    }
}