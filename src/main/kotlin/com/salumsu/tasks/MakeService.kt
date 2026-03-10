package com.salumsu.tasks

import com.salumsu.classDefinition.AccessModifier
import com.salumsu.lib.template
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
        val isRepositoryPresent = repositoryInterface.getOrElse("") != ""
        val templateProcessor = template(packageName, extension.mainJavaSrcDir) {
            import("org.springframework.stereotype.Service")
            if (isRepositoryPresent) {
                import("${extension.basePackage}.${repositoryInterface.get()}")
            }
            if (lombok.getOrElse(false)) {
                import("lombok.RequiredArgsConstructor")
            }

            classDef(serviceName.get()) {
                annotate("Service")
                if (lombok.getOrElse(false)) {
                    annotate("RequiredArgsConstructor")
                }

                field(
                    getClassName(repositoryInterface.get()),
                    getClassName(variabelize(repositoryInterface.get()))) {
                    access(AccessModifier.PRIVATE)
                    isFinal()
                }
            }
        }

        createFile(templateProcessor)
    }
}
