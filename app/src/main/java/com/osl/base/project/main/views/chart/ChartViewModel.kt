package com.osl.base.project.main.views.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel

class ChartViewModel : OslViewModel() {

    private val _onBarChart = MutableLiveData<Event<Unit>>()
    val onBarChart: LiveData<Event<Unit>> get() = _onBarChart

    private val _onBarAttrsChart = MutableLiveData<Event<Unit>>()
    val onBarAttrsChart: LiveData<Event<Unit>> get() = _onBarAttrsChart

    private val _onPointChart = MutableLiveData<Event<Unit>>()
    val onPointChart: LiveData<Event<Unit>> get() = _onPointChart

    private val _onLineChart = MutableLiveData<Event<Unit>>()
    val onLineChart: LiveData<Event<Unit>> get() = _onLineChart

    private val _onLineNotScrollChart = MutableLiveData<Event<Unit>>()
    val onLineNotScrollChart: LiveData<Event<Unit>> get() = _onLineNotScrollChart

    fun onBarChart() {
        _onBarChart.value = Event(Unit)
    }

    fun onBarAttrsChart() {
        _onBarAttrsChart.value = Event(Unit)
    }

    fun onPointChart() {
        _onPointChart.value = Event(Unit)
    }

    fun onLineChart() {
        _onLineChart.value = Event(Unit)
    }

    fun onLineNotScrollChart() {
        _onLineNotScrollChart.value = Event(Unit)
    }
}