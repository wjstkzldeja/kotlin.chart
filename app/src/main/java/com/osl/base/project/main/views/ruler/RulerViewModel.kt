package com.osl.base.project.main.views.ruler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.main.utils.ruler.defaultWeightRange
import com.osl.base.project.osl.views.OslViewModel

class RulerViewModel(
) : OslViewModel() {

    private val _ruleItems = MutableLiveData(defaultWeightRange.toList())
    val ruleItems: LiveData<List<Int>> get() = _ruleItems

    private val _weightValue = MutableLiveData("0.0")
    val weightValue: LiveData<String> get() = _weightValue

    fun updateWeightValue(weightValuePos: Int) {
        val weightValue = _ruleItems.value?.get(weightValuePos) ?: 0
        val fWeight = weightValue / 10f
        updateWeight(fWeight)
    }


    private fun updateWeight(fWeight: Float) {
        _weightValue.value = String.format("%.1f", fWeight)
//        cat?.cat?.catWeight = fWeight
//        _isWeightSelected.value = fWeight > 0f
    }
}