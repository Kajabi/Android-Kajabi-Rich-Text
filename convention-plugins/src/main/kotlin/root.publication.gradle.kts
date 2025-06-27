plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "com.github.Kajabi"
    version = System.getenv("VERSION") ?: "1.2.0"
}

