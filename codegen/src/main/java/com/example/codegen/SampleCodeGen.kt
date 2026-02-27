package com.example.codegen

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class SampleCodeGen {
    private val userDir = System.getProperty("user.dir")
    private val sourceJsonFile = "SampleCodeGen.json"
    private val sourceJsonFilePath = "src/main/java/com/example/codegen/property/$sourceJsonFile"
    private val targetKtFile = "GeneratedBySampleCodeGen.kt"
    private val targetKtFilePath = userDir.plus("/src/main/java/com/example/codegen/main/$targetKtFile")

    private fun getPropertyJson(): JSONObject {
        try {
            val file = File(sourceJsonFilePath)
            return JSONObject(file.readText())
        } catch (_: Exception) {
            throw Exception("There is an unexpected exception while initialising the property file.")
        }
    }

    fun execute() {
        println("---------- Executing Sample CodeGen --------------")
        val jsonData = getPropertyJson()
        try {
            val file = File(targetKtFilePath)
            val data = """
                |package com.example.codegen.main
                |
                |${buildSealedClasses(jsonData)}
            """.trimMargin()
            file.writeText(data)
            println("---------- Completed Sample CodeGen --------------")
        } catch (exp: Exception) {
            exp.printStackTrace()
            throw Exception("There is an unexpected exception while executing the codegen.")
        }
    }

    private fun buildSealedClasses(jsonObject: JSONObject) = buildString {
        val sealedClasses = jsonObject.getJSONArray("sealed_classes")
        for (i in 0 until sealedClasses.length()) {
            val sealedClass = sealedClasses.getJSONObject(i)
            append(buildSealedClass(sealedClass))
            append("\n\n")
        }
        // remove trailing blank lines at end of file
        trimEnd()
    }

    private fun buildSealedClass(sealedClass: JSONObject): String = buildString {
        val className = sealedClass.getString("class_name")
        val params = sealedClass.getJsonArrayOrNull("params")
        val superClasses = sealedClass.getJsonArrayOrNull("super_classes")
        val companionObject = sealedClass.getJsonObjectOrNull("companion_object")

        // ── Class signature
        append("sealed class $className(")
        if (params != null) append(buildTypeParams(params))
        append(")")

        // ── Superclass
        if (superClasses != null) {
            append(buildSuperClasses(superClasses))
        }

        // ── CompanionObject , Functions, Properties etc inside class body
        if (companionObject != null) {
            append(" {\n")
            append(buildCompanionObject(companionObject))
            append("}")
        }
    }

    private fun buildTypeParams(params: JSONArray): String = buildString {
        params.forEach { param ->
            if (param is JSONObject) {
                val isVararg = param.getBooleanOrNull("is_vararg") ?: false
                val mutability = param.getStringOrNull("mutability")
                val name = param.getString("name")
                val type = param.getString("type")

                if (isVararg) {
                    append("vararg ")
                }
                else if (mutability != null){
                    param.getStringOrNull("access_modifier")?.let { append("$it ") }
                    append("$mutability ")
                }
                append("$name: $type, ")
            }
        }
    }.removeSuffix(", ")

    private fun buildValueParams(params: JSONArray): String = buildString {
        params.forEach { param ->
            if (param is JSONObject) {
                val paramName = param.getString("name")
                val value = param.getString("value")
                append("$paramName = $value, ")
            }
        }
    }.removeSuffix(", ")

    private fun buildSuperClasses(superClasses: JSONArray): String = buildString {
        for (i in 0 until superClasses.length()) {
            val superClass = superClasses.getJSONObject(i)
            val superClassName = superClass.getString("name")
            val superParams = superClass.getJsonArrayOrNull("params")

            // `: SuperClass(valueArgs)`
            append(" : $superClassName(")
            if (superParams != null) append(buildValueParams(superParams))
            append(")")
        }
    }

    private fun buildCompanionObject(companion: JSONObject): String = buildString {
        val properties = companion.getJsonArrayOrNull("properties")
        val functions = companion.getJsonArrayOrNull("functions")

        append("    companion object {\n")
        if (properties != null) {
            append(buildCompanionProperties(properties))
        }

        // ── blank line between properties and functions for readability
        if (properties != null && functions != null) append("\n")

//        if (functions != null) {
//            append(buildCompanionFunctions(functions))
//        }
        append("    }\n")
    }

    private fun buildCompanionProperties(properties: JSONArray): String = buildString {
        properties.forEach { item ->
            if (item is JSONObject) {
                val accessModifier = item.getStringOrNull("access_modifier")
                val isConst = item.getBooleanOrNull("is_const") ?: false
                val mutability = item.getStringOrNull("mutability") ?: "val"
                val name = item.getString("name")
                val type = item.getString("type")
                val value = item.getString("value")

                // builds: private const val NAME: Type = value
                append("        ")
                accessModifier?.let { append("$it ") }
                if (isConst) append("const ")
                append("$mutability $name: $type = $value\n")
            }
        }
    }
}

fun JSONObject.getJsonObjectOrNull(key: String): JSONObject? {
    return if (this.has(key)) this.getJSONObject(key) else null
}

fun JSONObject.getJsonArrayOrNull(key: String): JSONArray? {
    return if (this.has(key)) this.getJSONArray(key) else null
}

fun JSONObject.getStringOrNull(key: String): String? {
    return if (this.has(key)) this.getString(key) else null
}

fun JSONObject.getBooleanOrNull(key: String): Boolean? {
    return if (this.has(key)) this.getBoolean(key) else null
}