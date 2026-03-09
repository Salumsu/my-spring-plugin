package com.salumsu.tasks

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class MakeController : BaseTask() {
    @get:Option(option = "name", description = "Name of the service to generate")
    @get:Input
    abstract val serviceName: Property<String>

    @get:Option(option = "lombok", description = "Add Lombok annotations")
    @get:Input
    @get:Optional
    abstract val lombok: Property<Boolean>

    @get:Option(option = "service", description = "Class path of the service class from base package")
    @get:Input
    @get:Optional
    abstract val serviceClass: Property<String>

    @get:Option(option = "endpoint", description = "The class base endpoint")
    @get:Input
    @get:Optional
    abstract val endpoint: Property<String>

    @TaskAction
    fun generate() {
        val packageName = getPackageName(extension.controllerPath)

        val file = getFile(packageName, serviceName.get()) ?: return

        val annotations = buildList {
            add("@RestController")
            if (lombok.getOrElse(false)) add("@RequiredArgsConstructor")
            if (endpoint.getOrElse("") != "") add("@RequestMapping(\"${endpoint.get()}\")")
        }.joinToString("\n")

        val isServiceClassPresent = serviceClass.getOrElse("") != "";
        val serviceClass = if (isServiceClassPresent) getClassName(serviceClass.get()) else ""
        val imports = buildList {
            add("import org.springframework.web.bind.annotation.*;")
            if (isServiceClassPresent) add("import ${extension.basePackage}.${serviceClass};")
            if (lombok.getOrElse(false)) add("import lombok.RequiredArgsConstructor;")
        }.joinToString("\n")

        val serviceVal = if (isServiceClassPresent) "private final $serviceClass ${variabelize(serviceClass)};" else ""

        file.writeText("""
            |package $packageName;
            |
            |$imports
            |
            |$annotations
            |public class ${serviceName.get()} {
            |   $serviceVal
            |}
        """.trimMargin())
    }
}