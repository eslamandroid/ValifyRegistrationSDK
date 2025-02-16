package com.valify.registration.domain.usecases

import android.graphics.Bitmap
import com.valify.registration.domain.repository.ImageRepository
import com.valify.registration.domain.utils.ResultSource
import com.valify.registration.utils.bitmapToBase64
import javax.inject.Inject

internal class SaveCapturedImageUseCase @Inject constructor(private val repository: ImageRepository) {

    suspend operator fun invoke(image: Bitmap): ResultSource<Boolean> =
        repository.saveCapturedImage(image.bitmapToBase64())

}