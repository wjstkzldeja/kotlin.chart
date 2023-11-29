package com.osl.base.project.osl

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.osl.base.project.logger.initTimber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class OslApplication : Application() {

    companion object {
        val diskIO: ExecutorService = Executors.newSingleThreadExecutor()

        var instance: OslApplication? = null
            private set
    }

    abstract val viewModelFactory: ViewModelProvider.Factory

    override fun onCreate() {
        instance = this
        super.onCreate()

        initTimber()
    }

    override fun onTerminate() {
        instance = null
        super.onTerminate()
    }
}