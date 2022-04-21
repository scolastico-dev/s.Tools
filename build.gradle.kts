import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("org.jetbrains.dokka") version "1.6.10"
    kotlin("jvm") version "1.6.20"
}

group = "me.scolastico"
version = "dev-snapshot"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "12"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.19.0")
    }
}

allprojects {
    pluginManager.withPlugin("com.vanniktech.maven.publish") {
        extensions.getByType(MavenPublishPluginExtension::class.java).apply {
            sonatypeHost = SonatypeHost.S01
        }
    }
}

apply(plugin="org.jetbrains.dokka")
apply(plugin="com.vanniktech.maven.publish")

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar{
        archiveFileName.set("scolastico-tools.jar")
    }
    javadoc {
        options.encoding("UTF-8")
    }
    test {
        systemProperty("file.encoding", "UTF-8")
        useJUnitPlatform()
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("detekt.yml")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        txt.required.set(true)
        xml.required.set(false)
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "12"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "12"
}

dependencies {
    testImplementation(kotlin("test"))
    api("io.leego:banana:2.1.0")
    api("io.sentry:sentry:5.7.2")
    api("org.apache.commons:commons-lang3:3.12.0")
    api("commons-io:commons-io:2.11.0")
    api("com.google.code.gson:gson:2.9.0")
    api("org.reflections:reflections:0.9.12")
    api("commons-fileupload:commons-fileupload:1.4")
    api("org.springframework.boot:spring-boot-starter-web:2.6.6")
    api("org.fusesource.jansi:jansi:2.4.0")
    api("info.picocli:picocli:4.6.3")
    api("org.jline:jline:3.21.0")
    api("com.kosprov.jargon2:jargon2-api:1.1.1")
    api("com.kosprov.jargon2:jargon2-native-ri-backend:1.1.1")
    api("org.json:json:20220320")
    api("io.ebean:ebean:13.4.0")
    api("io.ebean:ebean-test:13.2.0")
    api("io.ebean:ebean-migration:13.0.0")
    api("com.fasterxml.jackson.core:jackson-core:2.13.2")
    api("mysql:mysql-connector-java:8.0.28")
    api("org.mariadb.jdbc:mariadb-java-client:3.0.4")
    api("org.xerial:sqlite-jdbc:3.36.0.3")
    api("com.microsoft.sqlserver:mssql-jdbc:10.2.0.jre11")
    api("org.postgresql:postgresql:42.3.3")
    api("com.oracle.database.jdbc:ojdbc10:19.14.0.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
    api("com.googlecode.lanterna:lanterna:3.1.1")
    api("info.picocli:picocli-shell-jline3:4.6.3")
    api("org.jline:jline:3.21.0")
    api("org.jline:jline-console:3.21.0")
    api("commons-codec:commons-codec:1.15")
    api("io.ktor:ktor-server-core:1.6.8")
    api("io.ktor:ktor-server-netty:1.6.8")
    api("io.ktor:ktor-websockets:1.6.8")
    api("io.ktor:ktor-gson:1.6.8")
    api("io.ktor:ktor-network-tls-certificates:1.6.8")
    // disabled because of CVE-2022-23221 CVE-2021-42392 CVE-2021-23463
    // and incompatibility with eban
    // api("com.h2database:h2:1.4.190")
    // dependencies for ktor 2.0
    // api("io.ktor:ktor-server-core:2.0.0-beta-1")
    // api("io.ktor:ktor-server-netty:2.0.0-beta-1")
    // api("io.ktor:ktor-server-status-pages:2.0.0-beta-1")
    // api("io.ktor:ktor-server-default-headers:2.0.0-beta-1")
    // api("io.ktor:ktor-server-websockets:2.0.0-beta-1")
    // api("io.ktor:ktor-server-content-negotiation:2.0.0-beta-1")
    // api("io.ktor:ktor-server-cors-jvm:2.0.0-beta-1")
    // api("io.ktor:ktor-serialization-gson:2.0.0-beta-1")
}
