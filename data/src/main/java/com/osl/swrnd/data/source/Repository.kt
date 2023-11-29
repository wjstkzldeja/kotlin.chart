package com.osl.swrnd.data.source

import com.osl.swrnd.domain.repository.IRepository
import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.AuthTokenVo
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Repository(
    private val testDataSource: IDataSource
) : IRepository {

    override fun getTestData(title: String): Flowable<TestVo> {
        return testDataSource.getTestData(title).on
    }

    override fun getTestData(testReq: TestReq): Flowable<TestVo> {
        return testDataSource.getTestData(testReq).on
    }

    override fun postGenerateAuthToken(devId: String, apiKey: String): Flowable<AuthTokenVo> {
        return testDataSource.postGenerateAuthToken(devId, apiKey).on
    }

    private val <T> Flowable<T>.on
        get() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}