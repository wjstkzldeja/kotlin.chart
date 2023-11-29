package com.osl.base.project.main.views.history_taking.begin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel

class BeginViewModel : OslViewModel() {

    private val _onBeginClick = MutableLiveData<Event<Unit>>()
    val onBeginClick: LiveData<Event<Unit>> get() = _onBeginClick

    fun onBeginClick() {
        _onBeginClick.value = Event(Unit)
    }
}