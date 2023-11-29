package com.osl.base.project.main.views.chart

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentChartBkBinding
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

/** home fragment*/
class ChartBkFragment : OslFragment<FragmentChartBkBinding, ChartBkViewModel>() {
    override val layoutRes = R.layout.fragment_chart_bk
    override val destinationId = R.id.chartFragment
    override val viewModelClass = ChartBkViewModel::class.java
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

        /**막대 차트*/
        viewModel.initCreateBarChartData()
        initChartBarAxis()
        initChartBarBase()
        initChartBar()

        /**포인트 차트*/
        viewModel.initCreatePointChartData()
        initChartPointAxis()
        initChartPointBase()
        initChartPointLine()

//        val test = ChartDateUtils().isWeekFriday()
//        val test2 = ChartDateUtils().isWeekFridayTest()
//        d("isWeekFriday : ${test}")
//        d("isWeekFriday 2 : ${ChartDateUtils().getTimestamp(test2)}")
    }

    override fun addObservers() {
        viewModel.axisScrollEvent.observeEvent { scrollValue ->
            axisScrollEvent(scrollValue)
        }
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
//        chartView.viewModel = viewModel
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