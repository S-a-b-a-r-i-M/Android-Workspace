package com.example.firstapplication.notes.core

// TODO: confirm the file location.

sealed class Result<out T> {
    class Success<T>(val data: T, val message: String = "") : Result<T>()
    class Error(val message: String, val code: ErrorCode? = null) : Result<Nothing>()
}

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    VALIDATION,
    DUPLICATION,
}

