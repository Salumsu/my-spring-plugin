package com.salumsu.classDefinition

enum class ClassType {
    INTERFACE { override fun toString() = "interface" },
    CLASS { override fun toString() = "class" },
}

class ClassDefinition (
    var name: String,
    var accessModifier: AccessModifier,
    var classType: ClassType = ClassType.CLASS,
    var types: MutableList<ClassDefinition> = mutableListOf(),
    val extending: ClassDefinition? = null,
    val implementing: MutableList<ClassDefinition> = mutableListOf(),
    val annotations: MutableList<Annotation> = mutableListOf(),
    val fields: MutableList<ClassField> = mutableListOf(),
    val constructors: MutableList<Constructor> = mutableListOf(),
) {
    var indent: Int = 0

    fun setIndent(indent: Int): ClassDefinition {
        this.indent = indent
        return this
    }

    fun instanceName (): String {
        return name.replaceFirstChar { it.lowercase() }
    }

    override fun toString(): String {
        var result = annotations.joinToString("\n")
        result += "\n$accessModifier $classType ${this.typeParameter()}"

        if (extending != null) {
            result += " extends ${extending.typeParameter()}"
        }

        if (implementing.isNotEmpty()) {
            val implementingStr = implementing.joinToString(", ") { it.typeParameter() }
            result += " implementing $implementingStr"
        }

        result += " {\n"

        if (fields.isNotEmpty()) {
            result += fields.map { it.setIndent(1) }.joinToString("\n")
            result += "\n"
        }

        if (constructors.isNotEmpty()) {
            result += constructors.map { it.setIndent(1) }.joinToString("\n")
            result += "\n"
        }

        result += "\n}"
        return result.prependIndent("\t".repeat(indent)).prependIndent("|")
    }

    fun typeParameter(): String {
        var result = name
        if (types.isNotEmpty()) {
            result += "<"

            result += types.joinToString(", ") { it.typeParameter() }
            result += ">"
        }

        return result
    }
}