package com.example.firstapplication.architectures.mvc.model.db.tables

import com.example.firstapplication.architectures.mvc.model.db.AbstractTable

object NoteTable : AbstractTable {
    const val TABLE_NAME = "t_note"
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_CREATED_AT = "created_at"

    override val createTableQuery = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT NOT NULL,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_CREATED_AT INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))
        )
    """

    override val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
}