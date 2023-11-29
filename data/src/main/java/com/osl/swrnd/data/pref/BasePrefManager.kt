@file:Suppress("unused", "SameParameterValue")

package com.osl.swrnd.data.pref

import android.content.Context
import timber.log.Timber.Forest.v

/**
 * Created by jb.hwang on 2017-02-06.
 */
open class BasePrefManager {
  companion object {
    protected var mContext: Context? = null
    protected var KEY_PREFERENCES: String? = null

    @JvmStatic
    protected fun initContext(context: Context) {
      v("context : $context")
      mContext = context
      if (mContext != null) {
        KEY_PREFERENCES =
          mContext!!.packageName + "_preferences"
      }
    }

    @JvmStatic
    protected fun setSettings(key: String?, value: Int) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putString(key, value.toString())
      e.apply()
    }

    @JvmStatic
    protected fun setSettings(key: String?, value: Long) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putLong(key, value)
      e.apply()
    }

    @JvmStatic
    protected fun setSettings(key: String?, value: Float) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putFloat(key, value)
      e.apply()
    }

    @JvmStatic
    protected fun setSettings(key: String?, text: String?) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putString(key, text)
      e.apply()
    }

    @JvmStatic
    protected fun setSettings(key: String?, bool: Boolean) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putBoolean(key, bool)
      e.apply()
    }

    @JvmStatic
    protected fun setSettings(key: String?, value: Set<String?>?) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putStringSet(key, value)
      e.apply()
    }

    @JvmStatic
    protected fun setIntSetSettings(key: String?, value: Set<Int>?) {
      if (null == mContext) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val e = pref.edit()
      e.putStringSet(key, value?.map { it.toString() }?.toSet())
      e.apply()
    }

    @JvmStatic
    protected fun getSettings(key: String?): Int {
      if (mContext == null) {
        return 0
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return Integer.valueOf(pref.getString(key, "0")!!)
    }

    @JvmStatic
    protected fun getStringSetSettings(key: String?): Set<String>? {
      if (mContext == null) {
        return null
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return pref.getStringSet(key, null)
    }

    @JvmStatic
    protected fun getIntSetSettings(key: String?): Set<Int>? {
      if (mContext == null) {
        return null
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val stringSet = pref.getStringSet(key, null)
      return stringSet?.map { it.toInt() }?.toSet()
    }

    /* default value is false */
    @JvmStatic
    protected fun getBooleanSettings(key: String?): Boolean {
      return getBooleanSettings(key, false)
    }

    @JvmStatic
    protected fun getBooleanSettings(key: String?, defaultValue: Boolean): Boolean {
      if (mContext == null) {
        return defaultValue
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return pref.getBoolean(key, defaultValue)
    }

    @JvmStatic
    protected fun getStringSettings(key: String?): String? {
      if (mContext == null) {
        return null
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return pref.getString(key, null)
    }

    @JvmStatic
    protected fun getStringSettings(key: String?, defaultValue: String?): String? {
      if (mContext == null) {
        return defaultValue
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return pref.getString(key, defaultValue)
    }

    @JvmStatic
    protected fun getSettings(key: String?, initValue: Int): Int {
      if (mContext == null) {
        return 0
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return Integer.valueOf(pref.getString(key, initValue.toString())!!)
    }

    @JvmStatic
    protected fun getLongSettings(key: String?, initValue: Long): Long {
      if (mContext == null) {
        return 0
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      return pref.getLong(key, initValue)
    }

    @JvmStatic
    protected fun removeSettings(userName: String?) {
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val editor = pref.edit()
      editor.remove(userName)
      editor.apply()
    }

    @JvmStatic
    fun removeSettings() {
      if (mContext == null) {
        return
      }
      val pref = mContext!!.getSharedPreferences(
        KEY_PREFERENCES,
        Context.MODE_PRIVATE
      )
      val editor = pref.edit()
      val keySet: Set<String> = pref.all.keys
      for (key in keySet) {
        editor.remove(key)
      }
      editor.apply()
    }

    @JvmStatic
    fun terminate() {
      mContext = null
    }
  }
}