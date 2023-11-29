package com.osl.swrnd.data.source

import com.google.gson.reflect.TypeToken
import com.osl.swrnd.data.readFromCache
import com.osl.swrnd.data.source.local.realm.RealmSource
import com.osl.swrnd.data.source.remote.ApiService
import com.osl.swrnd.data.writeToCache
import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.AuthTokenVo
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable
import timber.log.Timber.Forest.d
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class DataSource(
    private val localDataSource: RealmSource, private val remoteDataSource: ApiService
) : IDataSource {

    override fun getTestData(title: String): Flowable<TestVo> {
        return withCache(title) { remoteDataSource.getTestData(title) }.delaySubscription(
            5L, TimeUnit.SECONDS
        )
    }

    override fun getTestData(testReq: TestReq): Flowable<TestVo> {
        return Flowable.merge(
            localDataSource.getTestData2(testReq), remoteDataSource.getTestData2(testReq)
        )
    }

    override fun postGenerateAuthToken(devId: String, apiKey: String): Flowable<AuthTokenVo> {
        return remoteDataSource.postGenerateAuthToken(devId, apiKey)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    // default utils
    //
    ////////////////////////////////////////////////////////////////////////////////////////////

    private fun <T> withCache(vararg args: Any?, remoteRun: () -> Flowable<T>): Flowable<T> {
        val methodName = Throwable().stackTrace[1].methodName
        return Flowable.merge(
            object : TypeToken<T>() {}.type.read(
                methodName, args
            ), remoteRun.invoke().mapped(
                methodName, args
            )
        )
    }

    private fun <T> Flowable<T>.mapped(vararg args: Any?) = map {
        if (args.isNullOrEmpty()) {
            it
        } else {
            it.writeToCache(*args)
        }
    }

    private fun <T> Type.read(
        methodName: String, vararg args: Any?
    ) = Flowable.generate<T> { emitter ->

        val data: T? = readFromCache(this, methodName, *args)
        d("$data")
        data?.let {
            emitter.onNext(it)
        }
        emitter.onComplete()
    }

//  private fun <T> Flowable<ResVo<T>>.mapped(vararg args: Any?) = map {
//    if (it.success != true || it.payload == null) {
//      throw Exception(it.errorCode?.toString())
//    }
//
//    if (args.isNullOrEmpty()) {
//      it
//    } else {
//      it.writeToCache(*args)
//    }.payload!!
//  }
//
//  private fun <T> Type.read(
//    methodName: String,
//    vararg args: Any?
//  ) = Flowable.generate<T> { emitter ->
//
//    val data: ResVo<T>? = readFromCache(this, methodName, *args)
//    d { data }
//    data?.payload?.let {
//      emitter.onNext(it)
//    }
//    emitter.onComplete()
//  }
}