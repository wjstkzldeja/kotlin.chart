package com.osl.base.project.main.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel

class TermsOfServiceBottomSheetViewModel : OslViewModel() {
    private val _onSubmitClick = MutableLiveData<Event<Unit>>()
    val onSubmitClick: LiveData<Event<Unit>> get() = _onSubmitClick
    fun onSubmitClick(){
        _onSubmitClick.value = Event(Unit)
    }
}