package com.example.camerax.ui

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [DbModel::class] ,version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun appDataBase() : MDao

    companion object{
        @Volatile
        private var instance: AppDataBase? = null

        fun instance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "CameraX").build()
                this.instance = instance
                instance
            }
        }
    }
}