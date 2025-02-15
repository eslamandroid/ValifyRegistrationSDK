package com.valify.registration.data.repository

import android.database.sqlite.SQLiteException
import com.valify.registration.data.datasources.local.dao.ImageDao
import com.valify.registration.data.datasources.local.entity.ImageEntity
import com.valify.registration.domain.repository.ImageRepository
import com.valify.registration.domain.utils.AppError
import com.valify.registration.domain.utils.ResultSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ImageRepository {
    override suspend fun saveCapturedImage(image: String): ResultSource<Boolean> = withContext(dispatcher) {
        kotlin.runCatching {
            imageDao.insertImage(ImageEntity(imageData = image))
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