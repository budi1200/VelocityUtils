plugins {
    kotlin("jvm") version "1.6.20-M1"
    kotlin("kapt") version "1.6.20-M1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.kyori.blossom") version "1.3.0"
}

group = "si.budimir"
version = "1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

repositories {
    mavenCentral()
    maven(url = uri("https://nexus.velocitypowered.com/repository/maven-public/"))
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    // Velocity
    compileOnly("com.velocitypowered:velocity-api:3.0.1")
    kapt("com.velocitypowered:velocity-api:3.0.1")

    // Configurate
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
    implementation("net.kyori:adventure-serializer-configurate4:4.9.3")

    // Adventure
    implementation("net.kyori:adventure-extra-kotlin:4.9.3")
    implementation("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")
}

tasks.shadowJar {
    // This makes it shadow only stuff with "implementation"
    project.configurations.implementation.get().isCanBeResolved = true
    configurations = mutableListOf(project.configurations.implementation.get()) as List<FileCollection>?

    relocate(
        "net.kyori.adventure.text.minimessage",
        "si.budimir.velocityutils.libs.net.kyori.adventure.text.minimessage"
    )
    relocate("org.spongepowered", "si.budimir.velocityutils.libs.org.spongepowered")
}

blossom {
    val file = "src/main/kotlin/si/budimir/velocityutils/enums/Constants.kt"
    mapOf(
        "PLUGIN_NAME" to rootProject.name,
        "PLUGIN_VERSION" to project.version
    ).forEach { (k, v) ->
        replaceToken("{$k}", v, file)
    }
}

task("buildAndCopy") {
    dependsOn("shadowJar")

    doLast {
        copy {
            from("build/libs/VelocityUtils-" + project.version + "-all.jar")
            into("../01-proxy/plugins/")
        }
    }
}