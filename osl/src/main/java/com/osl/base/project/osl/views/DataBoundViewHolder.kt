package com.osl.base.project.osl.views

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

class DataBoundViewHolder(
  val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry(this)
  private var wasPaused: Boolean = false

  init {
    lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
  }

  fun markCreated() {
    if (lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
      return
    }
    lifecycleRegistry.currentState = Lifecycle.State.CREATED
  }

  fun markAttach() {
    if (lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
      return
    }
    if (wasPaused) {
      lifecycleRegistry.currentState = Lifecycle.State.RESUMED
      wasPaused = false
    } else {
      lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }
  }

  fun markDetach() {
    if (lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
      return
    }
    wasPaused = true
    lifecycleRegistry.currentState = Lifecycle.State.CREATED
  }

  fun markDestroyed() {
    if (lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
      return
    }
    lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
  }

  override fun getLifecycle(): Lifecycle {
    return lifecycleRegistry
  }
}
