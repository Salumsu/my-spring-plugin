package com.salumsu

import com.salumsu.tasks.MakeController
import com.salumsu.tasks.MakeEntity
import com.salumsu.tasks.MakeRepository
import com.salumsu.tasks.MakeService
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class MySpringPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("mySpringPlugin", MySpringPluginExtension::class.java)

        project.afterEvaluate {
            if (extension.basePackage.isEmpty()) {
                throw GradleException("mySpringPlugin: 'basePackage' is required")
            }
        }

        project.tasks.register("makeEntity", MakeEntity::class.java) {
            this.extension = extension
        }

        project.tasks.register("makeService", MakeService::class.java) {
            this.extension = extension
        }

        project.tasks.register("makeRepository", MakeRepository::class.java) {
            this.extension = extension
        }

        project.tasks.register("makeController", MakeController::class.java) {
            this.extension = extension
        }
    }
}