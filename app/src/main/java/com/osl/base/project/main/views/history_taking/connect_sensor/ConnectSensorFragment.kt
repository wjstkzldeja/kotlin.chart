package com.osl.base.project.main.views.history_taking.connect_sensor

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.kofigyan.stateprogressbar.StateProgressBar
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentConnectSensorBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class ConnectSensorFragment : OslFragment<FragmentConnectSensorBinding, ConnectSensorViewModel>() {
    override val layoutRes = R.layout.fragment_connect_sensor
    override val destinationId = R.id.connectSensorFragment
    override val viewModelClass = ConnectSensorViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        setProgressStatus()
        initConnect()
    }

    override fun addObservers() {
        viewModel.onSensorClick.observeEvent {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            viewPager.currentItem = 1
        }
    }

    private fun setProgressStatus() {
        val layoutProgress = ui.viewDataBinding.layoutProgress
        layoutProgress.run {
            txtTitle.text = resources.getString(R.string.connect_sensor_title)
            progressState.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
            progressLinear.setProgressCompat(50, true)
        }
    }

    private fun initConnect() {
        viewModel.run {
            generateAuthToken()
            tokenValue.observe(viewLifecycleOwner) { initConnection(requireView()) }
        }
    }

    private fun readGlucoseData() {
        viewModel.run {
            readGlucoseData(requireView())
            glucoseValue.observe(viewLifecycleOwner) {}
        }
    }
}