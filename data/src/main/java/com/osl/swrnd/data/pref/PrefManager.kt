package com.osl.swrnd.data.pref

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.osl.swrnd.entity.local.res.TestVo
import com.osl.swrnd.entity.local.res.TimeListVo
import timber.log.Timber.Forest.v

object PrefManager : BasePrefManager() {

    private val gson = Gson()

    var userSeqNo: Long?
        get() = getLongSettings(KEY_USER_SEQ_NO, Long.MIN_VALUE).takeUnless { it == Long.MIN_VALUE }
        set(value) = setSettings(KEY_USER_SEQ_NO, value ?: Long.MIN_VALUE)

    var testVos: List<TestVo>?
        get() {
            val type = object : TypeToken<List<TestVo>>() {}.type
            return gson.fromJson(getStringSettings(KEY_TEST_VOS) ?: return null, type)
        }
        set(value) = setSettings(KEY_TEST_VOS, gson.toJson(value))

    /** alarm test code*/
    var testTimeList: ArrayList<TimeListVo>?
        get() {
            val type = object : TypeToken<ArrayList<TimeListVo>>() {}.type
            return gson.fromJson(getStringSettings("TIME_TEST") ?: return null, type)
        }
        set(value) {
            setSettings("TIME_TEST", value?.let { gson.toJson(value) })
        }

    fun initContext(context: Context) {
        v("context : $context")
        Companion.initContext(context)
    }

    fun terminate() {
        Companion.terminate()
    }
}

private const val KEY_USER_SEQ_NO = "KEY_USER_SEQ_NO"
private const val KEY_TEST_VOS = "KEY_TEST_VOS"
