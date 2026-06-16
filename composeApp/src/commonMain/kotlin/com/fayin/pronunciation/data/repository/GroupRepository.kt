package com.fayin.pronunciation.data.repository

import com.fayin.pronunciation.db.PronunciationDatabase
import com.fayin.pronunciation.platformCurrentTimeMillis

class GroupRepository(private val db: PronunciationDatabase) {
    fun getAll() = db.groupQueries.getAll().executeAsList()

    fun create(title: String): Long {
        val now = platformCurrentTimeMillis()
        val maxOrder = try { db.groupQueries.getAll().executeAsList().maxOfOrNull { it.sortOrder } ?: -1L }
        catch (e: Exception) { -1L }
        db.groupQueries.insert(title, now, maxOrder + 1)
        return db.groupQueries.lastInsertId().executeAsOne()
    }

    fun rename(id: Long, newTitle: String) { db.groupQueries.update(newTitle, id) }
    fun delete(id: Long) { db.entryQueries.deleteByGroup(id); db.groupQueries.deleteById(id) }

    fun shuffle() {
        val groups = db.groupQueries.getAll().executeAsList()
        if (groups.size < 2) return
        for ((i, id) in groups.map { it.id }.shuffled().withIndex()) db.groupQueries.updateSortOrder(i.toLong(), id)
    }
}
