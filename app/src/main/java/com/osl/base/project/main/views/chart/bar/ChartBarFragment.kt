package com.osl.base.project.main.views.chart.bar

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentChartBarBinding
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

/** home fragment*/
class ChartBarFragment : OslFragment<FragmentChartBarBinding, ChartBarViewModel>() {
    override val layoutRes = R.layout.fragment_chart_bar
    override val destinationId = R.id.chartBarFragment
    override val viewModelClass = ChartBarViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java
    private fun getActViewModel() = actViewModel as ActMainViewModel

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel

        /**막대 차트*/
        viewModel.initCreateBarChartData()
        initChartBarAxis()
        initChartBarBase()
        initChartBar()
    }

    override fun addObservers() {
    }

    /**@@@@@@@@@@@@@@@@@@@@@@@@@막대 차트*/
    /** Axis 라인 차트 초기화*/
    private fun initChartBarAxis() {
        val chartView = ui.viewDataBinding.chartBarAxis
        //x
        chartView.xAxisList = viewModel.xAxisList
        chartView.xAxisMaxCount = viewModel.xMaxCount//x축 그릴 갯수

        //y
        chartView.yAxisList = viewModel.yAxisList
        chartView.yAxisMaxCount = viewModel.yMaxCount//y축 그릴 갯수
    }

    /** base 라인 차트 초기화*/
    private fun initChartBarBase() {
        val chartView = ui.viewDataBinding.chartBarBase
        //x
        chartView.xMaxCount = viewModel.xMaxCount

        //y
        chartView.yValueList = viewModel.yAxisList
        chartView.yMaxCount = viewModel.yMaxCount //y축 그릴 갯수
    }

    /** Line 라인 차트 초기화*/
    private fun initChartBar() {
        val chartView = ui.viewDataBinding.chartBar
        //x
        chartView.xValueList = viewModel.xValueList
        chartView.xMaxCount = viewModel.xMaxCount

        //y
        chartView.yValueList = viewModel.yValueList
        chartView.yMinValue = viewModel.yMinValue // y최소값
        chartView.yMaxValue = viewModel.yMaxValue //y축 최대값
        chartView.yMaxCount = viewModel.yMaxCount //y축 그릴 갯수
    }
}