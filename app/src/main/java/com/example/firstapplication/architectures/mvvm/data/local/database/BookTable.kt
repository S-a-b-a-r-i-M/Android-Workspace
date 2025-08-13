package com.example.firstapplication.architectures.mvvm.data.local.database

object BookTable {
    const val TABLE_NAME = "t_book"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "name"
    const val COLUMN_AUTHOR = "author"

    const val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_AUTHOR TEXT NOT NULL
        )
    """

    const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}