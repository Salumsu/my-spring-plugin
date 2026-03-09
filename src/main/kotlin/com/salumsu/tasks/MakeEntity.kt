package com.salumsu.tasks

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

        val file = getFile(packageName, entityName.get()) ?: return

        val lombokImports = if (lombok.getOrElse(false)) "import lombok.Getter;\nimport lombok.Setter;" else ""
        val lombokAnnotations = if (lombok.getOrElse(false)) "@Getter\n@Setter" else ""
        file.writeText("""
            |package $packageName;
            |
            |import jakarta.persistence.Entity;
            |import jakarta.persistence.Id;
            |import jakarta.persistence.GeneratedValue;
            |import jakarta.persistence.GenerationType;
            |$lombokImports
            |
            |$lombokAnnotations
            |@Entity
            |public class ${entityName.get()} {
            |    @Id
            |    @GeneratedValue(strategy = GenerationType.IDENTITY)
            |    private Long id;
            |}
        """.trimMargin())
    }
}