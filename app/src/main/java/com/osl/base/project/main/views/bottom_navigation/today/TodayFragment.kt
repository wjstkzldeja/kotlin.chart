package com.osl.base.project.main.views.bottom_navigation.today

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentTodayBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class TodayFragment : OslFragment<FragmentTodayBinding, TodayViewModel>() {
    override val layoutRes = R.layout.fragment_today
    override val destinationId = R.id.todayFragment
    override val viewModelClass = TodayViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {}
}