plugins {
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.protobuf") version "0.9.6"
    id ("jacoco")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.33.5"
    }
}


sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.google.code.gson:gson:2.13.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.springframework.data:spring-data-jpa:4.0.3")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:4.0.3")

    implementation("com.google.protobuf:protobuf-java-util:4.33.5")
    implementation("com.google.protobuf:protobuf-java:4.33.5")

    implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.18")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.2.1")

    testImplementation("org.testcontainers:junit-jupiter:1.21.4")
    testImplementation("org.testcontainers:testcontainers:1.21.4")
    testImplementation("org.testcontainers:postgresql:1.21.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test:4.0.3")
    testImplementation("org.springframework.boot:spring-boot-resttestclient:4.0.3")

}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.test {
    useJUnitPlatform()

    finalizedBy(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}


tasks.jacocoTestReport {

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("com/sneha/error**","com/sneha/Constant**","com/sneha/Main**","com/sneha/userservice**", "com/sneha/pointservice**","com/sneha/rankingservice**")
            }
        })
    )
}

application {
    // Define the main class for the application.
    mainClass = "com.sneha.Main"
}

tasks.test {
    useJUnitPlatform()
}