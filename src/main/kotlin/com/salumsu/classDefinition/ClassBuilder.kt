package com.salumsu.classDefinition

class ClassBuilder(
    private var name: String,
    private var accessModifier: AccessModifier
) {
    private var classType: ClassType = ClassType.CLASS
    private var extending: ClassDefinition? = null
    private var types: MutableList<ClassDefinition> = mutableListOf()
    private val implementing: MutableList<ClassDefinition> = mutableListOf()
    private val annotations: MutableList<Annotation> = mutableListOf()
    private val fields: MutableList<ClassField> = mutableListOf()
    private val constructors: MutableList<Constructor> = mutableListOf()

    fun isInterface() {
        classType = ClassType.INTERFACE
    }

    fun extend(extending: ClassDefinition) {
        this.extending = extending
    }

    fun implement(implementing: ClassDefinition) {
        this.implementing.add(implementing)
    }

    fun type(newType: ClassDefinition) {
        this.types.add(newType)
    }

    fun annotate(name: String, block: AnnotationBuilder.() -> Unit = {}) {
        annotations.add(annotation(name, block))
    }

    fun field(type: String, name: String, block: ClassFieldBuilder.() -> Unit = {}): ClassField {
        val newField = classField(type, name, block)
        fields.add(newField)
        return newField
    }

    fun construct(block: ConstructorBuilder.() -> Unit = {}): Constructor {
        val newConstructor = constructor(name, block)
        constructors.add(newConstructor)
        return newConstructor
    }

    fun build () = ClassDefinition(name, accessModifier, classType, types, extending, implementing, annotations, fields, constructors)
}

fun buildClass(name: String, accessModifier: AccessModifier, block: ClassBuilder.() -> Unit = {}): ClassDefinition {
    return ClassBuilder(name, accessModifier).apply(block).build()
}