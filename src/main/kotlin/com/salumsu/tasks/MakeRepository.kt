package com.salumsu.tasks

import com.salumsu.classDefinition.AccessModifier
import com.salumsu.lib.template
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

        val templateProcessor = template(packageName, extension.mainJavaSrcDir) {
            import("org.springframework.data.jpa.repository.JpaRepository")
            import("${extension.basePackage}.${modelClassPath.get()}")

            classDef(repositoryName.get()) {
                isInterface()
                extend(
                    classDef("JpaRepository") {
                        type(
                            classDef(getClassName(modelClassPath.get()))
                        )

                        type (
                            classDef(modelKeyType.getOrElse("Long"))
                        )
                    }
                )
            }
        }

        createFile(templateProcessor)
    }
}