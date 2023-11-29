package com.osl.base.project.main.utils.ruler

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class RulerPagerSnapHelper : PagerSnapHelper() {
  override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {

    val nextPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)

    return when {
      nextPos == RecyclerView.NO_POSITION -> nextPos
      layoutManager?.canScrollVertically() == true -> nextPos + velocityY / 400
      layoutManager?.canScrollHorizontally() == true -> nextPos + velocityX / 400
      else -> nextPos
    }
  }

  fun smoothScrollTo(layoutManager: RecyclerView.LayoutManager?, position: Int) {
    layoutManager ?: return
    createScroller(layoutManager)?.let {
      it.targetPosition = position
      layoutManager.startSmoothScroll(it)
    }
  }

  fun scrollTo(layoutManager: RecyclerView.LayoutManager?, position: Int) {
    layoutManager ?: return
    layoutManager.scrollToPosition(position)
//    createScroller(layoutManager)?.let {
//      it.targetPosition = position
//      layoutManager.scrollToPosition(it)
//    }
  }
}
