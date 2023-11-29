package com.osl.base.project.main

import androidx.lifecycle.ViewModelProvider
import com.osl.base.project.main.factory.ViewModelFactory
import com.osl.base.project.osl.OslApplication
import com.osl.swrnd.data.pref.PrefManager
import com.osl.swrnd.domain.repository.IRepository

class App : OslApplication() {
  private val _viewModelFactory: ViewModelFactory by lazy { ViewModelFactory(dataRepository) }
  override val viewModelFactory: ViewModelProvider.Factory
    get() = _viewModelFactory

  private val dataRepository: IRepository
    get() = RepositoryLocator.provideRepository(this)

  init {
    instance?.applicationContext?.let { PrefManager.initContext(it) }
  }
}