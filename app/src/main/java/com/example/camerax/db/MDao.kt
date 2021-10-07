package com.example.camerax.ui

import androidx.room.*

@Dao
interface MDao{

    @Query("SELECT * FROM dbmodel")
    fun getAllModel():List<DbModel>

    @Insert
    fun insert(word: DbModel)

    @Query("DELETE FROM dbmodel ")
    fun deleteAllModel()

    @Insert
    fun insertModel(dbModel: DbModel)

    @Delete
    fun deleteWord(word: DbModel)

    @Update
    fun update(word:DbModel)
}