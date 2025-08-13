package com.example.firstapplication.architectures.mvc.model.db.tables

interface AbstractTable {
    val createTableQuery: String
    val dropTableQuery: String
}