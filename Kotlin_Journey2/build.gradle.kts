import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.5.21"
    application
}

group = "me.kfb"
version = "1.0-SNAPSHOT"
allprojects {
    repositories {
        maven { url =uri("https://maven.aliyun.com/repository/public")  }
        maven { url =uri("https://maven.aliyun.com/repository/google")  }
        maven { url =uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url =uri("https://maven.aliyun.com/repository/central") }
        maven { url =uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url =uri("https://maven.aliyun.com/repository/releases") }
        maven { url =uri("https://maven.aliyun.com/repository/snapshots") }
        maven { url =uri("https://maven.aliyun.com/repository/grails-core") }
        mavenLocal()
        mavenCentral()
    }
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("junit:junit:4.13.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
//    implementation ("org.apache.poi:poi:5.2.3")     // 用于处理.xls文件
//    implementation ("org.apache.poi:poi-ooxml:5.2.3")    // 用于处理.xlsx文件
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}