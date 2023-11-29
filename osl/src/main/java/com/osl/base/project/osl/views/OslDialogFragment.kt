package com.osl.base.project.osl.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.osl.base.project.osl.OslApplication
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.utils.EventObserver
import com.osl.base.project.osl.utils.UiManager

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class OslDialogFragment<V : ViewDataBinding, VM : OslViewModel> :
  DialogFragment() {

  protected lateinit var ui: UiManager<V, VM>
  protected lateinit var actViewModel: OslViewModel
  protected val viewModel: VM get() = ui.viewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val viewLifecycleOwner = this.viewLifecycleOwner
    ui = UiManager(
      DataBindingUtil.inflate(inflater, layoutRes, container, false),
      ViewModelProvider(this, OslApplication.instance!!.viewModelFactory)[viewModelClass]
    )
    actViewModel = ViewModelProvider(requireActivity(), OslApplication.instance!!.viewModelFactory)[actViewModelClass]

    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          if (!viewModel.isOnLoading) {
            viewModel.onBackPressed()
          }
        }
      })
    ui.viewDataBinding.lifecycleOwner = viewLifecycleOwner
    return ui.viewDataBinding.root

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViews(savedInstanceState)
    addObservers()
    viewModel.onBackPressed.observeEvent(::onBackPressed)
  }

  @get:LayoutRes
  protected abstract val layoutRes: Int
  protected abstract val destinationId: Int
  protected abstract val viewModelClass: Class<VM>
  protected abstract val actViewModelClass: Class<out OslViewModel>
  protected abstract fun initViews(savedInstanceState: Bundle?)
  protected abstract fun addObservers()

  protected fun NavDirections.navigate() {
    if (findNavController().currentDestination?.id == destinationId) {
      findNavController().navigate(this)
    }
  }

  protected open fun onBackPressed() {
    findNavController().navigateUp()
  }

  protected fun LiveData<Event<Unit>>.observeEvent(observer: () -> Unit) {
    observe(viewLifecycleOwner, EventObserver { observer.invoke() })
  }

  protected fun <T> LiveData<Event<T>>.observeEvent(observer: (T) -> Unit) {
    observe(viewLifecycleOwner, EventObserver { observer.invoke(it) })
  }

  protected fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
    observe(viewLifecycleOwner) { observer.invoke(it) }
  }

}
