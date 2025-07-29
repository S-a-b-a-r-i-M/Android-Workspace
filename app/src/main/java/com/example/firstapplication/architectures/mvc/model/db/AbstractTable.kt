package com.example.firstapplication.architectures.mvc.model.db

interface AbstractTable {
    val createTableQuery: String
    val dropTableQuery: String
}