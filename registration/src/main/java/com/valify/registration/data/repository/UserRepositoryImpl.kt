package com.valify.registration.data.repository

import android.database.sqlite.SQLiteException
import com.valify.registration.data.datasources.local.dao.UserDao
import com.valify.registration.data.datasources.local.entity.toEntity
import com.valify.registration.domain.model.UserModel
import com.valify.registration.domain.repository.UserRepository
import com.valify.registration.domain.utils.AppError
import com.valify.registration.domain.utils.ResultSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun saveUser(user: UserModel): ResultSource<Boolean> = withContext(dispatcher) {
        kotlin.runCatching {
            userDao.insertUser(user.toEntity())
            ResultSource.Success(true)
        }.getOrElse {
            if (it is SQLiteException) {
                ResultSource.Error(AppError.DatabaseError)
            } else {
                ResultSource.Error(AppError.UnknownError)
            }
        }
    }
}