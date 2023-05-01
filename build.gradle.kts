plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    application
    java
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    jacoco
    id("org.sonarqube") version "3.3"
}

repositories {
    mavenCentral()
}

application {
    applicationName = project.name
    group = "ru.ekaterina-gorbunova.${project.name}"
    mainClass.set("Main")
    applicationDefaultJvmArgs += listOf("--XX:+ExitOnOutOfMemoryError", "-XX:+PrintFlagsFinal")
}

dependencies {
    val testContainersVersion = "1.17.6"
    val junitVersion = "5.9.2"
    val h2Version = "2.1.214"
    val jacksonVersion = "2.14.2"

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("io.github.classgraph:classgraph:4.8.157")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.liquibase:liquibase-core:4.19.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.hibernate:hibernate-core:5.6.15.Final")
    implementation("com.vladmihalcea:hibernate-types-52:2.21.1")
    implementation("com.h2database:h2:$h2Version")
    implementation("com.google.guava:guava:31.1-jre")
    runtimeOnly("org.postgresql:postgresql:42.5.4")

    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0") { isTransitive = false }
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("resources")
    }
    test {
        java.srcDir("test")
        resources.srcDir("resources")
    }
}

tasks.test {
    useJUnitPlatform()
}