package com.osl.base.project.main.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.history_taking.connect_sensor.ConnectSensorViewModel
import com.osl.base.project.main.views.home.HomeViewModel
import com.osl.base.project.main.views.terra.libre.LibreViewModel
import com.osl.swrnd.domain.repository.IRepository
import com.osl.swrnd.domain.usecase.GenerateAuthTokenUseCase
import com.osl.swrnd.domain.usecase.Test2UseCase
import com.osl.swrnd.domain.usecase.TestUseCase

class ViewModelFactory(repository: IRepository) : ViewModelProvider.NewInstanceFactory() {

    private val testUseCase = TestUseCase(repository)
    private val test2UseCase = Test2UseCase(repository)
    private val generateAuthTokenUseCase = GenerateAuthTokenUseCase(repository)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(ActMainViewModel::class.java) -> ActMainViewModel()
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                    testUseCase, test2UseCase
                )
                isAssignableFrom(LibreViewModel::class.java) -> LibreViewModel(
                    generateAuthTokenUseCase
                )
                isAssignableFrom(ConnectSensorViewModel::class.java) -> ConnectSensorViewModel(
                    generateAuthTokenUseCase
                )
                else -> super.create(modelClass)
            }
        } as T

    }
}