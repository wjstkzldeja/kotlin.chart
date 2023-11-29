package com.osl.base.project.main.views.terra.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.osl.base.project.main.utils.terra.NotificationHelper

class NotificationWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            NotificationHelper(applicationContext).run {
                createNotificationChannel()
                displayNotification()
            }
        } catch (e: Exception) {
            Result.retry()
        }
        return Result.success()
    }
}