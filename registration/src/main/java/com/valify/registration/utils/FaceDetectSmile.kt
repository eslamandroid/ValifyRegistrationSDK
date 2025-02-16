package com.valify.registration.utils

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectSmile(private val onSmileDetected: () -> Unit) : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(proxyImage: ImageProxy) {
        val image = proxyImage.image ?: return
        val inputImage = InputImage.fromMediaImage(image, proxyImage.imageInfo.rotationDegrees)
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        ).process(inputImage).addOnSuccessListener { faces ->
            for (face in faces) {
                val smileProb = face.smilingProbability ?: 0.0f
                if (smileProb > 0.8f) {
                    onSmileDetected()
                }
            }
        }.addOnCompleteListener {
            proxyImage.close()

        }
    }
}