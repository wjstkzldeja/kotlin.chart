package com.osl.base.project.main.bindings

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import timber.log.Timber.Forest.d

@BindingAdapter(value = ["url", "glide", "holder"], requireAll = false)
fun ImageView.setImageUrl(url: String?, glide: RequestManager?, holder: Drawable?) {
  (glide ?: return).clear(this)
  if (url == null) {
    setImageDrawable(null)
    return
  }

//  val t = System.currentTimeMillis()
//  var src: String? = null
  val src = when {
    url.contains("[.\\-]".toRegex()) -> url
    url.contains("/") -> url
    else -> resources.getIdentifier(url, "drawable", context.packageName)
  }

  val req = glide.load(src)
    .override(measuredWidth, measuredHeight)

  holder?.let {
    req.error(it)
  }

  req
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transition(DrawableTransitionOptions.withCrossFade())
//    .listener(object : RequestListener<Drawable> {
//      override fun onResourceReady(
//        resource: Drawable?,
//        model: Any?,
//        target: Target<Drawable>?,
//        dataSource: DataSource?,
//        isFirstResource: Boolean
//      ): Boolean {
//        d(isFirstResource, System.currentTimeMillis() - t, dataSource, src)
//        return false
//      }
//
//      override fun onLoadFailed(
//        e: GlideException?,
//        model: Any?,
//        target: Target<Drawable>?,
//        isFirstResource: Boolean
//      ) = false
//
//    })
    .into(this)
}

@BindingAdapter(value = ["src", "glide", "holder"], requireAll = false)
fun ImageView.setImageUrl(src: Int?, glide: RequestManager?, holder: Drawable?) {
  (glide ?: return).clear(this)
  if (src == null) {
    setImageDrawable(null)
    return
  }

  val req = glide.load(src)
    .override(measuredWidth, measuredHeight)

  holder?.let {
    req.error(it)
  }

  req
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transition(DrawableTransitionOptions.withCrossFade())
//    .listener(object : RequestListener<Drawable> {
//      override fun onResourceReady(
//        resource: Drawable?,
//        model: Any?,
//        target: Target<Drawable>?,
//        dataSource: DataSource?,
//        isFirstResource: Boolean
//      ): Boolean {
//        d(isFirstResource, System.currentTimeMillis() - t, dataSource, src)
//        return false
//      }
//
//      override fun onLoadFailed(
//        e: GlideException?,
//        model: Any?,
//        target: Target<Drawable>?,
//        isFirstResource: Boolean
//      ) = false
//
//    })
    .into(this)
}

@BindingAdapter("src", requireAll = false)
fun setImageResource(view: ImageView, src: Int?) {
  src ?: return

  view.setImageResource(src)
}

@BindingAdapter(value = ["uri", "glide", "holder"], requireAll = false)
fun setImageUri(view: ImageView, uri: Uri?, glide: RequestManager?, holder: Drawable?) {
  (glide ?: return).clear(view)
  if (uri == null) {
    view.setImageDrawable(null)
    return
  }
  val req = glide.load(uri)
    .override(view.measuredWidth, view.measuredHeight)

  holder?.let {
    req.error(it)
  }

  req
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transition(DrawableTransitionOptions.withCrossFade())
    .into(view)
}
