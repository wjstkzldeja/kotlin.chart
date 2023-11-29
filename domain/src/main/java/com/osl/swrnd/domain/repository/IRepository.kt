package com.osl.swrnd.domain.repository

import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.AuthTokenVo
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable

interface IRepository {
    fun getTestData(title: String): Flowable<TestVo>
    fun getTestData(testReq: TestReq): Flowable<TestVo>
    fun postGenerateAuthToken(devId: String, apiKey: String): Flowable<AuthTokenVo>
}