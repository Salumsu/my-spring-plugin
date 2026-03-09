package com.salumsu.tasks

import com.salumsu.lib.AccessModifier
import com.salumsu.lib.template
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option

abstract class MakeEntity : BaseTask() {
    @get:Option(option = "name", description = "Name of the entity to generate")
    @get:Input
    abstract val entityName: Property<String>

    @get:Option(option = "lombok", description = "Add Lombok annotations")
    @get:Input
    @get:Optional
    abstract val lombok: Property<Boolean>

    @TaskAction
    fun generate() {
        val packageName = getPackageName(extension.entityPath)
        val templateProcessor = template(packageName, entityName.get(), extension.mainJavaSrcDir) {
            import("jakarta.persistence") {
                item("Entity")
                item("Id")
                item("GeneratedValue")
                item("GenerationType")
            }
            annotate("Entity")
            field("Long", "id") {
                access(AccessModifier.PRIVATE)
                annotate("Id")
                annotate("GeneratedValue") {
                    param("strategy", "GenerationType.IDENTITY")
                }
            }
        }

        var file = templateProcessor.getFile()
        if (file.exists() && !overwrite.getOrElse(false)) {
            println("Skipping ${file.name}, already exists. Pass --overwrite to overwrite.")
            return;
        }

        templateProcessor.writeToFile(file)
    }
}