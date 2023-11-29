package com.osl.base.project.main.views.themetest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentHomeBinding
import com.osl.base.project.main.databinding.FragmentThemeTestBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.dialog.TestViewModel
import com.osl.base.project.osl.utils.TEST_DIALOG_KEY
import com.osl.base.project.osl.utils.statusBarTransparency
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber.Forest.d

/** home fragment*/
class ThemeTestFragment : OslFragment<FragmentThemeTestBinding, ThemeTestViewModel>() {
    override val layoutRes = R.layout.fragment_theme_test
    override val destinationId = R.id.homeFragment
    override val viewModelClass = ThemeTestViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel


        statusBarTransparency()
 /*       setFullScreenTheme()
        statusBarTransparency()*/
        getNavigationBarHeight(context)

    }
    @SuppressLint("InternalInsetResource")
    fun getNavigationBarHeight(context: Context?): Int {
        if (context != null) {
            val statusBarHeightResourceId: Int =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (statusBarHeightResourceId > 0) {
                d("bottomHeight : ${context.resources.getDimensionPixelSize(statusBarHeightResourceId)}")
                return context.resources.getDimensionPixelSize(statusBarHeightResourceId)
            }
        }
        return 0
    }

    override fun addObservers() {
    }
}