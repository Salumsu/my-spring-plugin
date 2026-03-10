package com.salumsu.tasks

import com.salumsu.classDefinition.AccessModifier
import com.salumsu.classDefinition.ClassField
import com.salumsu.classDefinition.Constructor
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
        val templateProcessor = template(packageName, extension.mainJavaSrcDir) {
            import("jakarta.persistence") {
                item("Entity")
                item("Id")
                item("GeneratedValue")
                item("GenerationType")
            }

            if (lombok.getOrElse(false)) {
                import("lombok") {
                    item("Getter")
                    item("Setter")
                }
            }

            classDef(entityName.get()) {
                if (lombok.getOrElse(false)) {
                    annotate("Getter")
                    annotate("Setter")
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
        }


        createFile(templateProcessor)
    }
}