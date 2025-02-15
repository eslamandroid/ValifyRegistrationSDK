package com.valify.registration.domain.usecases

import com.valify.registration.domain.model.UserModel
import com.valify.registration.domain.repository.UserRepository
import com.valify.registration.domain.utils.ResultSource
import javax.inject.Inject

internal class SaveUserUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(user: UserModel): ResultSource<Boolean> = repository.saveUser(user)

}