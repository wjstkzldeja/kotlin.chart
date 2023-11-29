package com.osl.base.project.main.views.login.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.osl.base.project.main.views.login.viewpager.LoginViewPagerFragment1
import com.osl.base.project.main.views.login.viewpager.LoginViewPagerFragment2
import com.osl.base.project.main.views.login.viewpager.LoginViewPagerFragment3
import com.osl.base.project.main.views.login.viewpager.LoginViewPagerFragment4

class LoginViewPagerAdapter(
    private val fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        val fragment = when(position){
            0 -> LoginViewPagerFragment1()
            1 -> LoginViewPagerFragment2()
            2 -> LoginViewPagerFragment3()
            3 -> LoginViewPagerFragment4()
            else -> LoginViewPagerFragment1()
        }
        return fragment
    }
}