plugins {
    kotlin("jvm") version "1.6.20-M1"
    kotlin("kapt") version "1.6.20-M1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.kyori.blossom") version "1.3.0"
}

group = "si.budimir"
version = "1.0.1"

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
    maven {
        name = "papermc"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

val velocityVersion = "3.1.2-SNAPSHOT"
val configurateVersion = "4.1.2"
val adventureVersion = "4.10.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    // Velocity
    compileOnly("com.velocitypowered:velocity-api:$velocityVersion")
    kapt("com.velocitypowered:velocity-api:$velocityVersion")

    // Configurate
    implementation("org.spongepowered:configurate-hocon:$configurateVersion")
    implementation("org.spongepowered:configurate-extra-kotlin:$configurateVersion")
    implementation("net.kyori:adventure-serializer-configurate4:$adventureVersion")

    // Adventure
    implementation("net.kyori:adventure-extra-kotlin:$adventureVersion")
}

tasks.shadowJar {
    // This makes it shadow only stuff with "implementation"
    project.configurations.implementation.get().isCanBeResolved = true
    configurations = mutableListOf(project.configurations.implementation.get()) as List<FileCollection>?

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