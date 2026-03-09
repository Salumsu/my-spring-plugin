package com.salumsu.tasks

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class MakeRepository : BaseTask() {
    @get:Option(option = "name", description = "Name of the service to generate")
    @get:Input
    abstract val repositoryName: Property<String>

    @get:Option(option = "model", description = "Class path of the repository model from base package")
    @get:Input
    abstract val modelClassPath : Property<String>

    @get:Option(option = "type", description = "Type of the repository model primary key. default: Long")
    @get:Input
    @get:Optional
    abstract val modelKeyType : Property<String>

    @TaskAction
    fun generate() {
        val packageName = getPackageName(extension.repositoryPath)

        val file = getFile(packageName, repositoryName.get()) ?: return
        val modelImport = "import ${extension.basePackage}.${modelClassPath.get()};"
        val modelClass = getClassName(modelClassPath.get())
        val type = modelKeyType.getOrElse("Long")

        file.writeText("""
            |package $packageName;
            |import org.springframework.data.jpa.repository.JpaRepository;
            |$modelImport
            |
            |public interface ${repositoryName.get()} extends JpaRepository<$modelClass, $type> {
            |}
        """.trimMargin())
    }
}