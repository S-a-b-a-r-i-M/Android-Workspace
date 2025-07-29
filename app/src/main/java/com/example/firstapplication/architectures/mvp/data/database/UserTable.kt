package com.example.firstapplication.architectures.mvp.data.database

object UserTable {
    const val TABLE_NAME = "t_user"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "name"
    const val COLUMN_EMAIL = "email"

    const val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_EMAIL TEXT
        )
    """

    const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}