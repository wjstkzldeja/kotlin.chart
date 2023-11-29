@file:Suppress("unused")

package com.osl.base.project.osl.views

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.osl.base.project.osl.OslApplication
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.utils.EventObserver
import com.osl.base.project.osl.utils.UiManager
import com.osl.base.project.osl.utils.windowSoftInputMode
import timber.log.Timber.Forest.d

@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class OslFragment<V : ViewDataBinding, VM : OslViewModel> : Fragment(), MenuProvider {

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
    actViewModel =
      ViewModelProvider(requireActivity(), OslApplication.instance!!.viewModelFactory)[actViewModelClass]
    d("$viewModel, $actViewModel")

    ui.viewDataBinding.lifecycleOwner = viewLifecycleOwner

    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          if (!viewModel.isOnLoading) {
            viewModel.onBackPressed()
          }
        }
      })

    activity?.window?.let {
      windowSoftInputMode(it, view)
    }

    return ui.viewDataBinding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    null.setActionBar()
    initViews(savedInstanceState)
    addObservers()
    addLoadingObservers()
    viewModel.onBackPressed.observeEvent(::onBackPressed)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    ui.viewDataBinding.unbind()
  }

  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
  }

  override fun onMenuItemSelected(item: MenuItem) = when (item.itemId) {
    android.R.id.home -> {
      onBackPressed()
      true
    }

    else -> false
  }

  protected fun NavDirections.navigate() {
    if (findNavController().currentDestination?.id == destinationId) {
      findNavController().navigate(this)
    }
  }

  protected fun <T> LiveData<T>.observe(observer: (value: T) -> Unit) {
    observe(viewLifecycleOwner, Observer(observer))
  }

  protected fun <T> LiveData<Event<T>>.observeEvent(observer: (value: T) -> Unit) {
    observe(viewLifecycleOwner, EventObserver(observer))
  }

  protected fun LiveData<Event<Unit>>.observeEvent(observer: () -> Unit) {
    observe(viewLifecycleOwner, EventObserver { observer.invoke() })
  }

  protected fun <T> navResultObserve(
    key: String = "result",
    clearValue: T? = null,
    acceptValue: T? = null,
    observer: (T) -> Unit = {}
  ) {
    getNavigationResult<T?>(key)?.observe {
      it ?: return@observe
      ui.viewDataBinding.root.post {
        clearNavigationResult(clearValue, key)
        if (acceptValue == null || it == acceptValue) {
          observer.invoke(it)
        }
      }
    }
  }

  protected open fun onBackPressed() {
    if (!findNavController().navigateUp()) {
      activity?.finish()
    }
  }

  private fun addLoadingObservers() {
    viewModel.onLoading.observeEvent { (actViewModel as? ILoadingHandler)?.requestLoading() }
    viewModel.onFinishLoading.observeEvent { (actViewModel as? ILoadingHandler)?.requestFinishLoading() }
  }

  @get:LayoutRes
  protected abstract val layoutRes: Int

  @get:IdRes
  protected abstract val destinationId: Int
  protected abstract val viewModelClass: Class<VM>
  protected abstract val actViewModelClass: Class<out OslViewModel>
  protected abstract fun initViews(savedInstanceState: Bundle?)
  protected abstract fun addObservers()

  protected fun Toolbar?.setActionBar(
    enableNavigationUp: Boolean = false,
    navIcon: Int? = null/* = R.drawable.ic_nav_up*/,
    navUpListener: () -> Unit = ::onBackPressed
  ) {
    this ?: return
    if (enableNavigationUp) {
      navIcon?.let(this::setNavigationIcon)
      setNavigationOnClickListener { navUpListener.invoke() }
    }

  }

  protected fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
  }
}

private val Fragment.currentBackStackEntry: NavBackStackEntry?
  get() {
    val navController = findNavController()
    return navController.currentBackStackEntry?.takeUnless {
      it.destination.navigatorName.contains("dialog")
    } ?: let {
      navController.previousBackStackEntry
    }
  }

fun <T> Fragment.getNavigationResult(key: String = "result") =
  currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.clearNavigationResult(clearValue: T, key: String = "result") =
  currentBackStackEntry?.savedStateHandle?.set(key, clearValue)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
  findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
