package com.osl.base.project.main.views.bottom_navigation.health_path

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentHealthPathBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class HealthPathFragment : OslFragment<FragmentHealthPathBinding, HealthPathViewModel>() {
    override val layoutRes = R.layout.fragment_health_path
    override val destinationId = R.id.healthPathFragment
    override val viewModelClass = HealthPathViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {}
}