import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	id("org.springframework.boot") version "2.4.1"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
	id("jacoco")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Kafka
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")

	// Other
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.github.microutils:kotlin-logging:2.0.3")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.4.RELEASE") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
	testImplementation("org.springframework.kafka:spring-kafka-test:2.5.1.RELEASE")
	testImplementation("io.mockk:mockk:1.10.2")
	testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	withType<Test> {
		useJUnitPlatform()
	}

	test {
		testLogging {
			events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)

			showExceptions = true
			showCauses = true
			showStackTraces = true
		}
	}

	jacocoTestReport {
		reports {
			xml.isEnabled = false
			csv.isEnabled = false
			html.isEnabled = true
			html.destination = file("$buildDir/reports/coverage")
		}
	}

	jacocoTestCoverageVerification {
		violationRules {
			isFailOnViolation = true
			rule {
				element = "SOURCEFILE"
				excludes = listOf(
						"com/example/kafkastreams/KafkaStreamsApplication.kt"
				)
				limit {

					// This is the minimum coverage required for the build to pass
					minimum = "0.8".toBigDecimal()
				}
			}
		}
	}

	register<Task>("coverage") {
		group = "verification"
		description = "Runs the unit tests with coverage."
		dependsOn(":test", ":jacocoTestReport", ":jacocoTestCoverageVerification")
		val jacocoTestReport = findByName("jacocoTestReport")
		jacocoTestReport?.mustRunAfter(findByName("test"))
		findByName("jacocoTestCoverageVerification")?.mustRunAfter(jacocoTestReport)
	}
}

