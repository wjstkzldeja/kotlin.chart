package com.osl.base.project.main.views.terra

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentTerraBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class TerraFragment : OslFragment<FragmentTerraBinding, TerraViewModel>() {
    override val layoutRes = R.layout.fragment_terra
    override val destinationId = R.id.terraFragment
    override val viewModelClass = TerraViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {
        viewModel.onLibreClick.observeEvent {
            TerraFragmentDirections.actionTerraFragmentToLibreFragment().navigate()
        }
        viewModel.onSamsungClick.observeEvent {
            TerraFragmentDirections.actionTerraFragmentToSamsungFragment().navigate()
        }
    }
}