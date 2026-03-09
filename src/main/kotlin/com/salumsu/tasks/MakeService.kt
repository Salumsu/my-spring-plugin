package com.salumsu.tasks

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class MakeService : BaseTask() {
    @get:Option(option = "name", description = "Name of the service to generate")
    @get:Input
    abstract val serviceName: Property<String>

    @get:Option(option = "lombok", description = "Add Lombok annotations")
    @get:Input
    @get:Optional
    abstract val lombok: Property<Boolean>

    @get:Option(option = "repository", description = "Class path of the repository interface from base package")
    @get:Input
    @get:Optional
    abstract val repositoryInterface: Property<String>

    @TaskAction
    fun generate() {
        val packageName = getPackageName(extension.servicePath)

        val file = getFile(packageName, serviceName.get()) ?: return

        val annotations = buildList {
            add("@Service")
            if (lombok.getOrElse(false)) add("@RequiredArgsConstructor")
        }.joinToString("\n")

        val isRepositoryPresent = repositoryInterface.getOrElse("") != ""
        val repositoryClass = if (isRepositoryPresent) getClassName(repositoryInterface.get()) else ""
        val imports = buildList {
            add("import org.springframework.stereotype.Service;")
            if (isRepositoryPresent) add("import ${extension.basePackage}.${repositoryInterface.get()};")
            if (lombok.getOrElse(false)) add("import lombok.RequiredArgsConstructor;")
        }.joinToString("\n")

        val repositoryVal = if (isRepositoryPresent) "private final $repositoryClass ${variabelize(repositoryClass)};" else ""

        file.writeText("""
            |package $packageName;
            |
            |$imports
            |
            |$annotations
            |public class ${serviceName.get()} {
            |   $repositoryVal
            |}
        """.trimMargin())
    }
}
