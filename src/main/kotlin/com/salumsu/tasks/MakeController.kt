package com.salumsu.tasks

import com.salumsu.classDefinition.AccessModifier
import com.salumsu.lib.template
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class MakeController : BaseTask() {
    @get:Option(option = "name", description = "Name of the service to generate")
    @get:Input
    abstract val controllerName: Property<String>

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
        val isServiceClassPresent = serviceClass.getOrElse("") != "";
        val templateProcessor = template(packageName, extension.mainJavaSrcDir) {
            import("org.springframework.web.bind.annotation.*")
            if (isServiceClassPresent) {
                import("${extension.basePackage}.${serviceClass.get()}")
            }
            if (lombok.getOrElse(false)) {
                import("lombok.RequiredArgsConstructor")
            }

            classDef(controllerName.get()) {
                annotate("RestController")
                if (lombok.getOrElse(false)) {
                    annotate("RequiredArgsConstructor")
                }
                if (endpoint.getOrElse("") != "") {
                    annotate("RequestMapping(\"${endpoint.get()}\")")
                }

                field(
                    getClassName(serviceClass.get()),
                    getClassName(variabelize(serviceClass.get()))) {
                    access(AccessModifier.PRIVATE)
                    isFinal()
                }
            }
        }

        createFile(templateProcessor)
    }
}