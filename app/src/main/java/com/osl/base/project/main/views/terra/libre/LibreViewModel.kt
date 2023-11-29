package com.osl.base.project.main.views.terra.libre

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.tryterra.terra.Terra
import com.osl.base.project.main.BuildConfig.TERRA_API_KEY
import com.osl.base.project.main.BuildConfig.TERRA_DEV_ID
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel
import com.osl.swrnd.domain.usecase.GenerateAuthTokenUseCase
import timber.log.Timber.Forest.d

class LibreViewModel(
    private val generateAuthTokenUseCase: GenerateAuthTokenUseCase
) : OslViewModel() {

    private val _onGlucoseClick = MutableLiveData<Event<Unit>>()
    val onGlucoseClick: LiveData<Event<Unit>> get() = _onGlucoseClick

    private val _tokenValue = MutableLiveData<String>()
    val tokenValue: LiveData<String> get() = _tokenValue

    private val _glucoseValue = MutableLiveData<Double?>()
    val glucoseValue: LiveData<Double?> get() = _glucoseValue

    fun onGlucoseClick() {
        _onGlucoseClick.value = Event(Unit)
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

    fun readGlucoseData(context: Context) {
        try {
            Terra.instance(TERRA_DEV_ID, null, context) { manager, _ ->
                manager.readGlucoseData { response ->
                    _glucoseValue.postValue(response?.data?.day_avg_blood_glucose_mg_per_dL)
                }
            }
        } catch (e: Exception) {
            d("readGlucoseData: ${e.message}")
        }
    }
}