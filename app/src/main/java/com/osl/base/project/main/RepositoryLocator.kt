package com.osl.base.project.main

import android.content.Context
import com.osl.swrnd.data.source.IDataSource
import com.osl.swrnd.data.source.DataSource
import com.osl.swrnd.data.source.Repository
import com.osl.swrnd.data.source.local.realm.RealmSource
import com.osl.swrnd.data.source.remote.ApiManager
import com.osl.swrnd.domain.repository.IRepository


object RepositoryLocator {

  private var localDataSource: RealmSource? = null

  @Volatile
  var dataRepository: IRepository? = null

//  private val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//      database.execSQL("ALTER TABLE Translation ADD COLUMN category TEXT")
//    }
//  }

  fun provideRepository(context: Context): IRepository {
    synchronized(this) {
      return dataRepository ?: dataRepository ?: createRepository(context)
    }
  }

  private fun createRepository(context: Context): IRepository {
    return Repository(createDataSource(context))
  }

  private fun createDataSource(context: Context): IDataSource {
    val localDataSource = localDataSource ?: RealmSource(context)
    val apiService = ApiManager.getService(context)
    return DataSource(localDataSource, apiService)
  }

//  private fun createDataBase(context: Context): DataBase {
//    val db = Room.databaseBuilder(
//      context.applicationContext,
//      DataBase::class.java, "test.db"
//    )
////      .addMigrations(MIGRATION_1_2)
//      .build()
//    localDataSource = db
//    return db
//  }

//  fun close() {
//    localDataSource?.close()
//  }

  fun clear() {
    synchronized(this) {
//      close()
      dataRepository = null
    }
  }
}