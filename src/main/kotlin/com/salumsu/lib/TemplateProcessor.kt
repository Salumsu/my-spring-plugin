package com.salumsu.lib

import java.io.File

class TemplateProcessor (
    val packageName: String,
    val className: String,
    val mainJavaSrcDir: File,
    val isClass: Boolean = true, // false = interface
    val extending: String? = null,
    val implementing: MutableList<String> = mutableListOf(),
    val imports: MutableList<Import> = mutableListOf(),
    val annotations: MutableList<Annotation> = mutableListOf(),
    val fields: MutableList<ClassField> = mutableListOf(),
) {
    override fun toString(): String {
        var declaration = "public ${if (isClass) "class" else "interface"} $className"

        if (extending != null) declaration += " extends $extending"
        if (implementing.isNotEmpty()) declaration += " implements ${implementing.joinToString(", ")}"

        return """
            |package $packageName;
            |
            |${imports.joinToString("\n")}
            |
            |${annotations.joinToString("\n")}
            |$declaration {
            ${fields.joinToString("\n\n")}
            |}
        """.trimMargin()
    }

    fun getFile(): File {
        val outputDir = File(mainJavaSrcDir, packageName.replace(".", File.separator))
        outputDir.mkdirs()

        val file = File(outputDir, "$className.java")

        return file;
    }

    fun writeToFile(file: File = getFile()) {
        file.createNewFile()

        file.writeText(this.toString())
    }
}
