package com.osl.base.project.main.views.login.viewpager

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentLoginViewPagerFragment4Binding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class LoginViewPagerFragment4 : OslFragment<FragmentLoginViewPagerFragment4Binding, LoginViewPagerFragment4ViewModel>() {
    override val layoutRes = R.layout.fragment_login_view_pager_fragment4
    override val destinationId = R.id.loginViewPagerFragment4
    override val viewModelClass = LoginViewPagerFragment4ViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java


    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
    }

    override fun addObservers() {
    }
}