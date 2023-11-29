package com.osl.base.project.main.views.chart.point

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentChartPointBinding
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

/** home fragment*/
class ChartPointFragment : OslFragment<FragmentChartPointBinding, ChartPointViewModel>() {
    override val layoutRes = R.layout.fragment_chart_point
    override val destinationId = R.id.chartPointFragment
    override val viewModelClass = ChartPointViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java
    private fun getActViewModel() = actViewModel as ActMainViewModel

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel

        ui.viewDataBinding.textTest.setOnClickListener {
        }

        /**포인트 차트*/
        viewModel.initCreatePointChartData()
        initChartPointAxis()
        initChartPointBase()
        initChartPointLine()
    }

    override fun addObservers() {
    }

    /**@@@@@@@@@@@@@@@@@@@@@@@@@포인트차트*/
    /** Axis 라인 차트 초기화*/
    private fun initChartPointAxis() {
        val chartView = ui.viewDataBinding.chartPointAxis
        //x
        chartView.xAxisList = viewModel.xAxisList
        chartView.xAxisMaxCount = viewModel.xMaxCount//x축 그릴 갯수

        //y
        chartView.yAxisList = viewModel.yAxisList
        chartView.yAxisMaxCount = viewModel.yMaxCount//y축 그릴 갯수
    }

    /** base 라인 차트 초기화*/
    private fun initChartPointBase() {
        val chartView = ui.viewDataBinding.chartPointBase
        //x
        chartView.xMaxCount = viewModel.xMaxCount

        //y
        chartView.yValueList = viewModel.yAxisList
        chartView.yMaxCount = viewModel.yMaxCount //y축 그릴 갯수
    }

    /** Line 라인 차트 초기화*/
    private fun initChartPointLine() {
        val chartView = ui.viewDataBinding.chartPoint
        //x
        chartView.xValueList = viewModel.xValueList
        chartView.xMinValue = 0 //현재 사용하지 않음, 나중에 시간값으로 최소값 변경 가능성 있음
        chartView.xMaxValue = 100//현재 사용하지 않음, 나중에 시간값으로 최소값 변경 가능성 있음
        chartView.xMaxCount = viewModel.xMaxCount

        //y
        chartView.yValueList = ChartEngineUtils().createPointChartYValueList()
        chartView.yMinValue = viewModel.yMinValue // y최소값
        chartView.yMaxValue = viewModel.yMaxValue //y축 최대값
        chartView.yMaxCount = viewModel.yMaxCount //y축 그릴 갯수
    }

}