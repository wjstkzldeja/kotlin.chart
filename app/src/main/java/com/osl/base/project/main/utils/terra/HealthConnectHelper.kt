package com.osl.base.project.main.utils.terra

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import com.osl.base.project.osl.utils.HEALTH_CONNECT_PACKAGE
import com.osl.base.project.osl.utils.MARKET_URL
import com.osl.base.project.osl.utils.ONBOARDING_URL
import com.osl.base.project.osl.utils.VENDING_PACKAGE

class HealthConnectHelper(private val context: Context) {

    fun getOrCreate() = HealthConnectClient.getOrCreate(context)

    fun isAvailable() = HealthConnectClient.isAvailable(context)

    fun permissionContract() = PermissionController.createRequestPermissionResultContract()

    suspend fun hasAllPermission(
        client: HealthConnectClient, permissions: Set<HealthPermission>
    ) = client.permissionController.getGrantedPermissions(permissions).containsAll(permissions)

    fun setPermission() {
        val intent = context.packageManager.getLaunchIntentForPackage(HEALTH_CONNECT_PACKAGE)
        if (HealthConnectClient.isAvailable(context) && intent !== null) {
            context.startActivity(intent)
        }
    }

    fun installHealthConnect() {
        val url =
            Uri.parse(MARKET_URL).buildUpon().appendQueryParameter("id", HEALTH_CONNECT_PACKAGE)
                .appendQueryParameter("url", ONBOARDING_URL).build()
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setPackage(VENDING_PACKAGE)
            data = url
        }
        context.startActivity(intent)
    }
}