package com.osl.base.project.main.views.foodlens

import android.graphics.Color
import android.os.Bundle
import com.doinglab.foodlens.sdk.FoodLens
import com.doinglab.foodlens.sdk.FoodLensBundle
import com.doinglab.foodlens.sdk.UIService
import com.doinglab.foodlens.sdk.UIServiceResultHandler
import com.doinglab.foodlens.sdk.errors.BaseError
import com.doinglab.foodlens.sdk.network.model.RecognitionResult
import com.doinglab.foodlens.sdk.network.model.UserSelectedResult
import com.doinglab.foodlens.sdk.theme.BottomWidgetTheme
import com.doinglab.foodlens.sdk.theme.DefaultWidgetTheme
import com.doinglab.foodlens.sdk.theme.ToolbarTheme
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentFoodLensBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber.Forest.d


class FoodLensFragment : OslFragment<FragmentFoodLensBinding, FoodLensViewModel>() {
    override val layoutRes = R.layout.fragment_food_lens
    override val destinationId = R.id.foodLensFragment
    override val viewModelClass = FoodLensViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    private var uiService: UIService? = null
    var recognitionResult: RecognitionResult? = null

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        uiService = FoodLens.createUIService(context)
        ui.viewDataBinding.onCamera.setOnClickListener {
            startFoodLensCamera()
        }

        ui.viewDataBinding.onFoodData.setOnClickListener {
            editFoodData()
        }
    }

    override fun addObservers() {
    }

    /** 푸드렌즈 카메라 ui*/
    private fun startFoodLensCamera() {
        d("foodLens startFoodLensCamera")
        //Create UI Service

        val bottomWidgetTheme = BottomWidgetTheme(context)
        bottomWidgetTheme.buttonTextColor = Color.BLUE
        bottomWidgetTheme.widgetRadius = 30f

        val defaultWidgetTheme = DefaultWidgetTheme(context)
        defaultWidgetTheme.widgetColor = Color.GREEN

        val toolbarTheme = ToolbarTheme(context)
        toolbarTheme.backgroundColor = Color.CYAN

        uiService?.setBottomWidgetTheme(bottomWidgetTheme)
        uiService?.setDefaultWidgetTheme(defaultWidgetTheme)
        uiService?.setToolbarTheme(toolbarTheme)
        val bundle = FoodLensBundle()
        bundle.isEnableManualInput = true //검색입력 활성화 여부
        bundle.eatType = 1 //식사 타입 수동 선택
        bundle.isSaveToGallery = true //갤러리 기능 활성화 여부
        bundle.isUseImageRecordDate = true //갤러리에 저장된 사진의 사진촬영시간을 입력시간으로 사용할지 여부
        bundle.isEnableCameraOrientation = true //카메라 회전 기능 지원 여부
        bundle.isEnablePhotoGallery = true //카메라 화면 갤러리 버튼 활성화 여부
        uiService?.setDataBundle(bundle)
        uiService?.startFoodLensCamera(activity, object : UIServiceResultHandler {
            override fun onSuccess(result: UserSelectedResult) {
                //implement code
                recognitionResult = result
                d("foodLens onSuccess : ${result.toJSONString()}")
            }

            override fun onCancel() {
                d("foodLens onCancel")
            }

            override fun onError(error: BaseError) {
                d("foodLens onSuccess : ${error.message}")
            }
        })
    }

    /** 푸드렌즈로 저장한 데이터 수정 ui*/
    fun editFoodData() {
        uiService!!.startFoodLensDataEdit(
            activity,
            recognitionResult,
            object : UIServiceResultHandler {
                override fun onSuccess(result: UserSelectedResult) {
                    d("foodLens editFoodData onSuccess : ${result.toJSONString()}")
                    recognitionResult = result
                }

                override fun onCancel() {
                    d("foodLens editFoodData onCancel")
                }

                override fun onError(error: BaseError) {
                    d("foodLens editFoodData onError : ${error.message}")
                }
            })
    }

}