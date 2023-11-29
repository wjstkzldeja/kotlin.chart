package com.osl.swrnd.data

import android.util.Log.d
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.lang.reflect.Type
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Cache realm data object.
 *
 * just hash key query and response jquery.
 *
 * @author jbhwang
 * @since 20190923
 **/
open class CacheRealm : RealmObject() {
  @PrimaryKey
  var query: String = ""
  var response: String? = null
}

/**
 * read json data from cache structured by realm DB.
 *
 * convert method name and method arguments to hash key to use query.
 *
 * @author jbhwang
 * @since 20190923
 */
fun <T> readFromCache(collectionType: Type, vararg elements: Any?): T? {
  val e = elements.toList().joinToString("#_#")
  val query = hashKey(e)
  return Realm.getDefaultInstance().use { realm ->
    realm.where(CacheRealm::class.java)
      .equalTo("query", query)
      .findFirst()?.response?.let {
        d(e, it)
        Gson().fromJson(it, collectionType)
      }
  }
}

/**
 * write object to cache structured by realm DB.
 *
 * convert method name and method arguments to hash key to use query.
 *
 * @author jbhwang
 * @since 20190923
 */
fun <T> T.writeToCache(vararg elements: Any?): T {
  return apply {
    val e = elements.toList().joinToString("#_#")
    val query = hashKey(e)
    Gson().let {
      val toJson = it.toJson(this@apply)
      d(e, toJson)
      it.toJson(CacheRealm().apply {
        this.query = query
        this.response = toJson
      })
    }.let {
      try {
        Realm.getDefaultInstance().executeTransaction { realm ->
          realm.createOrUpdateObjectFromJson(CacheRealm::class.java, it)
        }
      } catch (_: Exception) {
      }
    }
  }
}

/**
 * generate hash key by string
 *
 * @author jbhwang
 * @since 20190923
 */
private fun hashKey(key: String): String {
  return try {
    val mDigest = MessageDigest.getInstance("MD5")
    mDigest.update(key.toByteArray())
    bytesToHexString(mDigest.digest())
  } catch (e: NoSuchAlgorithmException) {
    key.hashCode().toString()
  }
}

/**
 * convert byte array to hex string
 *
 * @author jbhwang
 * @since 20190923
 */
private fun bytesToHexString(bytes: ByteArray): String {
  val sb = StringBuilder()
  for (i in bytes.indices) {
    val hex = Integer.toHexString(0xFF and bytes[i].toInt())
    if (hex.length == 1) {
      sb.append('0')
    }
    sb.append(hex)
  }
  return sb.toString()
}
