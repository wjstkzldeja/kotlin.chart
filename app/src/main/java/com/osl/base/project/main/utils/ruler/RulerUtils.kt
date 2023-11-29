package com.osl.base.project.main.utils.ruler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/** apply padding 1 items to each head and tail */
val defaultWeightRange = IntRange(900 - 1, 1990 + 1)
val defaultKeyRange = IntRange(1400 - 1, 2400 + 1)
fun setupRuler(
  rvRuler: RecyclerView,
  linearLayoutManager: LinearLayoutManager,
  initPos: Int = 0,
  updated: (Int) -> Unit
): RulerPagerSnapHelper {
  val helper = RulerPagerSnapHelper()
  helper.attachToRecyclerView(rvRuler)
  rvRuler.postDelayed({
    helper.smoothScrollTo(linearLayoutManager, initPos)
  }, 100)
  /*rvRuler.postDelayed({
    helper.scrollTo(linearLayoutManager, initPos)
  }, 100)*/
  rvRuler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
      helper.findSnapView(rv.layoutManager)?.let {
        linearLayoutManager.getPosition(it)
      }?.let {
        updated.invoke(it)
      }
    }
  })

  return helper
}