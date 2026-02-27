package com.example.codegen

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class UiAutomationIdentifiersCodeGen {
    private val userDir = System.getProperty("user.dir")
    private val sourceJsonFilePath = userDir.replace("codegen", "app/src/main/java/com/example/firstapplication/util/UiAutomationIdentifiers.json")
    private val targetKtFile = "UiAutomationIdentifiers.kt"
    private val targetKtFilePath = userDir.plus("/src/main/java/com/example/codegen/main/$targetKtFile")

    private fun getPropertyJson(): JSONObject {
        try {
            val file = File(sourceJsonFilePath)
            return JSONObject(file.readText())
        } catch (_: java.lang.Exception) {
            throw Exception("There is an unexpected exception while initialising the property file.")
        }
    }

    fun execute() {
        println("---------- Executing Sample CodeGen $targetKtFilePath, $sourceJsonFilePath --------------")
        val jsonData = getPropertyJson()
        try {
            val file = File(targetKtFilePath)
            val data = """
                |package com.example.codegen.main
                |
                |${buildContent(jsonData)}
            """.trimMargin()
            file.writeText(data)
            println("---------- Completed Sample $targetKtFilePath --------------")
        } catch (exp: Exception) {
            exp.printStackTrace()
            throw Exception("There is an unexpected exception while executing the codegen.")
        }
    }

    private fun buildContent(json: JSONObject): String = buildString {
        // Sealed base class (always first)
        val sealedClass = json.getJSONObject("sealed_class")
        append(buildSealedClass(sealedClass))
        append("\n\n")

        // All subclasses (object or class based on param count)
        val subClasses = json.getJSONArray("sub_classes")
        append(buildSubClasses(subClasses, sealedClass.getString("name")))
    }

    private fun buildSealedClass(sealedClass: JSONObject): String = buildString {
        val className = sealedClass.getString("name")
        val params = sealedClass.getJsonArrayOrNull("params")
        val body = sealedClass.getStringOrNull("body")

        append("sealed class $className(")
        if (params != null) append(buildTypeParams(params))
        append(")")

        if (body != null) {
            append(" {\n")
            append("    $body\n")
            append("}")
        }
    }


    private fun buildSubClasses(subClasses: JSONArray, sealedClassName: String): String = buildString {
        for (i in 0 until subClasses.length()) {
            val subClass = subClasses.getJSONObject(i)
            append(buildSubClass(subClass, sealedClassName))
            append("\n\n")
        }
        trimEnd()
    }

    private fun buildSubClass(subClass: JSONObject, superClassName: String): String = buildString {
        val name = subClass.getString("name")
        val staticKey = subClass.getString("static_key")
        val params = subClass.getJSONArray("params")
        val hasParams = params.length() > 0

        if (hasParams) {
            // class — params are constructor inputs, no val/var — just passed to super
            append("class $name(")
            append(buildTypeParams(params))
            append(")")
        } else {
            // object — zero params rule
            append("object $name")
        }

        // super class — "StaticKey" + all param names spread into super call
        append(" : $superClassName(")
        append(buildSuperCallParams(staticKey, params))
        append(")")
    }

    /**
     * Builds typed params
     * Handles val/var, vararg, access modifiers.
     * e.g: val staticKey: String, vararg values: String
     */
    private fun buildTypeParams(params: JSONArray): String = buildString {
        for (i in 0 until params.length()) {
            val param = params.getJSONObject(i)
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
    }.removeSuffix(", ")

    private fun buildValueParams(params: JSONArray): String = buildString {
        for (i in 0 until params.length()) {
            val param = params.getJSONObject(i)
            val paramName = param.getString("name")
            val value = param.getString("value")
            append("$paramName = $value, ")
        }
    }.removeSuffix(", ")

    private fun buildSuperCallParams(staticKey: String, params: JSONArray): String = buildString {
        // static key is always the first argument — wrapped in quotes
        append("\"$staticKey\"")

        // spread all param names after the static key
        for (i in 0 until params.length()) {
            val param = params.getJSONObject(i)
            append(", ${param.getString("name")}")
        }
    }
}