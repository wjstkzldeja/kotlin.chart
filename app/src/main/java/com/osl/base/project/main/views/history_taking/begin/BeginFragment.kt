package com.osl.base.project.main.views.history_taking.begin

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentBeginBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class BeginFragment : OslFragment<FragmentBeginBinding, BeginViewModel>() {
    override val layoutRes = R.layout.fragment_begin
    override val destinationId = R.id.beginFragment
    override val viewModelClass = BeginViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        setProgressStatus()
        setLayoutHeader()
    }

    override fun addObservers() {
        viewModel.onBeginClick.observe(viewLifecycleOwner) {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            viewPager.currentItem = 0
        }
    }

    private fun setProgressStatus() {
        val layoutProgress = ui.viewDataBinding.layoutProgress
        layoutProgress.run {
            txtTitle.text = resources.getString(R.string.begin_title)
            progressState.setAllStatesCompleted(true)
        }
    }

    private fun setLayoutHeader() {
        ui.viewDataBinding.txtHeader.text =
            String.format(resources.getString(R.string.begin_header), "올리브스톤")
    }
}