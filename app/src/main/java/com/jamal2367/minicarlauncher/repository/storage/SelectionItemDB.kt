package com.jamal2367.minicarlauncher.repository.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jamal2367.minicarlauncher.repository.entities.SelectionItem

@Database(entities = [SelectionItem::class], version = 1)
abstract class SelectionItemDB : RoomDatabase() {
    companion object {
        private const val DB_NAME = "SelectionItem.db"
        private var INSTANCE: SelectionItemDB? = null

        fun getInstance(appContext: Context) = INSTANCE
            ?: Room.databaseBuilder(appContext, SelectionItemDB::class.java, DB_NAME).build()
    }

    abstract fun getSelectionItemsDAO(): SelectionItemDao
}