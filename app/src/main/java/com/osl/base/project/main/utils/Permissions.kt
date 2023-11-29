package com.osl.base.project.main.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.gun0912.tedpermission.provider.TedPermissionProvider
import timber.log.Timber.Forest.d

/** permission check*/
fun Context.checkPermissions(
    permissions: String,
    isGranted: (() -> Unit),
    isDenied: (() -> Unit)
) {
    when (ContextCompat.checkSelfPermission(
        TedPermissionProvider.context,
        permissions,
    )) {
        PackageManager.PERMISSION_GRANTED -> {
            d("plog PERMISSION_GRANTED")
            isGranted.invoke()
        }
        PackageManager.PERMISSION_DENIED -> {
            d("plog PERMISSION_DENIED")
            permission(isGranted, isDenied,permissions)
        }
    }
}

/** ted permission*/
fun permission(isGranted: () -> Unit, isDenied: () -> Unit, permissions: String) {
    TedPermission.create()
        .setPermissions(permissions)
        .setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
                isGranted.invoke()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                isDenied.invoke()
            }
        })
        .setDeniedMessage("Please Allow Permission")
//        .setDeniedCloseButtonText("close")
        .setGotoSettingButton(true)
        .check()
}


/** android 퍼미션
 * 현재는 테드 퍼미션 사용
 * 프래그먼트에서 아래 코드 사용해야함*/
/*
private var activityResultLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            d("camera permission activityResultLauncher PERMISSION_GRANTED")
        } else {
            d("camera permission activityResultLauncher PERMISSION_DENIED")
        }
    }

private fun cameraPermissionCheck() {
    when (ContextCompat.checkSelfPermission(
        TedPermissionProvider.context,
        Manifest.permission.CAMERA
    )) {
        PackageManager.PERMISSION_GRANTED -> {
            startBackCamera()
            d("camera permission PERMISSION_GRANTED")
        }
        PackageManager.PERMISSION_DENIED -> {
            d("camera permission PERMISSION_DENIED")
            activityResultLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}*/
