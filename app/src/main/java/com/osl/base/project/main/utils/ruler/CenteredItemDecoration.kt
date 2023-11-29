package com.osl.base.project.main.utils.ruler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.osl.base.project.osl.utils.dp
/** itemWidth = item_rule width랑 맞춰야함*/
class CenteredItemDecoration(
  private val itemWidth: Int = 16.dp.toInt()
) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    super.getItemOffsets(outRect, view, parent, state)
    when (parent.layoutManager?.getPosition(view) ?: -1) {
      -1 -> return
      0 -> {
        outRect.left = (parent.measuredWidth - itemWidth) / 2 - itemWidth
        outRect.right = 0
      }
      parent.adapter?.itemCount?.minus(1) -> {
        outRect.left = 0
        outRect.right = (parent.measuredWidth - itemWidth) / 2 - itemWidth
      }
      else -> {
        outRect.left = 0
        outRect.right = 0
      }
    }
  }
}