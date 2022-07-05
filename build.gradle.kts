import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.*

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.dynxsty"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    // JDA & DIH4JDA (Interaction Framework)
    implementation("net.dv8tion:JDA:5.0.0-alpha.13") {
        exclude(module = "opus-java")
    }
    implementation("com.github.DynxstyGIT:DIH4JDA:1.5.5")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // Lombok Annotations
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}
java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "xyz.ratingtool.bot.Bot"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
