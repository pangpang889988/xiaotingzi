package com.fayin.pronunciation

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.fayin.pronunciation.db.PronunciationDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(PronunciationDatabase.Schema, "fayin_pronunciation.db")
    }
}
