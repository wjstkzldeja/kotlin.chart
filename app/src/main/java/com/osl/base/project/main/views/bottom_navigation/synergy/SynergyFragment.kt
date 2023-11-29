package com.osl.base.project.main.views.bottom_navigation.synergy

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentSynergyBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class SynergyFragment : OslFragment<FragmentSynergyBinding, SynergyViewModel>() {
    override val layoutRes = R.layout.fragment_synergy
    override val destinationId = R.id.synergyFragment
    override val viewModelClass = SynergyViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {}
}