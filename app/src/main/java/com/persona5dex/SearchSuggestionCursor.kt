package com.persona5dex

import android.app.SearchManager
import android.database.AbstractCursor
import android.database.Cursor
import android.provider.BaseColumns
import java.lang.UnsupportedOperationException

class SearchSuggestionCursor(private val databaseSearchCursor: Cursor) : AbstractCursor() {

    override fun getLong(column: Int): Long =
            position.toLong()

    override fun getCount(): Int =
            databaseSearchCursor.count

    override fun getColumnNames(): Array<String> =
            arrayOf(
                    BaseColumns._ID,
                    SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_TEXT_2,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                    SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
            )

    override fun getShort(column: Int): Short = throw UnsupportedOperationException()
    override fun getFloat(column: Int): Float = throw UnsupportedOperationException()
    override fun getDouble(column: Int): Double = throw UnsupportedOperationException()
    override fun getInt(column: Int): Int = throw UnsupportedOperationException()

    override fun isNull(column: Int): Boolean = false

    override fun getString(column: Int): String =
            column.getSearchCursorIndex().let {
                moveDatabaseCursorToPosition()
                databaseSearchCursor.getString(it)
            }

    private fun Int.mapPersonaColumnToSearchColumn(): String =
            when (val columnName = getColumnName(this)) {
                SearchManager.SUGGEST_COLUMN_TEXT_1 -> "name"
                SearchManager.SUGGEST_COLUMN_TEXT_2 -> "lineTwo"
                SearchManager.SUGGEST_COLUMN_INTENT_DATA -> "id"
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA -> "type"
                else -> error("persona cursor doesn't have column match for column name: $columnName")
            }

    private fun Int.getSearchCursorIndex() =
            this.mapPersonaColumnToSearchColumn().let {
                databaseSearchCursor.getColumnIndex(it)
            }

    private fun moveDatabaseCursorToPosition() =
            databaseSearchCursor.moveToPosition(position)
}