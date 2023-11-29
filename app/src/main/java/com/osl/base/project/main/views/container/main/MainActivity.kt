package com.osl.base.project.main.views.container.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.doinglab.foodlens.sdk.FoodLens
import com.doinglab.foodlens.sdk.UIService
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.osl.base.project.main.App
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.ActivityMainBinding
import com.osl.base.project.osl.views.OslActivity


class MainActivity : OslActivity<ActivityMainBinding, ActMainViewModel>() {
    override val navRes: Int = R.id.nav_host
    override val layoutRes: Int get() = R.layout.activity_main
    override val viewModel: ActMainViewModel by viewModels { (application as App).viewModelFactory }

    private var uiService: UIService? = null

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        // ui.viewDataBinding.setVariable(BR.glide, Glide.with(ui.viewDataBinding.root))
        setNavController()
    }

    override fun addObservers() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uiService = FoodLens.createUIService(TedPermissionProvider.context)
        uiService?.onActivityResult(requestCode, resultCode, data)
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        ui.viewDataBinding.bnvMain.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.healthPathFragment
                || destination.id == R.id.synergyFragment || destination.id == R.id.myFragment
            ) {
                viewModel.changeBottomNavVisibility(true)
            } else {
                viewModel.changeBottomNavVisibility(false)
            }
        }
    }
}