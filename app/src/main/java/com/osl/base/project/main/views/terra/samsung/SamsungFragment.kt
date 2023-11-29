package com.osl.base.project.main.views.terra.samsung

import android.os.Bundle
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentSamsungBinding
import com.osl.base.project.main.utils.terra.*
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.utils.ZONE_ID_SEOUL
import com.osl.base.project.osl.views.OslFragment
import kotlinx.coroutines.launch

class SamsungFragment : OslFragment<FragmentSamsungBinding, SamsungViewModel>() {
    override val layoutRes = R.layout.fragment_samsung
    override val destinationId = R.id.samsungFragment
    override val viewModelClass = SamsungViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    private val healthHelper by lazy { HealthConnectHelper(requireContext()) }
    private val permissions by lazy {
        setOf(
            HealthPermission.createReadPermission(StepsRecord::class),
            HealthPermission.createReadPermission(WeightRecord::class)
        )
    }

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        healthHelper.requestPermissionLauncher()
    }

    override fun addObservers() {
        viewModel.run {
            onStepsClick.observeEvent {
                healthHelper.checkAndExecute(permissions) {
                    readStepsByTimeRange()
                    readStepsIntoMonths()
                }
            }
            onWeightClick.observeEvent {
                healthHelper.checkAndExecute(permissions) {
                    readWeightByTimeRange()
                    readWeightAverage()
                }
            }
        }
    }

    private fun HealthConnectHelper.requestPermissionLauncher() {
        val permissionLauncher = registerForActivityResult(permissionContract()) { isGranted ->
            if (!isGranted.containsAll(permissions)) {
                Snackbar.make(requireView(), "please check permission", Snackbar.LENGTH_LONG).show()
            }
        }
        if (isAvailable()) permissionLauncher.launch(permissions)
        else installHealthConnect()
    }

    private fun HealthConnectHelper.checkAndExecute(
        permissions: Set<HealthPermission>, execute: () -> Unit
    ) {
        lifecycleScope.launch {
            if (isAvailable()) {
                if (hasAllPermission(getOrCreate(), permissions)) execute()
                else setPermission()
            } else installHealthConnect()
        }
    }

    private fun readStepsByTimeRange() {
        viewModel.run {
            readStepsByTimeRange(
                client = healthHelper.getOrCreate(),
                startTime = startOfDayInstant(ZONE_ID_SEOUL),
                endTime = endOfDayInstant(ZONE_ID_SEOUL)
            )
            stepsValue.observe(viewLifecycleOwner) { steps ->
                ui.viewDataBinding.txtStepsData.text = String.format(
                    resources.getString(R.string.txt_steps_data), steps
                )
            }
        }
    }

    private fun readStepsIntoMonths() {
        viewModel.run {
            readStepsIntoMonths(
                client = healthHelper.getOrCreate(),
                startTime = startOfDayLocalDate(ZONE_ID_SEOUL),
                endTime = endOfDayLocalDate(ZONE_ID_SEOUL)
            )
            stepsTotalValue.observe(viewLifecycleOwner) { steps ->
                ui.viewDataBinding.txtStepsTotalData.text = String.format(
                    resources.getString(R.string.txt_steps_total_data), steps
                )
            }
        }
    }

    private fun readWeightByTimeRange() {
        viewModel.run {
            readWeightByTimeRange(
                client = healthHelper.getOrCreate(),
                startTime = startOfDayInstant(ZONE_ID_SEOUL),
                endTime = endOfDayInstant(ZONE_ID_SEOUL)
            )
            weightValue.observe(viewLifecycleOwner) { weight ->
                ui.viewDataBinding.txtWeightData.text = String.format(
                    resources.getString(R.string.txt_weight_data), weight
                )
            }
        }
    }

    private fun readWeightAverage() {
        viewModel.run {
            readWeightAverage(
                client = healthHelper.getOrCreate(),
                startTime = startOfDayLocalDate(ZONE_ID_SEOUL),
                endTime = endOfDayLocalDate(ZONE_ID_SEOUL)
            )
            weightAverageValue.observe(viewLifecycleOwner) { weight ->
                ui.viewDataBinding.txtWeightAverageData.text = String.format(
                    resources.getString(R.string.txt_weight_average_data), weight
                )
            }
        }
    }
}