plugins {
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.protobuf") version "0.9.6"
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

}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

application {
    // Define the main class for the application.
    mainClass = "com.sneha.Main"
}

tasks.test {
    useJUnitPlatform()
}