package com.salumsu.classDefinition

class ImportBuilder (private val basePackage: String) {
    private val items = mutableListOf<String>()

    fun item (name: String) {
        items.add(name)
    }

    fun build() = Import(basePackage, items)
}

class Import (
    var basePackage: String,
    var items: List<String> = emptyList(),
){
    override fun toString (): String {
        if (items.isEmpty()) {
            return "import $basePackage;"
        }

        var result = "";
        result += items.joinToString("\n") { "import $basePackage.$it;" }

        return result;
    }
}

fun importPackage(basePackage: String, block: ImportBuilder.() -> Unit = {}): Import {
    return ImportBuilder(basePackage).apply(block).build()
}