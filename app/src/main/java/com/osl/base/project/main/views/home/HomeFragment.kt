package com.osl.base.project.main.views.home

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentHomeBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.dialog.TestViewModel
import com.osl.base.project.osl.utils.TEST_DIALOG_KEY
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber.Forest.d

/** home fragment*/
class HomeFragment : OslFragment<FragmentHomeBinding, HomeViewModel>() {
    override val layoutRes = R.layout.fragment_home
    override val destinationId = R.id.homeFragment
    override val viewModelClass = HomeViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        // viewModel.init()

        ui.viewDataBinding.alarm.setOnClickListener {
            HomeFragmentDirections.actionHomeFragmentToAlarmFragment().navigate()
        }
    }

    override fun addObservers() {
        viewModel.showTerraView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToTerraFragment().navigate()
        }

        /** dialog test click*/
        viewModel.onTestClick.observeEvent {
            d("event")
            HomeFragmentDirections.actionHomeFragmentToTestDialog().navigate()
        }

        /** dialog call back*/
        navResultObserve<TestViewModel.React?>(TEST_DIALOG_KEY) {
            d("Dialog return")
        }

        viewModel.showChartView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToChartFragment().navigate()
        }

        viewModel.showCalendarView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToCalendarFragment().navigate()
        }

        viewModel.showRulerView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToRulerFragment().navigate()
        }

        viewModel.showFoodLensView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToFoodLensFragment().navigate()
        }

        viewModel.showCameraView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToCameraFragment().navigate()
        }

        viewModel.showLoginView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToLoginFragment().navigate()
        }

        viewModel.showHistoryTakingView.observeEvent {
            HomeFragmentDirections.actionHomeFragmentToViewPagerFragment().navigate()
        }
    }
}