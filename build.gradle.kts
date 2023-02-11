import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:1.8.10")
    }
}

plugins {
    id("maven-publish")
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
    id("jacoco")
    kotlin("plugin.jpa") version "1.8.10"
    id("com.diffplug.spotless") version "6.15.0"
}

group = "com.stefanbratanov"
version = "1.4.0"
java.sourceCompatibility = JavaVersion.VERSION_17

jacoco {
    toolVersion = "0.8.8"
}

repositories {
    mavenCentral()
}

val junitVersion = "5.9.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.data:spring-data-keyvalue")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.postgresql:postgresql")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("commons-validator:commons-validator:1.7")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("org.apache.pdfbox:pdfbox:2.0.27")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    implementation("com.cloudinary:cloudinary-http44:1.33.0")
    implementation("org.seleniumhq.selenium:selenium-java:4.8.0")
    implementation("com.codeborne:phantomjsdriver:1.5.0")
    implementation("io.github.bonigarcia:webdrivermanager:4.4.3")
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
    testImplementation("org.mock-server:mockserver-spring-test-listener:5.15.0")
    testRuntimeOnly("com.h2database:h2")
}

springBoot {
    buildInfo()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

configure<SpotlessExtension> {
    kotlin {
        ktlint("0.48.2")
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}
