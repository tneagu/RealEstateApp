# ADR 001: Feature-Based Modularization Strategy

## Status
Accepted

## Context
The project requires a modular architecture to support scalability, parallel development, and clear separation of concerns. I needed to decide on a modularization strategy that balances complexity with maintainability.

### Requirements
- Clear module boundaries
- Support for parallel feature development
- Fast build times through incremental compilation
- Reusable shared components
- Clean dependency graph (no circular dependencies)

## Decision
I adopted a **feature-based modularization strategy** with shared core modules.

## Module Structure

```
RealEstateApp/
├── app/                          # Application module (navigation)
├── core/
│   ├── domain/                   # Shared domain models and types
│   ├── network/                  # Networking setup (Retrofit, OkHttp)
│   └── ui/                       # Shared UI components, theme, design system
├── features/
│   ├── listings/
│   │   ├── data/                 # API, DTOs, repositories implementations
│   │   ├── domain/               # Models, use cases
│   │   └── presentation/         # ViewModels, UI
│   └── listingdetails/
│       ├── data/
│       ├── domain/
│       └── presentation/
```

## Rationale

### Why Feature-Based Modularization?

**1. Business Logic Isolation**
- Each feature is self-contained
- Listings and ListingDetails can evolve independently

**2. Parallel Development**
- Multiple developers can work on different features without conflicts
- Reduced merge conflicts in separate modules
- Clear ownership boundaries

**3. Build Performance**
- Gradle can build modules in parallel
- Changes to one feature don't require rebuilding others
- Only affected modules recompile (incremental builds)

**4. Testability**
- Faster test execution

**5. Clear Dependencies**
- Dependency flow: `app → features → core`
- No feature-to-feature dependencies
- Easy to visualize and enforce

### Core Modules

**core:domain**
- **Purpose**: Shared domain types used across features
- **Contains**: `DataResult`, `DomainError`, `OfferType`
- **Dependencies**: None
- **Rationale**: Domain models should be stable and reusable

**core:network**
- **Purpose**: Centralized networking configuration
- **Contains**: Retrofit setup, OkHttpClient
- **Dependencies**: Hilt for DI
- **Rationale**:
  - Single source of truth for API configuration
  - Shared across all feature data layers

**core:ui**
- **Purpose**: Design system and reusable UI components
- **Contains**: Theme, colors, spacing, shared composables (LoadingView, ErrorView, PropertyTag)
- **Dependencies**: Compose, Material3
- **Rationale**:
  - Consistent UI/UX across features
  - Avoids duplication of common UI elements
  - Single place to update design system

### Feature Module Structure

Each feature follows Clean Architecture layers:

**data/** - Data layer
- DTOs (internal visibility)
- API services
- Repository implementations
- Mappers (DTO → Domain)
- Unit tests

**domain/** - Domain layer
- Domain models (public)
- Repository interfaces
- Use cases
- Business logic
- Unit tests

**presentation/** - Presentation layer
- MVI components (State, Intent, Effect)
- ViewModels
- Compose UI
- Unit tests

## Dependency Rules

### Allowed Dependencies
```
app           → features:*, core:*
features:*    → core:*
core:ui       → core:domain (for shared types)
core:network  → none (except DI and networking libs)
core:domain   → none
```

### Forbidden Dependencies
```
features:listings     ✗→ features:listingdetails
features:listingdetails ✗→ features:listings
core:*                ✗→ features:*
core:*                ✗→ app
```

## Consequences

### Positive

**Build Performance**
- Parallel module compilation
- Incremental builds only recompile affected modules
- Faster CI/CD pipeline

**Team Scalability**
- Clear ownership per feature
- Reduced merge conflicts
- New features can be added as new modules

**Code Quality**
- Enforced dependency boundaries prevent spaghetti code
- Shared code is explicitly extracted to core
- Feature isolation encourages focused, cohesive code

**Testing**
- Feature modules can be tested independently
- Faster test suites

### Negative

**Initial Complexity**
- More build.gradle.kts files to maintain
- Overhead for small features
- Learning curve for module boundaries

**Navigation Complexity**
- Inter-feature navigation requires app module coordination
- Can't directly navigate from feature to feature

**Dependency Management**
- Version alignment across modules (mitigated by version catalogs)
- Shared dependencies duplicated in build files

### Mitigations

**Complexity**:
- Use Gradle version catalog for centralized dependency management
- Document module structure in README
- Consistent module structure across features

**Navigation**:
- Centralize navigation graph in app module
- Use MVI Effects for navigation requests
- ViewModels emit navigation effects, app module handles routing

## Alternatives Considered

### 1. Monolithic Single Module
**Rejected**:
- Poor build performance at scale
- Merge conflicts increase with team size
- Hard to enforce architectural boundaries
- Cannot parallelize compilation

### 2. Layer-Based Modularization
```
data/
domain/
presentation/
```
**Rejected**:
- Features are scattered across layers
- Hard to understand full feature scope
- Doesn't support parallel feature development
- Difficult to add/remove features

### 3. Hybrid (Feature + Layer)
```
features/
  listings-data/
  listings-domain/
  listings-presentation/
```
**Rejected**:
- Over-modularization for small app
- Too much overhead for this project size
- Benefits don't outweigh complexity

## Implementation Guidelines

### Creating a New Feature

1. Create feature module structure:
   ```
   features/newfeature/
     ├── data/
     ├── domain/
     └── presentation/
   ```

2. Define module in `settings.gradle.kts`:
   ```kotlin
   include(":features:newfeature")
   ```

3. Add dependencies:
   ```kotlin
   // features/newfeature/build.gradle.kts
   dependencies {
       implementation(project(":core:network"))
       implementation(project(":core:domain"))
       implementation(project(":core:ui"))
   }
   ```

4. Follow Clean Architecture layers within module

5. Add navigation entry in app module

### Module Responsibility Checklist

Before creating shared code, ask:
- ✅ Is it used by 2+ features? → core module
- ✅ Is it feature-specific? → feature module

## Future Considerations

### Scalability Path
If the app grows significantly:
1. Split large features into sub-features
2. Introduce layer-based modules within features
3. Consider dynamic delivery for rarely-used features

## References
- [Guide to Android app modularization](https://developer.android.com/topic/modularization)
- [Now in Android modularization](https://github.com/android/nowinandroid)
- [Modularization at Scale (Google I/O)](https://www.youtube.com/watch?v=PZBg5DIzNww)

## Decision Date
2025-10-22
