package com.osl.base.project.main.views.camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doinglab.foodlens.sdk.*
import com.doinglab.foodlens.sdk.errors.BaseError
import com.doinglab.foodlens.sdk.network.model.FoodSearchResult
import com.doinglab.foodlens.sdk.network.model.NutritionResult
import com.doinglab.foodlens.sdk.network.model.RecognitionResult
import com.doinglab.foodlens.sdk.ui.util.BitmapUtil.readContentIntoByteArray
import com.google.gson.Gson
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentCameraBinding
import com.osl.base.project.main.utils.checkPermissions
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import timber.log.Timber.Forest.d
import java.io.File


class CameraFragment : OslFragment<FragmentCameraBinding, CameraViewModel>() {
    override val layoutRes = R.layout.fragment_camera
    override val destinationId = R.id.cameraFragment
    override val viewModelClass = CameraViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    private var camera: Camera? = null
    private var cameraController: CameraControl? = null
    private var networkService: NetworkService? = null

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        networkService = FoodLens.createNetworkService(context)

        ui.viewDataBinding.nutrition.setOnClickListener {
            getNutritionInfo(518)
        }

        ui.viewDataBinding.foodSearch.setOnClickListener {
            getFoodSearch()
        }

        oddTest()

    }

    override fun addObservers() {
        viewModel.onCamera.observeEvent {
            cameraPermissionCheck()
        }

        viewModel.onPickAlbum.observeEvent {
            openPicture()
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            launcherCamera.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun cameraPermissionCheck() {
        context?.checkPermissions(
            Manifest.permission.CAMERA, this::isGranted, this::isDenied
        )
    }

    private fun isGranted() {
//        startBackCamera()
        dispatchTakePictureIntent()
    }

    private fun isDenied() {
        d("plog isDenied")
    }


    /** 카메라 */
    private fun startBackCamera() {
        val context = context ?: return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(ui.viewDataBinding.pvCamera.surfaceProvider)
            }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                d("Use case binding failed")
            }
            cameraController = camera!!.cameraControl
        }, ContextCompat.getMainExecutor(context))
    }

    /** 앨범 선택 */
    private fun openPicture() {
        val intent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        launcherAlbum.launch(intent)
    }

    private var launcherCamera: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null) {

            if (result.resultCode == RESULT_OK && result.data != null) {
                val extras = result.data!!.extras
                val bitmap = extras!!["data"] as Bitmap?
                ui.viewDataBinding.imgTest.setImageBitmap(bitmap)
            }
        }
    }

    private var launcherAlbum: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null) {
            if (result.resultCode == RESULT_OK&& result.data != null) {
                d("foodLens actResult result : ${result}")
                val intent: Intent? = result.data
                d("foodLens actResult intent : ${intent}")
                val uri: Uri? = intent?.data
                d("foodLens actResult uri : ${uri}")
                uri?.let {
                    setImgUri(uri)
//                        getFoodInfo(uri)
                }

            }
        }
    }

    /** 웹링크로*/
    fun oddTest() {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://www.hidoc.co.kr/healthstory/news/C0000741262" // 웹 페이지 URL
            val doc = Jsoup.connect(url).get() // Jsoup을 사용하여 웹 페이지를 가져옵니다.

// og:image 태그를 추출합니다.
            val ogImageUrl = doc.select("meta[property=og:image]").attr("content")
// og:title 태그를 추출합니다.
            val ogTitle = doc.select("meta[property=og:title]").attr("content")
// og:description 태그를 추출합니다.
            val ogDescription = doc.select("meta[property=og:description]").attr("content")

            MainScope().launch {
                context?.let {
                    Glide.with(it).load(ogImageUrl).into(ui.viewDataBinding.imgTest);
                }
            }
        }

    }

    fun setImgUri(imgUri: Uri) {
        val context = context ?: return
        val bitmap: Bitmap
        val source = ImageDecoder.createSource(context.contentResolver, imgUri)
        bitmap = ImageDecoder.decodeBitmap(source)
//        ui.viewDataBinding.imgTest.setImageBitmap(bitmap)
//        ui.viewDataBinding.imgTest.setImageURI(imgUri)
        /*   Glide.with(context)
               .load(imgUri)
               .into(ui.viewDataBinding.imgTest);*/

        viewModel.setImage(imgUri)
    }

    /** 선택한 사진 음식 정보*/
    fun getFoodInfo(uri: Uri) {
        val getByte = imageToByte(uri) ?: return
        /**음식 데이터 가져올 순위*/
        networkService?.nutritionRetrieveMode = NutritionRetrieveMode.TOP1_NUTRITION_ONLY
        networkService?.predictMultipleFood(getByte, object : RecognizeResultHandler {
            override fun onSuccess(result: RecognitionResult) {
                val json = result.toJSONString()
                d("foodLens getFoodInfo json : ${json}")
                val foodPosList = result.foodPositions //Get food positions

                for (fp in foodPosList) {
                    d("foodLens getFoodInfo foodName : ${fp.userSelectedFood.foodName}")

                    /** food positions 데이터*/
                    /* val foods: List<Food> = fp.foods //Get food candidates at this position
                     for (food in foods) {
                         //Print out food name at this position
                         d("foodLens getFoodInfo @@@@@@@@@@@@@@@@@@")
                         d("foodLens getFoodInfo foodId : ${food.foodId}")
                         d("foodLens getFoodInfo foodName : ${food.foodName}")
                         d("foodLens getFoodInfo keyName : ${food.keyName}")
                         d("foodLens getFoodInfo nutrition calories : ${food.nutrition?.calories ?: 0}")
                     }*/
                }
            }

            override fun onError(errorReason: BaseError) {
                d("foodLens getFoodInfo errorReason.message : ${errorReason.message}")
            }
        })
    }

    /** cursor로 바이트 이미지 생성*/
    private fun imageToByte(uri: Uri): ByteArray? {
        val selectedImage: Uri = uri

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(
            selectedImage, filePathColumn, null, null, null
        )
        cursor ?: return null

        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()


        d("foodLens getFoodInfo selectedImage : ${selectedImage}")
        d("foodLens getFoodInfo picturePath : ${picturePath}")
        val byteData = readContentIntoByteArray(File(picturePath))
        return byteData
    }

    /** 음식 영양정보 검색*/
    fun getNutritionInfo(foodId: Int) {
        d("foodLens getNutritionInfo")
        networkService?.getNutritionInfo(foodId, object : NutritionResultHandler {
            override fun onSuccess(result: NutritionResult) {
                val json = Gson().toJson(result)
                d("foodLens getNutritionInfo json : ${json}")
                d("foodLens getNutritionInfo result : ${result.nutrition.calories}")
            }

            override fun onError(errorReason: BaseError) {
                d("foodLens getNutritionInfo onError")
            }
        })
    }

    /** 음식 이름 검색*/
    fun getFoodSearch() {
        networkService?.searchFoodsByName("아메리카노", object : SearchResultHandler {
            override fun onSuccess(result: FoodSearchResult) {
                /** 음식 이름 단일*/
                /*result.searchedFoodList[0]?.let {
                    d("foodLens getFoodSearch foodId : ${it.foodId}")
                    d("foodLens getFoodSearch foodId : ${it.foodName}")
                    getNutritionInfo(it.foodId)
                }*/
                val json = Gson().toJson(result)
                d("foodLens getFoodSearch json : ${json}")
                /** 음식 이름 리스트*/
                result.searchedFoodList.forEach {
//                    d("foodLens getFoodSearch foodId : ${it.foodId}")
//                    d("foodLens getFoodSearch foodName : ${it.foodName}")
//                    getNutritionInfo(it.foodId)
                }

            }

            override fun onError(errorReason: BaseError) {}
        })
    }

}