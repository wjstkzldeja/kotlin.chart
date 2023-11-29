package com.osl.base.project.main.views.bottom_navigation.my

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentMyBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class MyFragment : OslFragment<FragmentMyBinding, MyViewModel>() {
    override val layoutRes = R.layout.fragment_my
    override val destinationId = R.id.myFragment
    override val viewModelClass = MyViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {}
}