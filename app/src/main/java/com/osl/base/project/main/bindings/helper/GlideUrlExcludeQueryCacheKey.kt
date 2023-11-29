package com.osl.base.project.main.bindings.helper

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import java.net.URL

@Suppress("unused")
class GlideUrlExcludeQueryCacheKey : GlideUrl {
  constructor(url: String?) : super(url)
  constructor(url: String?, headers: Headers?) : super(url, headers)
  constructor(url: URL?) : super(url)
  constructor(url: URL?, headers: Headers?) : super(url, headers)

  override fun getCacheKey(): String {
    val url = toStringUrl()
    val queryIndex = url.indexOf("?")
    return if (queryIndex > -1) {
      url.substring(0, queryIndex)
    } else {
      url
    }
  }
}
