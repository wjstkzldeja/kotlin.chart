package com.osl.swrnd.data.source

import android.os.Environment
import timber.log.Timber.Forest.d
import java.io.File
import java.io.IOException

private const val parentFilePath = "/stAuto"

private const val defaultAddress = "192.168.0.1"
private const val dot = "."
private const val extension = "png"
private const val defaultFileName = defaultAddress + dot + extension

fun getServerIP(): String {
  val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path, parentFilePath)
  d("log path.exists() : ${path.exists()}")
  if (!path.exists()) {
    path.mkdirs()
  }

  val txtExists = path.listFiles()?.any { it.extension == extension } ?: false
  d("log txtExists : ${path.mkdir()}")
  return if (!txtExists) {
    write(path)
  } else {
    read(path)
  }
}

private fun write(path: File): String {
  return try {
    val file = File(path, defaultFileName)
    file.createNewFile()
    file.nameWithoutExtension
  } catch (e: IOException) {
    defaultAddress
  }
}

private fun read(path: File): String {
  return try {
    path.listFiles()?.firstOrNull { it.extension == extension }?.nameWithoutExtension ?: write(path)
  } catch (e: IOException) {
    defaultAddress
  }
}
