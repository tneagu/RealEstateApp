// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.gradle.versions)
    jacoco
}

// Detekt configuration
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt/detekt.yml")
    baseline = file("$projectDir/config/detekt/baseline.xml")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
}

// Dependency updates configuration
tasks.named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates").configure {
    checkForGradleUpdate = true
    outputFormatter = "html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

// JaCoCo configuration
jacoco {
    toolVersion = "0.8.12"
}

// Aggregate JaCoCo report for all modules
tasks.register<JacocoReport>("jacocoAggregatedReport") {
    group = "verification"
    description = "Generates aggregated JaCoCo coverage report from all modules"

    dependsOn(subprojects.mapNotNull { it.tasks.findByName("testDebugUnitTest") })

    val fileExclusions = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*\$Lambda$*.*",
        "**/*\$inlined$*.*",
        "**/*Module.*",
        "**/*Module\$*.*",
        "**/*_Factory.*",
        "**/*_MembersInjector.*",
        "**/Hilt_*.*",
        "**/*Hilt*.*",
        "**/*_Impl.*",
        "**/*ComposableSingletons*.*",
        // Exclude UI layer (Composables - require UI tests, not unit tests)
        "**/ui/**/*.class",
        "**/preview/**/*.class",
        "**/*Activity*.class",
        "**/navigation/**/*.class"
    )

    // Collect source directories
    sourceDirectories.setFrom(
        subprojects.flatMap { subproject ->
            listOf(
                subproject.file("src/main/java"),
                subproject.file("src/main/kotlin")
            ).filter { it.exists() }
        }
    )

    // Collect class directories with exclusions
    classDirectories.setFrom(
        subprojects.map { subproject ->
            subproject.fileTree("${subproject.layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
                exclude(fileExclusions)
            }
        }
    )

    // Collect execution data
    executionData.setFrom(
        subprojects.map { subproject ->
            subproject.fileTree(subproject.layout.buildDirectory.get()) {
                include("**/*.exec", "**/*.ec")
            }
        }
    )

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)

        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoAggregatedReport/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacocoAggregatedReport/jacocoAggregatedReport.xml"))
    }
}