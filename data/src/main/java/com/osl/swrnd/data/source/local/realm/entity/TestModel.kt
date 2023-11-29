package com.osl.swrnd.data.source.local.realm.entity

import com.osl.swrnd.entity.local.res.TestVo
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TestModel(
  @PrimaryKey
  var test: String? = "test",
  var isTest: Boolean? = true
) : RealmObject()

fun TestVo.toModel() = TestModel(
  test,
  isTest
)

fun TestModel.toVo() = TestVo(
  test ?: "test",
  isTest ?: true
)