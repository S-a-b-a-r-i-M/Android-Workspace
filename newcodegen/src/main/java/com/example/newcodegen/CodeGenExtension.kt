package com.example.newcodegen

/**
 * Consumer-facing configuration.
 * Each consuming module sets these paths in their build.gradle.kts.
 *
 * Usage in consumer:
 * codeGen {
 *     sourceJsonFilePath = "..."
 *     targetKtFilePath = "..."
 * }
 */
open class CodeGenExtension {
    var sourceJsonFilePath: String = ""
    var targetKtFilePath: String = ""
}