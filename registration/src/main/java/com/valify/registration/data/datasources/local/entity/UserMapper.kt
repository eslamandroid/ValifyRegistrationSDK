package com.valify.registration.data.datasources.local.entity

import com.valify.registration.domain.model.UserModel


fun UserModel.toEntity(): UserEntity = UserEntity(
    username = username,
    phone = phone,
    email = email,
    password = password,
)