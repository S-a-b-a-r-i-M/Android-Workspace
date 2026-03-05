package com.example.newcodegen

import org.gradle.api.Plugin
import org.gradle.api.Project

class CodeGenPlugin : Plugin<Project> {
    override fun apply(target: Project?) {
        if (target == null) return
        // Register the extension (this creates the codeGen { } DSL block)
        val extension = target.extensions.create("codeGen", CodeGenExtension::class.java)

        // Register the CodeGen task
        val codeGenTask = target.tasks.register("generateUiIdentifiers", CodeGenTask::class.java) {
            it.sourceJsonFilePath.set(target.provider { extension.sourceJsonFilePath })
            it.targetKtFilePath.set(target.provider { extension.targetKtFilePath })
        }

        // Hook into build lifecycle
        target.plugins.withId("com.android.application") {
            // consumer is an Android app module
            target.tasks.named("preBuild") { it.dependsOn(codeGenTask) }
        }

        target.plugins.withId("com.android.library") {
            // consumer is an Android library module
            target.tasks.named("preBuild") { it.dependsOn(codeGenTask) }
        }
    }
}