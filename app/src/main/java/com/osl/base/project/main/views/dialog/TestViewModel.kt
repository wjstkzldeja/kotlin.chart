package com.osl.base.project.main.views.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel
import java.io.Serializable

class TestViewModel : OslViewModel() {

  private val _onConfirmEvent = MutableLiveData<Event<React>>()
  val onConfirmEvent: LiveData<Event<React>> get() = _onConfirmEvent

  private val _onBackEvent = MutableLiveData<Event<React>>()
  val onBackEvent: LiveData<Event<React>> get() = _onBackEvent

  fun onConfirm() {
    _onConfirmEvent.value = Event(React.Negative)
  }

  fun onBack() {
    _onBackEvent.value = Event(React.Cancel)
  }

  sealed class React : Serializable {
    object Negative : React()
    object Cancel : React()
  }
}
