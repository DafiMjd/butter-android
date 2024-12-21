package com.example.butterapp.di

import android.content.Context
import androidx.room.Room
import com.example.butterapp.data.interceptor.ErrorInterceptor
import com.example.butterapp.data.local.auth.AuthDatabase
import com.example.butterapp.data.local.user.UserDatabase
import com.example.butterapp.data.remote.auth.AuthApi
import com.example.butterapp.data.remote.connection.ConnectionApi
import com.example.butterapp.data.remote.post.PostApi
import com.example.butterapp.data.remote.user.UserApi
import com.example.butterapp.data.repository.AuthRepository
import com.example.butterapp.data.repository.ConnectionRepository
import com.example.butterapp.data.repository.PostRepository
import com.example.butterapp.data.repository.UserRepository
import com.example.cryptocurrencyapp.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val client = OkHttpClient.Builder()

        .addInterceptor(ErrorInterceptor())
        .build()

    private fun <T> provideApi(c: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(c)
    }

    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return provideApi(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return provideApi(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectionApi(): ConnectionApi {
        return provideApi(ConnectionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return provideApi(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostRepository(api: PostApi): PostRepository {
        return PostRepository(api)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi): UserRepository {
        return UserRepository(api)
    }

    @Provides
    @Singleton
    fun provideConnectionRepository(api: ConnectionApi): ConnectionRepository {
        return ConnectionRepository(api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        userApi: UserApi,
        userDatabase: UserDatabase,
        authDatabase: AuthDatabase,
    ): AuthRepository {
        return AuthRepository(authApi, userApi, userDatabase, authDatabase)
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "users.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAuthDatabase(@ApplicationContext context: Context): AuthDatabase {
        return Room.databaseBuilder(
            context,
            AuthDatabase::class.java,
            "auth.db"
        ).build()
    }
}