@file:Suppress("unused")

package com.osl.base.project.main.bindings

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.osl.base.project.main.bindings.helper.GlideUrlExcludeQueryCacheKey
import com.osl.base.project.main.bindings.helper.OnScrollYListener
import com.osl.base.project.osl.utils.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber.Forest.d


@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean) {
  if (visible) {
    view.visibility = View.VISIBLE
  } else {
    view.visibility = View.GONE
  }
}

@BindingAdapter(value = ["visibility", "alphaAnimator", "maxAlpha"], requireAll = false)
fun setAlphaAnimator(view: View, visibility: Int, animator: ValueAnimator?, max: Float = 1f) {
  if (view.visibility == visibility) {
    return
  }
  if (animator == null) {
    view.visibility = visibility
    return
  }
  val to = if (visibility == View.VISIBLE) {
    max
  } else {
    0f
  }
  val from = view.alpha
  animator.apply {
    cancel()
    removeAllListeners()
    removeAllUpdateListeners()
    setFloatValues(view.alpha, to)
    addUpdateListener {
      val value = it.animatedValue as Float
      view.alpha = value
      view.invalidate()
    }
    addListener(
      onStart = {
        if (visibility == View.VISIBLE) {
          view.visibility = visibility
        }
      },
      onEnd = {
        if (visibility == View.GONE) {
          view.visibility = visibility
        }
      })
    start()
  }
}

@BindingAdapter("translateYAnimator")
fun setTranslateYAnimation(view: View, animator: ValueAnimator) {
  view.translationY = animator.animatedValue as Float
  animator.apply {
    this.addUpdateListener {
      view.translationY = it.animatedValue as Float
    }
    start()
  }
}

@BindingAdapter("translateXAnimator")
fun setTranslateXAnimation(view: View, animator: ValueAnimator) {
  view.translationX = animator.animatedValue as Float
  animator.apply {
    this.addUpdateListener {
      view.translationX = it.animatedValue as Float
    }
    start()
  }
}

@BindingAdapter("enabled")
fun setEnabledState(view: View, enabled: Boolean) {
  view.isEnabled = enabled
}

@BindingAdapter("viewHeight")
fun setViewHeight(view: View, height: Int) {
  view.layoutParams = view.layoutParams.apply {
    this.height = height
  }
  view.invalidate()
}

@BindingAdapter("guideBegin")
fun setGuideBegin(view: View, begin: Int) {
  if (view.layoutParams is ConstraintLayout.LayoutParams) {
    view.layoutParams = (view.layoutParams as ConstraintLayout.LayoutParams).apply {
      this.guideBegin = begin
    }
    view.invalidate()
  }
}

@BindingAdapter("rotate")
fun setRotate(view: View, rotate: Float) {
  view.rotation = rotate
}

@BindingAdapter("selected")
fun setSelectedState(view: View, selected: Boolean) {
  view.isSelected = selected
}

@BindingAdapter(value = ["viewGlide", "backgroundAwsUrl"], requireAll = false)
fun setBackgroundImage(view: View, glide: RequestManager?, url: String?) {
  (glide ?: return)
    .load(GlideUrlExcludeQueryCacheKey(url ?: return))
    .override(view.measuredWidth, view.measuredHeight)
    .transition(DrawableTransitionOptions.withCrossFade())
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .into(object : CustomTarget<Drawable?>() {
      override fun onLoadCleared(placeholder: Drawable?) = Unit
      override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
        view.background = resource
      }
    })
}

@BindingAdapter("onScrollY")
fun setOnScrollYListener(view: View, listener: OnScrollYListener) {
  if (view !is RecyclerView) {
    view.setOnScrollChangeListener { _, _, y, _, oldY ->
      listener.onScrollY(y, oldY)
    }
    return
  }

  view.clearOnScrollListeners()
  view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
      d("$dy")
      listener.onScrollY(recyclerView.scrollY + dy, recyclerView.scrollY)
    }
  })
}

@BindingAdapter("toScrollY")
fun setScrollY(view: View, y: Int) {
  MainScope().launch {
    delay(100)
    if (view is NestedScrollView) {
      delay(100)
      view.smoothScrollTo(0, y)
    } else {
      view.scrollY = y
    }
  }
}



@BindingAdapter("onLayoutChanged")
fun setOnLayoutChangedListener(view: View, listener: View.OnLayoutChangeListener) {
  view.removeOnLayoutChangeListener(listener)
  view.addOnLayoutChangeListener(listener)
}

@BindingAdapter("viewHeightDp")
fun setViewHeightDp(view: View, heightDp: Int) {
  view.layoutParams = view.layoutParams.apply {
    this.height = heightDp.dp.toInt()
  }
  view.invalidate()
}