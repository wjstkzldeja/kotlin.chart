package com.osl.base.project.main.views.container.main

import android.animation.ValueAnimator
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.osl.views.ILoadingHandler
import com.osl.base.project.osl.views.OslViewModel
import timber.log.Timber.Forest.d

class ActMainViewModel : OslViewModel(), ILoadingHandler {

  private val _loadingVisibility = MutableLiveData(View.GONE)
  val loadingVisibility: LiveData<Int> get() = _loadingVisibility

  private val _loadingMaxAlpha = MutableLiveData(0.5f)
  val loadingMaxAlpha: LiveData<Float> get() = _loadingMaxAlpha

  private val _loadingBgAnimator = MutableLiveData(ValueAnimator.ofFloat(0f, 0f).apply {
    duration = 500
  })
  val loadingBgAnimator: LiveData<ValueAnimator> get() = _loadingBgAnimator

  private val _loadingAnimator = MutableLiveData(ValueAnimator.ofFloat(0f, 0f).apply {
    duration = 500
  })
  val loadingAnimator: LiveData<ValueAnimator> get() = _loadingAnimator

  private val _bottomNavVisibility = MutableLiveData(View.VISIBLE)
  val bottomNavVisibility: LiveData<Int> get() = _bottomNavVisibility

  override fun requestLoading() {
    d("-")
    _loadingVisibility.value = View.VISIBLE
  }

  override fun requestFinishLoading() {
    d("-")
    _loadingVisibility.value = View.GONE
  }

  fun changeBottomNavVisibility(visibilityStatus: Boolean) {
    if(visibilityStatus){
      _bottomNavVisibility.value = View.VISIBLE
    }else{
      _bottomNavVisibility.value = View.GONE
    }
  }
}