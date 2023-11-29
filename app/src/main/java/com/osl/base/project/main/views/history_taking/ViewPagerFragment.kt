package com.osl.base.project.main.views.history_taking

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentViewPagerBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.history_taking.begin.BeginFragment
import com.osl.base.project.main.views.history_taking.connect_sensor.ConnectSensorFragment
import com.osl.base.project.osl.views.OslFragment

class ViewPagerFragment : OslFragment<FragmentViewPagerBinding, ViewPagerViewModel>() {
    override val layoutRes = R.layout.fragment_view_pager
    override val destinationId = R.id.viewPagerFragment
    override val viewModelClass = ViewPagerViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        setViewPager()
    }

    override fun addObservers() {}

    private fun setViewPager() {
        val fragments = arrayListOf(ConnectSensorFragment(), BeginFragment())
        val adapter =
            ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)
        val viewpager = ui.viewDataBinding.viewPager
        viewpager.run {
            this.adapter = adapter
            isUserInputEnabled = false
        }
    }
}