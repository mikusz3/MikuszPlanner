package com.mikusz3.mikuszplanner.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mikusz3.mikuszplanner.data.model.SubTask
import com.mikusz3.mikuszplanner.data.model.Task

@Database(
    entities = [Task::class, SubTask::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mikusz_planner_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
