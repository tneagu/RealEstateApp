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
    dependsOn(subprojects.map { it.tasks.withType<Test>() })

    val sourceDirs = files()
    val classDirs = files()
    val executionData = files()

    subprojects.forEach { subproject ->
        subproject.plugins.withType<JacocoPlugin>().configureEach {
            sourceDirs.from(subproject.files("src/main/java", "src/main/kotlin"))

            classDirs.from(subproject.fileTree("${subproject.layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
                exclude(
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
                    "**/*ComposableSingletons*.*"
                )
            })

            executionData.from(subproject.fileTree(subproject.layout.buildDirectory.get()) {
                include("**/*.exec", "**/*.ec")
            })
        }
    }

    sourceDirectories.setFrom(sourceDirs)
    classDirectories.setFrom(classDirs)
    executionData.setFrom(executionData)

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)

        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoAggregatedReport/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacocoAggregatedReport/jacocoAggregatedReport.xml"))
    }
}