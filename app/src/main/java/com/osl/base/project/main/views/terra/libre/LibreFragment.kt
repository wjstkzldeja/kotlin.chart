package com.osl.base.project.main.views.terra.libre

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.tryterra.terra.Terra
import co.tryterra.terra.enums.Connections
import com.google.android.material.snackbar.Snackbar
import com.osl.base.project.main.BuildConfig.TERRA_DEV_ID
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentLibreBinding
import com.osl.base.project.main.utils.checkPermissions
import com.osl.base.project.main.views.terra.worker.NotificationWorker
import com.osl.base.project.main.utils.terra.getTimeDuration
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.utils.NOTIFICATION_WORK
import com.osl.base.project.osl.utils.START_INTENT
import com.osl.base.project.osl.utils.TEST_TIMESTAMP
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber.Forest.d
import java.util.concurrent.TimeUnit

class LibreFragment : OslFragment<FragmentLibreBinding, LibreViewModel>() {
    override val layoutRes = R.layout.fragment_libre
    override val destinationId = R.id.libreFragment
    override val viewModelClass = LibreViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    private val mContext by lazy { requireContext() }

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        initConnection()
        checkAndExecute()
    }

    override fun addObservers() {
        viewModel.onGlucoseClick.observeEvent { readGlucoseData() }
    }

    private fun readGlucoseData() {
        viewModel.run {
            readGlucoseData(mContext)
            glucoseValue.observe(viewLifecycleOwner) { glucose ->
                ui.viewDataBinding.txtGlucoseData.text = String.format("%.0f", glucose)
            }
        }
    }

    private fun initConnection() {
        viewModel.run {
            generateAuthToken()
            tokenValue.observe(viewLifecycleOwner) { token ->
                Terra.instance(TERRA_DEV_ID, null, mContext) { manager, _ ->
                    manager.initConnection(
                        connection = Connections.FREESTYLE_LIBRE,
                        token = token,
                        context = mContext,
                        startIntent = START_INTENT
                    ) { isConnected, error ->
                        if (!isConnected) d("connection error: ${error?.message}")
                    }
                }
            }
        }
    }

    private fun createWorkManager() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
            getTimeDuration(TEST_TIMESTAMP), TimeUnit.SECONDS
        ).build()
        WorkManager.getInstance(mContext)
            .enqueueUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.KEEP, oneTimeWorkRequest)
    }

    private fun checkAndExecute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mContext.checkPermissions(
                Manifest.permission.POST_NOTIFICATIONS, this::isGranted, this::isDenied
            )
        }
    }

    private fun isGranted() = createWorkManager()
    private fun isDenied() =
        Snackbar.make(requireView(), "please check permission", Snackbar.LENGTH_LONG).show()
}