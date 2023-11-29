package com.osl.base.project.main.views.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.DialogTestBinding
import com.osl.base.project.main.views.home.HomeViewModel
import com.osl.base.project.osl.utils.TEST_DIALOG_KEY
import com.osl.base.project.osl.views.OslDialogFragment
import com.osl.base.project.osl.views.setNavigationResult

class TestDialog : OslDialogFragment<DialogTestBinding, TestViewModel>() {
  override val layoutRes = R.layout.dialog_test
  override val destinationId = R.id.testDialog
  override val viewModelClass = TestViewModel::class.java
  override val actViewModelClass = HomeViewModel::class.java

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  override fun initViews(savedInstanceState: Bundle?) {
  }

  override fun addObservers() {
    viewModel.onConfirmEvent.observeEvent(this::onReactResult)
    viewModel.onBackEvent.observeEvent(this::onBackPressed)
  }

  private fun onBackPressed(react: TestViewModel.React) {
    setNavigationResult(react, TEST_DIALOG_KEY)
    onBackPressed()
  }

  private fun onReactResult(react: TestViewModel.React) {
    setNavigationResult(react, TEST_DIALOG_KEY)
    onBackPressed()
  }

}
