package com.example.codegen

fun main(vararg args: String) {
    val type = args[0]

    when (type) {
        "mySampleTask" -> SampleCodeGen().execute()
        "UiAutomationIdentifiersTask" -> UiAutomationIdentifiersCodeGen().execute()
        else -> println("No matching task found for type: $type")
    }
}