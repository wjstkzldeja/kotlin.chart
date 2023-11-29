@file:Suppress("unused")

package com.osl.base.project.logger

import android.util.Log
import timber.log.Timber

class TimberDebugTree : Timber.DebugTree() {
  override fun createStackElementTag(element: StackTraceElement): String {
    return "(${element.fileName}:${element.lineNumber}) ${element.methodName}"
  }
}

class TimberReleaseTree : Timber.DebugTree() {

  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    @Suppress("ControlFlowWithEmptyBody")
    if (priority == Log.ERROR || priority == Log.WARN) {
      //SEND ERROR REPORTS TO YOUR Crashlytics.
    }
    super.log(priority, tag, message, t)
  }

  // disallow debug log
  override fun isLoggable(tag: String?, priority: Int) = (priority != Log.DEBUG)
}

// call this method when application created
fun initTimber() {
  // use below logic to release apk hide detail logs
//  if (BuildConfig.DEBUG) {
//    Timber.plant(TimberDebugTree())
//  } else {
//    Timber.plant(TimberReleaseTree())
//  }

  Timber.plant(TimberDebugTree())
}
