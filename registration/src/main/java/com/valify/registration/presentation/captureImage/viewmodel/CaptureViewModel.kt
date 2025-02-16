package com.valify.registration.presentation.captureImage.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valify.registration.domain.usecases.SaveCapturedImageUseCase
import com.valify.registration.domain.utils.ResultSource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class CaptureViewModel @Inject constructor(
    private val saveCapturedImageUseCase: SaveCapturedImageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaptureState())
    val uiState = _uiState.asStateFlow()
    private val _intent = MutableSharedFlow<CaptureIntent>(extraBufferCapacity = 1)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            _intent.collectLatest {
                when (it) {
                    is CaptureIntent.SaveCaptureImageIntent -> handleSaveCaptureImage(it.image)
                }
            }
        }
    }

    fun sendIntent(intent: CaptureIntent) {
        _intent.tryEmit(intent)
    }

    private fun handleSaveCaptureImage(image: Bitmap) {
        _uiState.update { it.copy(success = false, appFailure = null) }
        viewModelScope.launch {
            when (val result = saveCapturedImageUseCase(image)) {
                is ResultSource.Success -> {
                    _uiState.update { it.copy(success = result.data, appFailure = null) }
                }

                is ResultSource.Error -> {
                    _uiState.update { it.copy(success = false, appFailure = result.exception) }
                }
            }
        }
    }

}