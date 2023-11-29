package com.osl.swrnd.domain.usecase

import com.osl.swrnd.domain.repository.IRepository
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable

class TestUseCase(
  private val repository: IRepository
) : UseCase<TestUseCase.ReqVo, TestVo>() {

  fun vo(title: String?): ReqVo? {
    return ReqVo(title ?: return null)
  }

  override fun ReqVo.api(): Flowable<TestVo> {
    return repository.getTestData(title)
  }

  data class ReqVo(
    val title:String
  )
}