package com.osl.base.project.main.views.chart

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentChartBinding
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.home.HomeFragmentDirections
import com.osl.base.project.osl.views.OslFragment

/** home fragment*/
class ChartFragment : OslFragment<FragmentChartBinding, ChartViewModel>() {
    override val layoutRes = R.layout.fragment_chart
    override val destinationId = R.id.chartFragment
    override val viewModelClass = ChartViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java
    private fun getActViewModel() = actViewModel as ActMainViewModel

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {
        viewModel.onBarChart.observeEvent {
            ChartFragmentDirections.actionChartFragmentToChartBarFragment().navigate()
        }
        viewModel.onBarAttrsChart.observeEvent {
            ChartFragmentDirections.actionChartFragmentToChartBarAttrsFragment().navigate()
        }

        viewModel.onPointChart.observeEvent {
            ChartFragmentDirections.actionChartFragmentToChartPointFragment().navigate()
        }

        viewModel.onLineChart.observeEvent {
            ChartFragmentDirections.actionChartFragmentToChartLineFragment().navigate()
        }

        viewModel.onLineNotScrollChart.observeEvent {
            ChartFragmentDirections.actionChartFragmentToChartLineNotScrollFragment().navigate()
        }

    }

}