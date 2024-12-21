package com.example.butterapp.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM userentity LIMIT 1")
    suspend fun getUser(): UserEntity

    @Query("DELETE FROM userentity")
    suspend fun clearAll()
}