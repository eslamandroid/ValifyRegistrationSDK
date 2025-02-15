package com.valify.registration.data.di

import com.valify.registration.data.datasources.local.dao.ImageDao
import com.valify.registration.data.datasources.local.dao.UserDao
import com.valify.registration.data.repository.ImageRepositoryImpl
import com.valify.registration.data.repository.UserRepositoryImpl
import com.valify.registration.domain.repository.ImageRepository
import com.valify.registration.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
    ): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        imageDao: ImageDao,
    ): ImageRepository {
        return ImageRepositoryImpl(imageDao)
    }

}