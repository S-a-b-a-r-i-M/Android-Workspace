package com.example.firstapplication.notes.data

object NoteTable {
    const val TABLE_NAME = "t_note"
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_IS_COMPLETED = "is_completed" // SQLite doesn't have a native BOOLEAN data type.(0 - false, 1 - true)
    const val COLUMN_CREATED_AT = "created_at"

    val createTableQuery = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT NOT NULL,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_IS_COMPLETED INTEGER DEFAULT 0,
            $COLUMN_CREATED_AT INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))
        )
    """

    val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
}