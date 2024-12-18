package com.example.butterapp.di

import com.example.butterapp.data.remote.connection.ConnectionApi
import com.example.butterapp.data.remote.post.PostApi
import com.example.butterapp.data.remote.user.UserApi
import com.example.butterapp.data.repository.ConnectionRepository
import com.example.butterapp.data.repository.PostRepository
import com.example.butterapp.data.repository.UserRepository
import com.example.cryptocurrencyapp.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostRepository(api: PostApi): PostRepository {
        return PostRepository(api)
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi): UserRepository {
        return UserRepository(api)
    }


    @Provides
    @Singleton
    fun provideConnectionApi(): ConnectionApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConnectionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectionRepository(api: ConnectionApi): ConnectionRepository {
        return ConnectionRepository(api)
    }
}