package com.salumsu.classDefinition

class AnnotationParam (
    var name: String,
    var value: String
) {
    override fun toString(): String {
        return "$name = $value"
    }
}

class AnnotationBuilder(private val name: String) {
    private val params = mutableListOf<AnnotationParam>()

    fun param(name: String, value: String) {
        params.add(AnnotationParam(name, value))
    }

    fun build() = Annotation(name, params)
}

class Annotation (
    var name: String,
    var params: MutableList<AnnotationParam> = mutableListOf(),
) {
    override fun toString(): String {
        var result = "@$name"

        if (params.isNotEmpty()) {
            result += "(${params.joinToString(", ")})"
        }

        return result
    }
}

fun annotation(name: String, block: AnnotationBuilder.() -> Unit = {}): Annotation {
    return AnnotationBuilder(name).apply(block).build()
}