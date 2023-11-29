package com.osl.swrnd.domain.usecase

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

abstract class UseCase<VO, RESULT> {
  fun VO.request(
    success: (RESULT) -> Unit,
    error: (Throwable) -> Unit,
    preExecutor: (() -> Unit)? = null,
    postExecutor: (() -> Unit)? = null
  ): Disposable? {
    preExecutor?.invoke()
    var rst: Any? = null
    return api().subscribe(
      {
        if (rst == it) {
          postExecutor?.invoke()
          return@subscribe
        }
        rst = it
        success.invoke(it)
        postExecutor?.invoke()
      },
      {
        error.invoke(it)
        postExecutor?.invoke()
      }
    )
  }

  protected abstract fun VO.api(): Flowable<RESULT>
}