plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "com.github.Kajabi"
    version = System.getenv("VERSION") ?: "1.0.0-mentions-alpha01"
}

