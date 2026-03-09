package com.salumsu.lib

enum class AccessModifier {
    PRIVATE,
    PROTECTED,
    PUBLIC,
}

class ClassFieldBuilder(
    private val type: String,
    private val name: String,
) {
    var accessModifier: AccessModifier = AccessModifier.PUBLIC
    var isStatic: Boolean = false
    private val annotations = mutableListOf<Annotation>()

    fun static() { isStatic = true }

    fun access(modifier: AccessModifier) { accessModifier = modifier }

    fun annotate(name: String, block: AnnotationBuilder.() -> Unit = {}) {
        annotations.add(annotation(name, block))
    }

    fun build() = ClassField(type, name, accessModifier, isStatic, annotations)
}


class ClassField (
    var type: String,
    var name: String,
    var accessModifier: AccessModifier = AccessModifier.PUBLIC,
    var isStatic: Boolean = false,
    var annotations: MutableList<Annotation> = mutableListOf(),
){
    override fun toString(): String {
        var result = ""

        if (annotations.isNotEmpty()) {
            result += annotations.joinToString("\n") { "|    $it" } + "\n"
        }

        result += "|    " + accessModifier.toString().lowercase()

        if (isStatic) {
            result += " static"
        }

        result += " $type $name;"

        return result
    }
}

fun classField(type: String, name: String, block: ClassFieldBuilder.() -> Unit = {}): ClassField {
    return ClassFieldBuilder(type, name).apply(block).build()
}