package com.osl.base.project.main.views.terra.samsung

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel
import kotlinx.coroutines.launch
import timber.log.Timber.Forest.d
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period

class SamsungViewModel : OslViewModel() {

    private val _onStepsClick = MutableLiveData<Event<Unit>>()
    val onStepsClick: LiveData<Event<Unit>> get() = _onStepsClick

    private val _onWeightClick = MutableLiveData<Event<Unit>>()
    val onWeightClick: LiveData<Event<Unit>> get() = _onWeightClick

    private val _stepsValue = MutableLiveData(0L)
    val stepsValue: LiveData<Long> get() = _stepsValue

    private val _stepsTotalValue = MutableLiveData(0L)
    val stepsTotalValue: LiveData<Long> get() = _stepsTotalValue

    private val _weightValue = MutableLiveData(0.0)
    val weightValue: LiveData<Double> get() = _weightValue

    private val _weightAverageValue = MutableLiveData(0.0)
    val weightAverageValue: LiveData<Double> get() = _weightAverageValue

    fun onStepsClick() {
        _onStepsClick.value = Event(Unit)
    }

    fun onWeightClick() {
        _onWeightClick.value = Event(Unit)
    }

    fun readStepsByTimeRange(
        client: HealthConnectClient, startTime: Instant, endTime: Instant
    ) {
        viewModelScope.launch {
            try {
                val response = client.readRecords(
                    ReadRecordsRequest(
                        recordType = StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                _stepsValue.value = response.records[0].count
            } catch (e: Exception) {
                d("readStepsByTimeRange: ${e.message}")
            }
        }
    }

    fun readStepsIntoMonths(
        client: HealthConnectClient, startTime: LocalDateTime, endTime: LocalDateTime
    ) {
        viewModelScope.launch {
            try {
                val response = client.aggregateGroupByPeriod(
                    AggregateGroupByPeriodRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
                        timeRangeSlicer = Period.ofMonths(1)
                    )
                )
                _stepsTotalValue.value = response[0].result[StepsRecord.COUNT_TOTAL]
            } catch (e: Exception) {
                d("readStepsIntoMonths: ${e.message}")
            }
        }
    }

    fun readWeightByTimeRange(
        client: HealthConnectClient, startTime: Instant, endTime: Instant
    ) {
        viewModelScope.launch {
            try {
                val response = client.readRecords(
                    ReadRecordsRequest(
                        recordType = WeightRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                val latestValue = response.records.size - 1
                _weightValue.value = response.records[latestValue].weight.inKilograms
            } catch (e: Exception) {
                d("readWeightByTimeRange: ${e.message}")
            }
        }
    }

    fun readWeightAverage(
        client: HealthConnectClient, startTime: LocalDateTime, endTime: LocalDateTime
    ) {
        viewModelScope.launch {
            try {
                val response = client.aggregate(
                    AggregateRequest(
                        metrics = setOf(WeightRecord.WEIGHT_AVG),
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                _weightAverageValue.value = response[WeightRecord.WEIGHT_AVG]?.inKilograms
            } catch (e: Exception) {
                d("readWeightAverage: ${e.message}")
            }
        }
    }
}