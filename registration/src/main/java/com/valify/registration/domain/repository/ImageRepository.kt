package com.valify.registration.domain.repository

import com.valify.registration.domain.utils.ResultSource


interface ImageRepository {

    suspend fun saveCapturedImage(image:String): ResultSource<Boolean>

}