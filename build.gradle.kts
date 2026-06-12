plugins {
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

// from w3schools
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.java-websocket:Java-WebSocket:1.5.7")
    implementation("org.json:json:20260522")
    implementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "org.creamycorp.CyberAdvanceVM"
}