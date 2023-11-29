package com.osl.swrnd.data.source.remote

import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.AuthTokenVo
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable
import retrofit2.http.*


interface ApiService {

    @GET(getTestData)
    fun getTestData(@Query("req") req: String): Flowable<TestVo>

    @POST(postTestData)
    fun getTestData2(@Body testReq: TestReq): Flowable<TestVo>?

    @POST(postGenerateAuthToken)
    fun postGenerateAuthToken(
        @Header("dev-id") devId: String, @Header("x-api-key") apiKey: String
    ): Flowable<AuthTokenVo>
}
