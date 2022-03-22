import com.github.gradle.node.yarn.task.YarnTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val firebase_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("com.github.node-gradle.node") version "3.2.1"
}

group = "app.pwdr"
version = "0.0.1"
application {
    mainClass.set("app.pwdr.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")

    implementation("com.google.firebase:firebase-admin:$firebase_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

// REF: https://dev.to/darkes/using-gradle-s-kotlin-dsl-to-bundle-react-with-a-spring-boot-web-application-355k
// REF: https://github.com/node-gradle/gradle-node-plugin/blob/master/src/test/resources/fixtures/kotlin/build.gradle.kts
val appYarnInstallTask = tasks.register<YarnTask>("appYarnInstall") {
    description = "Installs all dependencies from package.json"
    workingDir.set(file("${project.projectDir}/src/main/resources/react"))
    args.set(listOf("install"))
}

val appYarnBuildTask = tasks.register<YarnTask>("appYarnBuild") {
    dependsOn(appYarnInstallTask)
    description = "Builds project"
    workingDir.set(file("${project.projectDir}/src/main/resources/react"))
    args.set(listOf("run", "build"))
}

val copyWebAppTask = tasks.register<Copy>("copyWebApp") {
    dependsOn(appYarnBuildTask)
    description = "Copies built project to where it will be served"
    from("src/main/resources/react/build")
    into("build/resources/main/static/.")
}

node {
    download.set(true)
    version.set("16.14.2")
    yarnVersion.set("1.22.18")
    workDir.set(file("${project.buildDir}/nodejs"))
    yarnWorkDir.set(file("${project.buildDir}/yarn"))
}

tasks.withType<KotlinCompile> {
    // So that all the tasks run with ./gradlew build
    dependsOn(copyWebAppTask)
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}