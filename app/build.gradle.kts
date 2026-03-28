plugins {
    id("com.gradleup.shadow")
    id("com.github.ben-manes.versions")
    id("org.sonarqube") version "7.2.3.7755"
    jacoco
    checkstyle
    application
}

application {
    mainClass = "hexlet.code.App"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    implementation(libs.javalin)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.slf4j.api)
    runtimeOnly(libs.slf4j.simple)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml"))
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
}

sonar {
    properties {
        property("sonar.projectKey", "DmitriyKorchagin95_java-project-72")
        property("sonar.organization", "dmitriykorchagin95")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.java.coveragePlugin", "jacoco")
    }
}
