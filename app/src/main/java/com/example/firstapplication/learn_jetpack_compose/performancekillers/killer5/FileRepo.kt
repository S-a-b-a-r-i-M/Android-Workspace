package com.example.firstapplication.learn_jetpack_compose.performancekillers.killer5

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FileRepo {

    suspend fun loadUserDataBad(userId: String) : String {
        delay(100) // Some async operation

        val fileContent = simulateBlockingFileReadBad()

        return processUserDataBad(fileContent, userId)
    }

    private fun simulateBlockingFileReadBad() : String {
        val data = StringBuilder()
        repeat(10_00_000) { index ->
            data.append("Line $index: User data \n")
        }
        return data.toString()
    }

    private fun processUserDataBad(fileContent: String, userId: String) : String {
        var result = ""
        repeat(10_000) {
            result = fileContent.take(100) + userId
        }
        return result
    }

    suspend fun loadUserDataGood(userId: String) : String {
        delay(100) // Some async operation

        val fileContent = simulateBlockingFileReadGood()

        return processUserDataGood(fileContent, userId)
    }

    private suspend fun simulateBlockingFileReadGood() = withContext(Dispatchers.IO) {
        simulateBlockingFileReadBad()
    }

    private suspend fun processUserDataGood(fileContent: String, userId: String) = withContext(Dispatchers.IO) {
        return@withContext processUserDataBad(fileContent, userId)
    }
}