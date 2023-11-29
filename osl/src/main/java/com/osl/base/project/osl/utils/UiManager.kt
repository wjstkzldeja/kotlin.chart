package com.osl.base.project.osl.utils

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.osl.base.project.osl.BR

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class UiManager<V : ViewDataBinding, VM : ViewModel>(
  val viewDataBinding: V,
  val viewModel: VM
) {

  init {
    viewDataBinding.setVariable(BR.glide, Glide.with(viewDataBinding.root))
  }
  val root: View get() = viewDataBinding.root
}
