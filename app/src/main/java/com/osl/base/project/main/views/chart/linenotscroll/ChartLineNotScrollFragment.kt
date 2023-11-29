package com.osl.base.project.main.views.chart.linenotscroll

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentChartBinding
import com.osl.base.project.main.databinding.FragmentChartLineNotScrollBinding
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

/** home fragment*/
class ChartLineNotScrollFragment : OslFragment<FragmentChartLineNotScrollBinding, ChartLineNotScrollViewModel>() {
    override val layoutRes = R.layout.fragment_chart_line_not_scroll
    override val destinationId = R.id.chartLineNotScrollFragment
    override val viewModelClass = ChartLineNotScrollViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java
    private fun getActViewModel() = actViewModel as ActMainViewModel

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel

        ui.viewDataBinding.textTest.setOnClickListener {
            /**라인 차트 리프레쉬 테스트*/
/*            val chartView = ui.viewDataBinding.chartLine
            viewModel.initCreateLineChartData()
            initChartLine()
            chartView.preCalculated()
            chartView.postInvalidateOnAnimation()*/
        }
        /**라인 차트*/
        viewModel.initCreateLineChartData()
        initChartLineAxis()
        initChartLineBase()
        initChartLine()

//        val test = ChartDateUtils().isWeekFriday()
//        val test2 = ChartDateUtils().isWeekFridayTest()
//        d("isWeekFriday : ${test}")
//        d("isWeekFriday 2 : ${ChartDateUtils().getTimestamp(test2)}")
    }

    override fun addObservers() {
    }
    /**@@@@@@@@@@@@@@@@@@@@@@@@@라인차트*/
    /** Axis 라인 차트 초기화*/
    private fun initChartLineAxis() {
        val chartView = ui.viewDataBinding.chartLineAxis
        //x
        chartView.xAxisList = viewModel.xAxisList
        chartView.xAxisMaxCount = viewModel.xMaxCount//x축 그릴 갯수

        //y
        chartView.yAxisList = viewModel.yAxisList
        chartView.yAxisMaxCount = viewModel.yMaxCount//y축 그릴 갯수
    }

    /** base 라인 차트 초기화*/
    private fun initChartLineBase() {
        val chartView = ui.viewDataBinding.chartLineBase
        //x
        chartView.xMaxCount = viewModel.xMaxCount

        //y
        chartView.yValueList = viewModel.yAxisList
        chartView.yMaxCount = viewModel.yMaxCount //y축 그릴 갯수
    }

    /** Line 라인 차트 초기화*/
    private fun initChartLine() {
        val chartView = ui.viewDataBinding.chartLine

        //x
        chartView.xValueList = viewModel.xValueList
        chartView.xMinValue = 0 //현재 사용하지 않음, 나중에 시간값으로 최소값 변경 가능성 있음
        chartView.xMaxValue = 100//현재 사용하지 않음, 나중에 시간값으로 최소값 변경 가능성 있음
        chartView.xMaxCount = viewModel.xMaxCount

        //y
        chartView.yValueList = viewModel.yValueList
        chartView.yMinValue = viewModel.yMinValue // y최소값
        chartView.yMaxValue = viewModel.yMaxValue //y축 최대값
        chartView.yMaxCount = viewModel.yMaxCount //y축 그릴 갯수
    }

    /** line move Axis차트로 이벤트 전송*/
    fun axisScrollEvent(scrollValue: Float) {
        ui.viewDataBinding.chartLineAxis.axisScrollEvent(scrollValue)
    }
}