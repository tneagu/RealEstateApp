# Real Estate App

[![CI/CD Pipeline](https://github.com/tneagu/RealEstateApp/workflows/CI%2FCD%20Pipeline/badge.svg)](https://github.com/tneagu/RealEstateApp/actions)
[![codecov](https://codecov.io/gh/tneagu/RealEstateApp/branch/master/graph/badge.svg)](https://codecov.io/gh/tneagu/RealEstateApp)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg)](https://kotlinlang.org)

A modern Android application showcasing real estate listings, built with Clean Architecture, MVI pattern, Jetpack Compose, and comprehensive CI/CD automation.

## Table of Contents
- [Key Architectural Decisions](#key-architectural-decisions)
- [Tech Stack](#tech-stack)
  - [Core](#core)
  - [Networking](#networking)
  - [Architecture](#architecture)
  - [Testing](#testing-1)
  - [Code Quality & CI/CD](#code-quality--cicd)
- [Project Structure](#project-structure)
- [Build Configuration](#build-configuration)
  - [Debug Build](#debug-build)
  - [Release Build (Not Implemented - By Design)](#release-build-not-implemented---by-design)
- [CI/CD Pipeline](#cicd-pipeline)
  - [Automated Quality Gates](#automated-quality-gates)
  - [Pipeline Status](#pipeline-status)
  - [Build Performance](#build-performance)
- [Testing](#testing)
  - [Unit Tests](#unit-tests)
  - [Coverage Reports](#coverage-reports)
- [Code Quality](#code-quality)
  - [Static Analysis](#static-analysis)
  - [Quality Standards](#quality-standards)
- [License](#license)

## Key Architectural Decisions

This project follows **Clean Architecture** principles with **MVI (Model-View-Intent)** pattern for presentation layer.
All major architectural decisions are documented in the [docs/adr/](docs/adr/) directory:

- **[ADR-001](docs/adr/001-modularization-strategy.md)**: Feature-based modularization strategy
- **[ADR-002](docs/adr/002-error-handling-approach.md)**: Domain-centric error handling
- **[ADR-003](docs/adr/003-dependency-injection-setup.md)**: DI scoping strategy

These documents follows standard ADR (Architecture Decision Records) format and explains:
- **Context**: Why the decision was needed
- **Decision**: What we chose
- **Rationale**: Why we made that choice
- **Consequences**: Trade-offs and implications
- **Alternatives**: What else was considered

## Tech Stack

### Core
- **Kotlin** 2.2.20 - 100% Kotlin codebase
- **Jetpack Compose** - Modern declarative UI
- **Material Design 3** - UI components and theming
- **Hilt** - Dependency injection
- **Coroutines** - Asynchronous programming

### Networking
- **Retrofit** REST API client
- **OkHttp** HTTP client
- **Moshi** - JSON parsing

### Architecture
- **Clean Architecture** - Clear separation of concerns
- **MVI Pattern** - Unidirectional data flow
- **Jetpack Navigation** - Compose navigation

### Testing
- **JUnit** - Unit testing framework
- **MockK** - Mocking library
- **Turbine** - Flow testing
- **JaCoCo** - Code coverageFF

### Code Quality & CI/CD
- **Detekt** - Static code analysis
- **Ktlint** - Code formatting
- **GitHub Actions** - CI/CD automation
- **Codecov** - Coverage reporting

## Project Structure

```
RealEstateApp/
├── app/                          # Application module
│   └── MainActivity.kt           # App entrypoint
│   └── navigation                # App navigation
│       ├── NavGraph.kt           # Main navigation graph
│
├── core/                         # Shared core modules
│   ├── domain/                   # Shared domain types
│   │   ├── model/                # DataResult, DomainError, OfferType
│   │   └── ...
│   ├── network/                  # Networking setup
│   │   ├── NetworkModule.kt      # Retrofit, OkHttp configuration
│   │   └── ...
│   └── ui/                       # Design system
│       ├── components/           # LoadingView, ErrorView, PropertyTag
│       ├── theme/                # Colors, Typography, Spacing
│       └── ...
│
├── features/                     # Feature modules
│   ├── listings/                 # Listings feature
│   │   ├── data/
│   │   │   ├── api/              # ListingsApiService
│   │   │   ├── dto/              # ListingDTO
│   │   │   ├── mapper/           # DTO → Domain mappers
│   │   │   └── repository/       # ListingsRepositoryImpl
│   │   ├── domain/
│   │   │   ├── model/            # Listing domain model
│   │   │   ├── repository/       # ListingsRepository interface
│   │   │   └── usecase/          # GetListingsUseCase
│   │   └── presentation/
│   │       ├── mvi/              # State, Intent, Effect
│   │       ├── viewmodel/        # ListingsViewModel
│   │       └── ui/               # ListingsScreen composables
│   │
│   └── listingdetails/           # Details feature (similar structure)
│
├── docs/                         # Documentation
│   └── adr/                      # Architecture Decision Records
│
├── config/                       # Configuration files
│   └── detekt/                   # Detekt rules
│
└── .github/                      # GitHub configuration
    └── workflows/                # CI/CD pipelines
```

## Build Configuration

### Debug Build
This project provides a debug build configuration for development and demonstration:

- **Available in**: GitHub Actions artifacts (every commit)
- **Signing**: Debug keystore (automatic)
- **Minification**: Disabled for easier debugging
- **Installable**: Yes, for testing purposes

### Release Build (Not Implemented - By Design)

In a production application, a release build would include:

#### Code Optimization
- R8/ProGuard minification and obfuscation
- Resource shrinking to reduce APK size
- Code optimization for performance

#### Security
- Production keystore (stored securely)
- Certificate pinning for API calls
- ProGuard rules to protect sensitive code paths

#### Configuration
- Production API endpoints (vs test/staging)
- Analytics and crash reporting enabled
- Release-specific feature flags

#### Why Not Included Here
- No production deployment target for this demo project
- Keystore management would be artificial for showcase purposes
- Focus is on architecture, code quality, and testing practices


## CI/CD Pipeline

### Automated Quality Gates

Every push to `master` triggers:

#### 1. Static Analysis & Linting
- **Detekt**: Kotlin code quality checks with custom rules
- **Android Lint**: Standard Android checks
- **Ktlint**: Code formatting validation

#### 2. Unit Tests & Coverage
- Run all module tests in parallel
- Generate JaCoCo coverage reports
- Upload coverage to Codecov
- **Target**: 80%+ coverage on critical paths

#### 3. Build Validation
- Build debug APK
- Upload APK as downloadable artifact

#### 4. Dependency Security
- Scan for vulnerable dependencies
- Generate update reports

### Pipeline Status

View the latest pipeline run: [GitHub Actions](https://github.com/tneagu/RealEstateApp/actions)

### Build Performance

Optimizations in place:
- Gradle configuration cache enabled
- Parallel module compilation
- Build cache optimization
- Incremental compilation
- Optimized JVM settings (4GB heap)

**Typical CI build time**: ~8-12 minutes

## Testing

### Unit Tests

Located in `src/test/` directories across all modules.

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :features:listings:testDebugUnitTest

# Run with coverage
./gradlew jacocoTestReport
```

### Coverage Reports

After running tests with coverage:
- **HTML Report**: `build/reports/jacoco/jacocoAggregatedReport/html/index.html`
- **XML Report**: `build/reports/jacoco/jacocoAggregatedReport/jacocoAggregatedReport.xml`
- **Online**: Check [Codecov](https://codecov.io/gh/tneagu/RealEstateApp)


## Code Quality

### Static Analysis

```bash
# Run Detekt
./gradlew detekt

# Run Android Lint
./gradlew lintDebug

# Run Ktlint check
./gradlew ktlintCheck

# Auto-format with Ktlint
./gradlew ktlintFormat
```

### Quality Standards

- **Code Coverage**: Target 80%+ on business logic (mappers, repositories, ViewModels)
- **Detekt Rules**: Standard code quality checks and Kotlin best practices
- **Kotlin Style**: Official Kotlin coding conventions
- **Max Line Length**: 120 characters
- **Max Complexity**: 15 per method


## License

This project is a technical assessment showcase. All rights reserved.

---
