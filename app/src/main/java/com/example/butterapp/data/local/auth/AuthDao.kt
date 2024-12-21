package com.example.butterapp.data.local.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert
    suspend fun insertAuth(auth: AuthEntity)

    @Query("SELECT * FROM authentity LIMIT 1")
    suspend fun getAuth(): AuthEntity

    @Query("DELETE FROM authentity")
    suspend fun clearAll()
}