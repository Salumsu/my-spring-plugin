package com.salumsu.classDefinition


class ClassFieldBuilder(
    private val type: String,
    private val name: String,
) {
    private var accessModifier: AccessModifier = AccessModifier.PUBLIC
    private var isStatic: Boolean = false
    private var final: Boolean = false

    private val annotations = mutableListOf<Annotation>()

    fun static() { isStatic = true }

    fun access(modifier: AccessModifier) { accessModifier = modifier }

    fun annotate(name: String, block: AnnotationBuilder.() -> Unit = {}) {
        annotations.add(annotation(name, block))
    }

    fun isFinal() {
        final = true
    }

    fun build() = ClassField(type, name, accessModifier, isStatic, annotations, final)
}


class ClassField (
    var type: String,
    var name: String,
    var accessModifier: AccessModifier = AccessModifier.PUBLIC,
    var isStatic: Boolean = false,
    var annotations: MutableList<Annotation> = mutableListOf(),
    var final: Boolean = false,
){
    var indent: Int = 0;

    fun setIndent(indent: Int): ClassField {
        this.indent = indent
        return this
    }

    override fun toString(): String {
        var result = ""

        if (annotations.isNotEmpty()) {
            result += annotations.joinToString("\n") { "$it" } + "\n"
        }

        result += "$accessModifier"

        if (isStatic) {
            result += " static"
        }

        if (final) {
            result += " final"
        }

        result += " $type $name;"

        return result.prependIndent("\t".repeat(indent))
    }
}

fun classField(type: String, name: String, block: ClassFieldBuilder.() -> Unit = {}): ClassField {
    return ClassFieldBuilder(type, name).apply(block).build()
}