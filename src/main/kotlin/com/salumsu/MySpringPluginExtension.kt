package com.salumsu

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import java.io.File

abstract class MySpringPluginExtension (private val project: Project) {
    var basePackage: String = ""
    var entityPath: String = "entity"
    var servicePath: String = "services"
    var repositoryPath: String = "repositories"
    var controllerPath: String = "controllers"

    val mainJavaSrcDir: File
        get() = project.extensions
            .getByType(SourceSetContainer::class.java)
            .getByName("main")
            .java.srcDirs.first()
}