package com.example.newcodegen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class CodeGenTask : DefaultTask() {

    @get:Input
    abstract val sourceJsonFilePath: Property<String>
    @get:Input
    abstract val targetKtFilePath: Property<String>

    @TaskAction
    fun execute() {
        val jsonFilePath = sourceJsonFilePath.get()
        val ktFilePath = targetKtFilePath.get()

        if (jsonFilePath.isEmpty() || ktFilePath.isEmpty()) {
            println("CodeGen Error: sourceJsonFilePath, targetKtFilePath is not set")
            return
        }

        println("▶ ▶ ▶ CodeGen started...")
        println("  JSON  → $jsonFilePath")
        println("  Output→ $ktFilePath")

        UiAutomationIdentifiersCodeGen(jsonFilePath, ktFilePath).execute()
        println("✅ CodeGen complete → $ktFilePath")
    }
}