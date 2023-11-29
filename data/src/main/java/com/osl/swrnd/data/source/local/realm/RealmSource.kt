package com.osl.swrnd.data.source.local.realm

import android.content.Context
import com.osl.swrnd.data.source.local.realm.entity.TestModel
import com.osl.swrnd.data.source.local.realm.entity.toVo
import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmSource(context: Context) {

  init {
    Realm.init(context)
    RealmConfiguration.Builder()
      .schemaVersion(0)
      .build()
  }

  fun getTestData2(testReq: TestReq): Flowable<TestVo> {
    return Flowable.generate { emitter ->
      Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(TestModel::class.java)
          .findFirst()
          ?.let {
            emitter.onNext(it.toVo())
          }
        emitter.onComplete()
      }
    }
  }
}