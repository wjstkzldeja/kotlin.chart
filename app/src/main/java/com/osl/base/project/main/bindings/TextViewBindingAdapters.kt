@file:Suppress("unused")

package com.osl.base.project.main.bindings

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter
import com.osl.base.project.main.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@RequiresApi(Build.VERSION_CODES.P)
@BindingAdapter("text")
fun setTextStringRes(view: TextView, @StringRes stringResId: Int?) {
  val strId = stringResId?.takeIf { it != 0 } ?: view.let {
    view.text = null
    return
  }

  view.setText(strId)
  setupFonts(view)
}

@RequiresApi(Build.VERSION_CODES.P)
@BindingAdapter(value = ["string", "imeFocus", "isUnderline", "setupFonts"], requireAll = false)
fun setTextString(
  view: TextView,
  string: CharSequence?,
  imeFocus: Boolean? = false,
  isUnderline: Boolean? = false,
  isUpdateFonts: Boolean? = true
) {
  if (string != null) {
    view.text = string
  }

  if (isUnderline == true) {
    view.also {
      it.paint?.isUnderlineText = true
    }
  }

  if (imeFocus == true && view is EditText) {
    MainScope().launch {
      delay(10)
      view.requestFocus()
      val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
      imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
  }

  if (isUpdateFonts == true) {
    setupFonts(view)
  }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun setupFonts(
  view: TextView,
  text: CharSequence? = view.text,
  typeface: Typeface = getTypeface(view.context)
) {
  text ?: return
  val matcher = Pattern.compile("([^ㄱ-힇]+)").matcher(text)

  val spannable: Spannable = SpannableString(text)

  if (text is Spannable && text.getSpans(0, spannable.length, TypefaceSpan::class.java).isNotEmpty()) {
    return
  }
  while (matcher.find()) {
    spannable.setSpan(
      TypefaceSpan(typeface),
      matcher.start(),
      matcher.end(),
      Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
  }
  view.setText(spannable, TextView.BufferType.SPANNABLE)
}

private fun getTypeface(context: Context): Typeface {
  if (typeface != null) {
    return typeface!!
  }
  synchronized(TextViewBindingAdapter::class.java) {
    if (typeface == null) {
      typeface = typeface//Typeface.create(ResourcesCompat.getFont(context, R.font.), Typeface.NORMAL)
    }
  }
  return typeface!!
}

private var typeface: Typeface? = null