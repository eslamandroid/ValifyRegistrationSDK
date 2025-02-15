package com.valify.registration.data.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.valify.registration.data.datasources.local.AppDatabase
import com.valify.registration.data.datasources.local.dao.UserDao
import com.valify.registration.data.utils.generateSecretKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(generateSecretKey(context).toCharArray())
        val sqlCipherSupportFactory: SupportSQLiteOpenHelper.Factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "registration_db.db"
        ).openHelperFactory(sqlCipherSupportFactory).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

}