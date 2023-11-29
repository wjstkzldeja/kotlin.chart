package com.osl.base.project.main.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentTermsOfServiceBottomSheetBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.home.HomeFragmentDirections
import com.osl.base.project.osl.OslApplication
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.utils.EventObserver
import com.osl.base.project.osl.utils.UiManager
import com.osl.base.project.osl.views.OslViewModel
import timber.log.Timber

class TermsOfServiceBottomSheetFragment : BottomSheetDialogFragment() {
    val layoutRes = R.layout.fragment_terms_of_service_bottom_sheet
    val destinationId = R.id.termsOfServiceBottomSheetFragment
    val viewModelClass = TermsOfServiceBottomSheetViewModel::class.java
    val actViewModelClass = ActMainViewModel::class.java

    fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel

    }

    fun addObservers() {
        viewModel.onSubmitClick.observeEvent {
            Timber.d("terms of service submit")
            //HomeFragmentDirections.actionHomeFragmentToTestDialog().navigate()
            //가입 완료 버튼 누름 (checkbox 확인 필요)
            dismiss()
        }
    }
    override fun getTheme(): Int = R.style.BottomSheetDialog

    /**
     * OslDialogFragment에서 가져온 부분
     */
    protected lateinit var ui: UiManager<FragmentTermsOfServiceBottomSheetBinding, TermsOfServiceBottomSheetViewModel>
    protected lateinit var actViewModel: OslViewModel
    protected val viewModel: TermsOfServiceBottomSheetViewModel get() = ui.viewModel

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(savedInstanceState)
        addObservers()
        viewModel.onBackPressed.observeEvent(::onBackPressed)
    }

}