package com.salumsu.lib


import com.salumsu.classDefinition.ClassDefinition
import com.salumsu.classDefinition.Import
import java.io.File

class TemplateProcessor (
    val packageName: String,
    val mainJavaSrcDir: File,
    val classDefinition: ClassDefinition,
    val imports: MutableList<Import> = mutableListOf()
) {
    override fun toString(): String {
        return """
            |package $packageName;
            |
            |${imports.joinToString("\n")}
            |
            $classDefinition
            |
        """.trimMargin()
    }

    fun getFile(): File {
        val outputDir = File(mainJavaSrcDir, packageName.replace(".", File.separator))
        outputDir.mkdirs()

        val file = File(outputDir, "${classDefinition.name}.java")

        return file;
    }

    fun writeToFile(file: File = getFile()) {
        file.createNewFile()

        file.writeText(this.toString())

        println("Successfully created class to ${file.toURI()}")
    }
}
