import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("Compose Rich Editor with @ Mentions")
            description.set("A Compose multiplatform rich text editor with advanced @ mention functionality and Lexical JSON compatibility.")
            url.set("https://github.com/Kajabi/Android-Kajabi-Rich-Text")

            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://opensource.org/licenses/Apache-2.0")
                }
            }
            issueManagement {
                system.set("Github")
                url.set("https://github.com/Kajabi/Android-Kajabi-Rich-Text/issues")
            }
            scm {
                connection.set("https://github.com/Kajabi/Android-Kajabi-Rich-Text.git")
                url.set("https://github.com/Kajabi/Android-Kajabi-Rich-Text")
            }
            developers {
                developer {
                    id.set("PGMacDesign")
                    name.set("Patrick MacDowell")
                    email.set("patrick.macdowell@kajabi.com")
                }
            }
        }
    }
}

// For JitPack, signing is optional and can cause issues
// Comment out the signing configuration for JitPack
/*
signing {
    useInMemoryPgpKeys(
        System.getenv("OSSRH_GPG_SECRET_KEY_ID"),
        System.getenv("OSSRH_GPG_SECRET_KEY"),
        System.getenv("OSSRH_GPG_SECRET_KEY_PASSWORD"),
    )
    sign(publishing.publications)
}

// TODO: remove after https://youtrack.jetbrains.com/issue/KT-46466 is fixed
project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
}
*/