plugins {
    kotlin("jvm") version "2.2.0"
    `java-gradle-plugin`  // ← required to author a Gradle plugin
}

dependencies {
    implementation("org.json:json:20240303")

    // Gradle API — needed to write Plugin, Task, Extension classes
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("codeGenPlugin") {
            id = "com.example.newcodegen"
            implementationClass = "com.example.newcodegen.CodeGenPlugin"
        }
    }
}