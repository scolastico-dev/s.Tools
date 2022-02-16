import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("org.jetbrains.dokka") version "1.6.10"
    kotlin("jvm") version "1.6.0"
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
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.0")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
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
    implementation("io.leego:banana:2.1.0")
    implementation("io.sentry:sentry:5.6.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.reflections:reflections:0.9.12")
    implementation("commons-fileupload:commons-fileupload:1.4")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.1")
    implementation("org.fusesource.jansi:jansi:2.4.0")
    implementation("info.picocli:picocli:4.6.2")
    implementation("org.jline:jline:3.21.0")
    implementation("com.kosprov.jargon2:jargon2-api:1.1.1")
    implementation("com.kosprov.jargon2:jargon2-native-ri-backend:1.1.1")
    implementation("org.json:json:20211205")
    implementation("io.ebean:ebean:12.15.0")
    implementation("io.ebean:ebean-test:12.15.0")
    implementation("io.ebean:ebean-migration:12.13.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.3")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("com.microsoft.sqlserver:mssql-jdbc:9.4.1.jre11")
    implementation("org.postgresql:postgresql:42.3.3")
    implementation("com.oracle.database.jdbc:ojdbc10:19.12.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.googlecode.lanterna:lanterna:3.1.1")
    implementation("info.picocli:picocli-shell-jline3:4.6.3")
    implementation("org.jline:jline:3.21.0")
    implementation("org.jline:jline-console:3.21.0")
    implementation("commons-codec:commons-codec:1.15")
    // disabled because of CVE-2022-23221 CVE-2021-42392 CVE-2021-23463
    // and incompatibility with eban
    // implementation("com.h2database:h2:1.4.190")
}
