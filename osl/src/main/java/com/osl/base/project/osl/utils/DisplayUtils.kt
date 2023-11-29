@file:Suppress("unused")

package com.osl.base.project.osl.utils

import android.graphics.Color
import android.os.Build
import android.view.*
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment

/**
 * [reference blog](https://velog.io/@tltty123/SOFTINPUTADJUSTRESIZE-Deprecated)
 */
fun windowSoftInputMode(w: Window, view: View?) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    w.setDecorFitsSystemWindows(true)
    view?.setOnApplyWindowInsetsListener { v, insets ->
      val topInset = insets.getInsets(WindowInsets.Type.statusBars()).top
      val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
      val navigationHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom
      val bottomInset = if (imeHeight == 0) navigationHeight else imeHeight
      v.setPadding(0, topInset, 0, bottomInset)
      insets
    }
  } else {
    @Suppress("DEPRECATION")
    w.setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
          or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    )
  }
}

/** 풀스크린 테마
 * 이슈 :
 * - statusBarTransparency 함수를 사용하지 않으면 노치부분 풀스크린이 안됨
 * - theme <item name="android:windowLayoutInDisplayCutoutMode">shortEdges</item> 사용해야 노치부분 대응함, 사용안하면 검은색 나옴
 * https://developer.android.com/guide/topics/display-cutout
 * - WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE 사용해야 사용자가 스와이프 했을때 풀스크린 유지됨*/
fun Fragment.setFullScreenTheme() {
  val activity = activity ?: return
  val w = activity.window ?: return
  val v = view ?: return
  /*API 30 이상*/
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    val windowInsetsController = activity.window.decorView.windowInsetsController ?: return
    windowInsetsController.hide(
      WindowInsets.Type.statusBars()
              or WindowInsets.Type.navigationBars()
    )
    windowInsetsController.systemBarsBehavior =
      WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
  } else {
    /*API 30 이하*/
    w.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    v.systemUiVisibility = FLAG_FULLSCREEN
    displayCutoutShortMode(w)
  }

  WindowCompat.setDecorFitsSystemWindows(w, false)
}

/** 풀스크린 테마 해제 */
fun Fragment.setFullScreenThemeClear() {
  val activity = activity ?: return
  val w = activity.window ?: return
  val v = view ?: return
  /*API 30 이상*/
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    val windowInsetsController = activity.window.decorView.windowInsetsController ?: return
    windowInsetsController.show(
      WindowInsets.Type.statusBars()
              or WindowInsets.Type.navigationBars()
    )
    windowInsetsController.systemBarsBehavior =
      WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
  } else {
    /*API 30 이하*/
    w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    v.systemUiVisibility = v.systemUiVisibility and FLAG_FULLSCREEN.inv()
    displayCutoutShortMode(w)
  }
}

/** 상태바, 하단 네비 투명 테마*/
fun Fragment.setTransparentLightTheme() {
  val activity = activity ?: return
  val w = activity.window ?: return
  w.setFlags(
    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
  )
  w.decorView.systemUiVisibility = 0
  displayCutoutShortMode(w)
}

/** 상태바, 하단 네비 투명 테마 해제 */
fun Fragment.setTransparentLightThemeClear() {
  val activity = activity ?: return
  val w = activity.window ?: return
  w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
  w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
  displayCutoutShortMode(w)
}

/** 상태바, 하단 네비 투명 테마 삭제 */
fun Fragment.setDarkTheme() {
  val activity = activity ?: return
  val w = activity.window ?: return

  w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
  w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
  w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
  w.clearFlags(View.SYSTEM_UI_FLAG_LOW_PROFILE)
  w.clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    w.clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    w.clearFlags(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
  }
  w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
  w.setFlags(
    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
  )
  w.statusBarColor = Color.BLUE
  w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

/** 상태바 반투명 검은색, 텍스트 배터리 빼고 삭제 */
fun Fragment.setTransparentDarkTheme() {
  val activity = activity ?: return
  val w = activity.window
  w.setFlags(
    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
  )
  w.decorView.systemUiVisibility = FLAG_TRANSPARENT_DARK_MODE
  displayCutoutShortMode(w)
}

/**
 * 상태바 숨기는 함수
 * -  <item name="android:windowTranslucentStatus">true</item> 사용하지 않으면 노치부분 풀스크린이 안됨
 * - theme <item name="android:windowLayoutInDisplayCutoutMode">shortEdges</item> 사용해야 노치부분 대응함
 * - WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE 사용해야 사용자가 스와이프 했을때 풀스크린 유지됨
 * */
fun Fragment.hideStatusBar() {
  val activity = activity ?: return
  val w = activity.window
  val wiController = WindowInsetsControllerCompat(w, w.decorView)
  wiController.hide(
    WindowInsetsCompat.Type.statusBars()
  )
  wiController.systemBarsBehavior =
    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

/**네비게이션 숨기는 함수*/
fun Fragment.hideNavigatorBar() {
  val activity = activity ?: return
  val w = activity.window
  val wiController = WindowInsetsControllerCompat(w, w.decorView)
  wiController.hide(
    WindowInsetsCompat.Type.navigationBars()
  )
  wiController.systemBarsBehavior =
    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

/** 상태바 텍스트 색상 변경
 * <item name="android:windowLightStatusBar">true</item> 같은 기능으로 보임 */
fun Fragment.lightStatusBar(isLight: Boolean = true) {
  val activity = activity ?: return
  val w = activity.window
  WindowCompat.getInsetsController(w, w.decorView).isAppearanceLightStatusBars = isLight
}

/** 네비게이션 텍스트 색상 변경
 * <item name="android:windowLightNavigationBar">true</item> 같은 기능으로 보임 */
fun Fragment.lightNavigatorBar(isLight: Boolean = true) {
  val activity = activity ?: return
  val w = activity.window
  WindowCompat.getInsetsController(w, w.decorView).isAppearanceLightNavigationBars = isLight
}

/** 상태바만 투명하게 만드는 함수
 * 이슈 : 레이아웃 하단 정렬시, 하단 네비게이션과 겹쳐짐
 * <item name="android:statusBarColor">@android:color/transparent</item> 같은 기능*/
fun Fragment.statusBarTransparency() {
  val activity = activity ?: return
  val w = activity.window
  w.statusBarColor = Color.TRANSPARENT
  // Making status bar overlaps with the activity
  WindowCompat.setDecorFitsSystemWindows(w, false)
}

/** 상태바만 투명하게 해제 함수*/
fun Fragment.statusBarTransparencyClear(@ColorRes color: Int) {
  val activity = activity ?: return
  val w = activity.window
  w.statusBarColor = ContextCompat.getColor(activity, color)
  // Making status bar overlaps with the activity
  WindowCompat.setDecorFitsSystemWindows(w, true)
}

fun Fragment.blackNavColor() {
  statusBarTransparencyClear(android.R.color.black)
}

fun Fragment.whiteNavColor() {
  statusBarTransparencyClear(android.R.color.white)
}

fun displayCutoutShortMode(w: Window?) {
  if (w == null) {
    return
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    w.attributes.layoutInDisplayCutoutMode =
      WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
  }
}
