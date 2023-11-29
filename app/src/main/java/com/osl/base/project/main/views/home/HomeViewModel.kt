package com.osl.base.project.main.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel
import com.osl.swrnd.domain.usecase.Test2UseCase
import com.osl.swrnd.domain.usecase.TestUseCase
import com.osl.swrnd.entity.local.req.TestReq
import timber.log.Timber.Forest.d
import timber.log.Timber.Forest.w

class HomeViewModel(
    private val testUseCase: TestUseCase, private val test2UseCase: Test2UseCase
) : OslViewModel() {

    private val _onTestClick = MutableLiveData<Event<Unit>>()
    val onTestClick: LiveData<Event<Unit>> get() = _onTestClick

    private val _showChartView = MutableLiveData<Event<Unit>>()
    val showChartView: LiveData<Event<Unit>> get() = _showChartView

    private val _showCalendarView = MutableLiveData<Event<Unit>>()
    val showCalendarView: LiveData<Event<Unit>> get() = _showCalendarView

    private val _showRulerView = MutableLiveData<Event<Unit>>()
    val showRulerView: LiveData<Event<Unit>> get() = _showRulerView

    private val _showTerraView = MutableLiveData<Event<Unit>>()
    val showTerraView: LiveData<Event<Unit>> get() = _showTerraView

    private val _showFoodLensView = MutableLiveData<Event<Unit>>()
    val showFoodLensView: LiveData<Event<Unit>> get() = _showFoodLensView

    private val _showCameraView = MutableLiveData<Event<Unit>>()
    val showCameraView: LiveData<Event<Unit>> get() = _showCameraView
    private val _showLoginView = MutableLiveData<Event<Unit>>()
    val showLoginView: LiveData<Event<Unit>> get() = _showLoginView

    private val _showHistoryTakingView = MutableLiveData<Event<Unit>>()
    val showHistoryTakingView: LiveData<Event<Unit>> get() = _showHistoryTakingView

    fun onChartClick() {
        _showChartView.value = Event(Unit)
    }

    fun onCalendarClick() {
        _showCalendarView.value = Event(Unit)
    }

    fun onRulerClick() {
        _showRulerView.value = Event(Unit)
    }


    fun onTestClick() {
        _onTestClick.value = Event(Unit)
    }

    fun onTerraClick() {
        _showTerraView.value = Event(Unit)
    }

    fun onFoodLensClick() {
        _showFoodLensView.value = Event(Unit)
    }

    fun onCameraClick() {
        _showCameraView.value = Event(Unit)
    }

    fun onLoginClick() {
        _showLoginView.value = Event(Unit)
    }

    fun onHistoryTakingClick() {
        _showHistoryTakingView.value = Event(Unit)
    }

    /** api test*/
    fun init() {
        with(testUseCase) {
            vo("")?.request(
                success = {},
                error = {},
            )?.addTo(disposable)
        }
        with(test2UseCase) {
            TestReq("title").request(
                success = { d("success ${it}") },
                error = { w("error ${it}") },
                preExecutor = ::onLoading,
                postExecutor = ::onFinishLoading
            )?.addTo(disposable)
        }
    }
}