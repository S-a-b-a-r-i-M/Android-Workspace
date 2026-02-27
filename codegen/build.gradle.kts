
plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.json:json:20240303")
}

tasks.register<JavaExec>("mySampleTask") {
    mainClass.set("com.example.codegen.TaskExecutorKt")
    classpath = sourceSets["main"].runtimeClasspath
    args("mySampleTask")
}

tasks.register<JavaExec>("UiAutomationIdentifiersTask") {
    mainClass.set("com.example.codegen.TaskExecutorKt")
    classpath = sourceSets["main"].runtimeClasspath
    args("UiAutomationIdentifiersTask")
}