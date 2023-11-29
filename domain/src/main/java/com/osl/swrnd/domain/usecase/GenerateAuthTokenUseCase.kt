package com.osl.swrnd.domain.usecase

import com.osl.swrnd.domain.repository.IRepository
import com.osl.swrnd.entity.local.res.AuthTokenVo
import io.reactivex.Flowable

class GenerateAuthTokenUseCase(
    private val repository: IRepository
) : UseCase<GenerateAuthTokenUseCase.ReqVo, AuthTokenVo>() {

    fun vo(devId: String?, apiKey: String?): ReqVo? {
        return ReqVo(devId ?: return null, apiKey ?: return null)
    }

    override fun ReqVo.api(): Flowable<AuthTokenVo> {
        return repository.postGenerateAuthToken(devId, apiKey)
    }

    data class ReqVo(
        val devId: String, val apiKey: String
    )
}