@file:Suppress("unused")

package com.osl.base.project.main.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.osl.base.project.osl.views.DataBoundListAdapter
import timber.log.Timber.Forest.d

@BindingAdapter(value = ["adapter", "items"], requireAll = false)
fun <T> setRecyclerViewAdapterItems(view: RecyclerView, adapter: DataBoundListAdapter<T>?, items: List<T>?) {
  if (view.adapter != adapter) {
    view.adapter = adapter
  }
  d("${items?.size}, ${view}")
  adapter?.submitList(items)
}

@BindingAdapter("layoutManager", requireAll = false)
fun setRecyclerViewLayoutManager(view: RecyclerView, layoutManager: RecyclerView.LayoutManager?) {
  view.layoutManager = layoutManager
}

@BindingAdapter("decorator", requireAll = false)
fun setRecyclerViewItemDecorator(view: RecyclerView, decorator: RecyclerView.ItemDecoration?) {
  decorator ?: return
  view.removeItemDecoration(decorator)
  view.addItemDecoration(decorator)
}

@BindingAdapter("pagerAdapter")
fun setViewPager2Adapter(view: ViewPager2, adapter: FragmentStateAdapter?) {
  view.adapter = adapter
}

@BindingAdapter("currentItem")
fun setViewPager2CurrentItem(view: ViewPager2, currentItem: Int) {
  view.setCurrentItem(currentItem, false)
}
