package com.salumsu.tasks

import com.salumsu.MySpringPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.options.Option
import java.io.File

abstract class BaseTask : DefaultTask() {
    init {
        group = "my spring plugin"
    }

    @get:Internal
    lateinit var extension: MySpringPluginExtension

    @get:Option(option = "path", description = "Overrides default class path")
    @get:Input
    @get:Optional
    abstract val subPath: Property<String>

    @get:Option(option = "overwrite", description = "Overwrite class if it exists")
    @get:Input
    @get:Optional
    abstract val overwrite: Property<Boolean>

    fun getPackageName(defaultPath: String): String {
        return when {
            subPath.isPresent -> "${extension.basePackage}.${subPath.get()}"
            else -> "${extension.basePackage}.${defaultPath}"
        }
    }

    fun getClassName(classPath: String): String {
        return classPath.split(".").last()
    }

    fun variabelize (className: String): String {
        return className.replaceFirstChar { it.lowercase() }
    }

    fun getFile(packageName: String, className: String): File? {
        val outputDir = File(extension.mainJavaSrcDir,packageName.replace(".", File.separator))
        outputDir.mkdirs()

        val file = File(outputDir, "$className.java")
        if (file.exists() && !overwrite.getOrElse(false)) {
            println("Skipping ${file.name}, already exists. Pass --overwrite to overwrite.")
            return null;
        }

        file.createNewFile();

        return file;
    }
}