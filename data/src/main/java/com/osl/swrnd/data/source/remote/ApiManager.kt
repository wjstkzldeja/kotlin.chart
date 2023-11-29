package com.osl.swrnd.data.source.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.osl.swrnd.data.BuildConfig
import com.osl.swrnd.data.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiManager {

    private fun getInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(getInterceptor()).build()
    }

    private fun getGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    fun getService(context: Context): ApiService {
        return Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGson())).baseUrl(
                context.getString(R.string.protocol) + context.getString(R.string.base_url) + ":" + context.getString(
                    R.string.port
                )
            ).client(getHttpClient()).build().create(ApiService::class.java)
    }
}