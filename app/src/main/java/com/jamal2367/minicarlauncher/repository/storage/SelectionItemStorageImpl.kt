package com.jamal2367.minicarlauncher.repository.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamal2367.minicarlauncher.repository.SelectionItemStorage
import com.jamal2367.minicarlauncher.repository.entities.SelectionItem
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

@Dao
interface SelectionItemDao {
    @Query("SELECT * FROM selection_item WHERE text_key = :textKey LIMIT 1")
    fun getItemByTextKey(textKey: String): Maybe<SelectionItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveItem(item: SelectionItem): Completable
}

class SelectionItemStorageImpl
@Inject constructor(private val dao: SelectionItemDao) : SelectionItemStorage {

    override fun getSelection(textKey: String) = dao.getItemByTextKey(textKey)

    override fun saveSelection(selectionItem: SelectionItem) = dao.saveItem(selectionItem)
}