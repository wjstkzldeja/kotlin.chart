package com.osl.swrnd.domain.usecase

import com.osl.swrnd.domain.repository.IRepository
import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable

class Test2UseCase(
  private val repository: IRepository
) : UseCase<TestReq, TestVo>() {

  fun vo(title: String?): TestReq? {
    return TestReq(title ?: return null)
  }

  override fun TestReq.api(): Flowable<TestVo> {
    return repository.getTestData(title)
  }
}