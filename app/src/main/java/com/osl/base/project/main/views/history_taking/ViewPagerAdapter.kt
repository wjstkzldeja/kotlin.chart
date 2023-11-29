package com.osl.base.project.main.views.history_taking

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.osl.base.project.osl.views.OslFragment
import com.osl.base.project.osl.views.OslViewModel

class ViewPagerAdapter(
    list: ArrayList<OslFragment<out ViewDataBinding, out OslViewModel>>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragments = list

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}