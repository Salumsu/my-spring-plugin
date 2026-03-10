package com.salumsu.lib

import com.salumsu.classDefinition.AccessModifier
import com.salumsu.classDefinition.Annotation
import com.salumsu.classDefinition.AnnotationBuilder
import com.salumsu.classDefinition.ClassBuilder
import com.salumsu.classDefinition.ClassDefinition
import com.salumsu.classDefinition.ClassField
import com.salumsu.classDefinition.ClassFieldBuilder
import com.salumsu.classDefinition.Import
import com.salumsu.classDefinition.ImportBuilder
import com.salumsu.classDefinition.annotation
import com.salumsu.classDefinition.buildClass
import com.salumsu.classDefinition.classField
import com.salumsu.classDefinition.importPackage
import java.io.File

class TemplateProcessorBuilder (
    private val packageName: String,
    private val mainJavaSrcDir: File,
) {
    private val imports: MutableList<Import> = mutableListOf()
    private var classDefinition: ClassDefinition? = null;

    fun import(packageName: String, block: ImportBuilder.() -> Unit = {}) {
        imports.add(importPackage(packageName, block))
    }

    fun classDef(name: String, accessModifier: AccessModifier = AccessModifier.PUBLIC, block: ClassBuilder.() -> Unit = {}): ClassDefinition {
        classDefinition = buildClass(name, accessModifier, block)
        return classDefinition!!
    }

    fun build(): TemplateProcessor {
        requireNotNull(classDefinition) { "classDefinition must be defined" }
        return TemplateProcessor(packageName, mainJavaSrcDir, classDefinition!!, imports)
    }
}

fun template(packageName: String, mainJavaSrcDir: File, block: TemplateProcessorBuilder.() -> Unit = {}): TemplateProcessor {
    return TemplateProcessorBuilder(packageName, mainJavaSrcDir).apply(block).build()
}
