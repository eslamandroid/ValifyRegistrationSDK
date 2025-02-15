package com.valify.registration.presentation.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valify.registration.domain.model.UserModel
import com.valify.registration.domain.usecases.SaveUserUseCase
import com.valify.registration.domain.usecases.ValidateUserInputUseCase
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
internal class RegistrationViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val validateUserInputUseCase: ValidateUserInputUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationState())
    val uiState = _uiState.asStateFlow()
    private val _intent = MutableSharedFlow<RegistrationIntent>(extraBufferCapacity = 1)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            _intent.collectLatest { it ->
                when (it) {
                    is RegistrationIntent.SaveUserIntent -> handleSaveUser(it.input)
                    RegistrationIntent.ResetState -> _uiState.update { it.copy(success = false, appFailure = null) }
                }
            }
        }
    }

    fun sendIntent(intent: RegistrationIntent) {
        _intent.tryEmit(intent)
    }

    private fun handleSaveUser(input: UserModel) {
        val validationResult = validateUserInputUseCase(input)
        if (validationResult is ResultSource.Error) {
            _uiState.update { it.copy(loading = false, appFailure = validationResult.exception) }
            return
        }
        _uiState.update { it.copy(loading = true, success = false, appFailure = null) }
        viewModelScope.launch {
            when (val result = saveUserUseCase(input)) {
                is ResultSource.Success -> {
                    _uiState.update { it.copy(loading = false, success = result.data, appFailure = null) }
                }

                is ResultSource.Error -> {
                    _uiState.update { it.copy(loading = false, appFailure = result.exception) }
                }
            }
        }
    }

}