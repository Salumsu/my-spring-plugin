package com.salumsu.classDefinition

class Constructor (
    val className: String,
    val includeFields: Set<ClassField>
) {
    private var indent: Int = 0

    fun setIndent(indent: Int): Constructor {
        this.indent = indent
        return this
    }

    override fun toString(): String {
        var result = "public $className ("

        if (includeFields.isNotEmpty()) {
            result += includeFields.joinToString(", ") { "${it.type} ${it.name}" }
        }

        result += ") {\n"

        for (field in includeFields) {
            result += "\tthis.${field.name} = ${field.name};\n"
        }

        result += "}"
        return result.prependIndent("\t".repeat(indent))
    }
}

class ConstructorBuilder(private val className: String) {
    private val fields = mutableSetOf<ClassField>()

    fun field(type: String, name: String, block: ClassFieldBuilder.() -> Unit = {}) {
        fields.add(classField(type, name, block))
    }

    fun existingField(classField: ClassField) {
        fields.add(classField)
    }

    fun build() = Constructor(className, fields)
}

fun constructor(className: String, block: ConstructorBuilder.() -> Unit = {}): Constructor {
    return ConstructorBuilder(className).apply(block).build()
}