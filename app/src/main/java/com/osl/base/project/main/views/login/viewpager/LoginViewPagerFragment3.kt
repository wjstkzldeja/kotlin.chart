package com.osl.base.project.main.views.login.viewpager

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentLoginViewPagerFragment3Binding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class LoginViewPagerFragment3 : OslFragment<FragmentLoginViewPagerFragment3Binding, LoginViewPagerFragment3ViewModel>() {
    override val layoutRes = R.layout.fragment_login_view_pager_fragment3
    override val destinationId = R.id.loginViewPagerFragment3
    override val viewModelClass = LoginViewPagerFragment3ViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java


    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {
    }
}