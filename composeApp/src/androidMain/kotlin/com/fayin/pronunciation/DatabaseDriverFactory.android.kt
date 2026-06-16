package com.fayin.pronunciation

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.fayin.pronunciation.db.PronunciationDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PronunciationDatabase.Schema, context, "fayin_pronunciation.db")
    }
}
