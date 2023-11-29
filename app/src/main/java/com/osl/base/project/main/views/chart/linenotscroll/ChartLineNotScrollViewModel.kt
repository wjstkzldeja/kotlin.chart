package com.osl.base.project.main.views.chart.linenotscroll

import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel

class ChartLineNotScrollViewModel : OslViewModel() {
    var xAxisList = arrayListOf<Long>()
    var yAxisList = arrayListOf<Int>()
    var xValueList = arrayListOf<Long>()
    var yValueList = arrayListOf<Int>()
    var xMaxCount = 0
    var yMaxCount = 0
    var yMinValue = 0
    var yMaxValue = 0
    var rangeStep = 50

    fun initCreateLineChartData() {
        /**x축 그릴 갯수
         * axis/base/chart 사용*/
        xMaxCount = 6
        /**y축 최소값
         * chart 사용*/
        yMinValue = 0
        /**y축 최대값
         * chart 사용*/
        yMaxValue = 300
        /**y축 값 간격
         * y리스트 만들때 사용*/
        rangeStep = 50

        /**Axis list
         * axis 사용*/
        xAxisList = ChartEngineUtils().createXAxisList()
        yAxisList = ChartEngineUtils().createYAxisList(yMinValue, yMaxValue, rangeStep)

        /**y축 그릴 갯수
         * axis/base/chart 사용*/
        yMaxCount = (yAxisList.size - 1)

        /**line chart list
         * chart 사용*/
        xValueList = ChartEngineUtils().createLineChartXValueList()
        yValueList = ChartEngineUtils().createLineChartYValueList(0)//0 + yMinValue 테스트용
    }
}