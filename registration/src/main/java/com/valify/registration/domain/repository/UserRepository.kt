package com.valify.registration.domain.repository

import com.valify.registration.domain.model.UserModel
import com.valify.registration.domain.utils.ResultSource

interface UserRepository {
    suspend fun saveUser(user: UserModel): ResultSource<Boolean>
}