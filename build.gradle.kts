plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("myPlugin") {
            id = "com.salumsu.my-spring-plugin"
            implementationClass = "com.salumsu.MySpringPlugin"
        }
    }
}
