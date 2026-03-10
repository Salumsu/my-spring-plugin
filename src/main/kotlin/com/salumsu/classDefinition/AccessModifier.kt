package com.salumsu.classDefinition

enum class AccessModifier {
    PRIVATE { override fun toString() = "private" },
    PROTECTED { override fun toString() = "protected" },
    PUBLIC { override fun toString() = "public" },
}
