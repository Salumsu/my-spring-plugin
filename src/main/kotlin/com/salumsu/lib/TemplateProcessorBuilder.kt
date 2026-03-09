package com.salumsu.lib

import java.io.File

class TemplateProcessorBuilder (
    private val packageName: String,
    private val className: String,
    private val mainJavaSrcDir: File,
) {
    private var isClass: Boolean = true
    private var extending: String? = null
    private val implementing: MutableList<String> = mutableListOf()
    private val imports: MutableList<Import> = mutableListOf()
    private val annotations: MutableList<Annotation> = mutableListOf()
    private val fields: MutableList<ClassField> = mutableListOf()

    fun isInterface() {
        isClass = false
    }

    fun extend(className: String) {
        extending = className
    }

    fun implement(className: String) {
        implementing.add(className)
    }

    fun import(packageName: String, block: ImportBuilder.() -> Unit = {}) {
        imports.add(importPackage(packageName, block))
    }

    fun annotate(name: String, block: AnnotationBuilder.() -> Unit = {}) {
        annotations.add(annotation(name, block))
    }

    fun field(type: String, name: String, block: ClassFieldBuilder.() -> Unit = {}) {
        fields.add(classField(type, name, block))
    }

    fun build() = TemplateProcessor(packageName, className, mainJavaSrcDir, isClass, extending, implementing, imports, annotations, fields)
}

fun template(packageName: String, className: String, mainJavaSrcDir: File, block: TemplateProcessorBuilder.() -> Unit = {}): TemplateProcessor {
    return TemplateProcessorBuilder(packageName, className, mainJavaSrcDir).apply(block).build()
}
