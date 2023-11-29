package com.osl.base.project.main.views.history_taking.connect_sensor

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.tryterra.terra.Terra
import co.tryterra.terra.enums.Connections
import com.osl.base.project.main.BuildConfig.TERRA_API_KEY
import com.osl.base.project.main.BuildConfig.TERRA_DEV_ID
import com.osl.base.project.main.views.home.HomeFragmentDirections
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.utils.START_INTENT
import com.osl.base.project.osl.views.OslViewModel
import com.osl.swrnd.domain.usecase.GenerateAuthTokenUseCase
import timber.log.Timber.Forest.d

class ConnectSensorViewModel(
    private val generateAuthTokenUseCase: GenerateAuthTokenUseCase
) : OslViewModel() {

    private val _onSensorClick = MutableLiveData<Event<Unit>>()
    val onSensorClick: LiveData<Event<Unit>> get() = _onSensorClick

    private val _tokenValue = MutableLiveData<String>()
    val tokenValue: LiveData<String> get() = _tokenValue

    private val _glucoseValue = MutableLiveData<Double?>()
    val glucoseValue: LiveData<Double?> get() = _glucoseValue

    fun onSensorClick() {
        _onSensorClick.value = Event(Unit)
    }

    fun generateAuthToken() {
        with(generateAuthTokenUseCase) {
            vo(TERRA_DEV_ID, TERRA_API_KEY)?.request(
                success = { _tokenValue.value = it.token },
                error = { d("error: ${it.message}") },
                preExecutor = ::onLoading,
                postExecutor = ::onFinishLoading
            )?.addTo(disposable)
        }
    }

    fun initConnection(view: View) {
        Terra.instance(TERRA_DEV_ID, null, view.context) { manager, error ->
            if (error != null) d("instance error: ${error.message}")
            else manager.initConnection(
                connection = Connections.FREESTYLE_LIBRE,
                token = _tokenValue.value!!,
                context = view.context,
                startIntent = START_INTENT,
            ) { isConnected, err ->
                if (!isConnected) d("connection error: ${err?.message}")
            }
        }
    }

    fun readGlucoseData(view: View) {
        Terra.instance(TERRA_DEV_ID, null, view.context) { manager, error ->
            if (error != null) d("instance error: ${error.message}")
            else manager.readGlucoseData { response ->
                _glucoseValue.postValue(response?.data?.day_avg_blood_glucose_mg_per_dL)
            }
        }
    }
}