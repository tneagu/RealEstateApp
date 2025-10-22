# ADR 003: Dependency Injection Setup

## Status
Accepted

## Context
The app uses Hilt for dependency injection across multiple modules (core:network, features). I needed to establish clear guidelines for module installation and scoping to ensure consistency, avoid common pitfalls, and maintain a clean architecture.

## Decision
All DI modules use `SingletonComponent` with minimal scoping - only expensive infrastructure objects are singletons.

## Scoping Strategy

### @Singleton (Application-Scoped)
Only for expensive-to-create infrastructure:
- `OkHttpClient` 
- `Retrofit`
- `Moshi`

### Unscoped (Default)
Everything else creates new instances per injection:
- API services
- Repositories
- Use cases
- Converters/Mappers

## Module Structure

```kotlin
// core:network
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideOkHttpClient(...): OkHttpClient

    @Provides @Singleton
    fun provideRetrofit(...): Retrofit
}

// features:listings
@Module
@InstallIn(SingletonComponent::class)
abstract class ListingsModule {
    @Binds
    abstract fun bindListingsRepository(...): ListingsRepository

    @Provides
    fun provideListingsApiService(retrofit: Retrofit): ListingsApiService
}
```

## Rationale

**1. Consistency**
All infrastructure (network + data layer) in SingletonComponent provides a unified model.

**2. Simplicity**
Clear rule: expensive infrastructure = singleton, business logic = unscoped.

**3. Standard Practice**
Aligns with official Android architecture samples (Now in Android).

## Why Unscoped for Most Components?

**Repositories and Use Cases:**
- Stateless coordinators
- Cheap to create (just wiring dependencies)
- Unscoped prevents accidental state sharing bugs
- Fresh instances ensure isolation

**API Services:**
- Stateless interfaces
- Lightweight to create
- Although Retrofit is singleton, there si no real benefit to making services singleton

## Component Accessibility

`SingletonComponent` makes bindings available to:
- ViewModels (primary consumers)
- WorkManager Workers (background tasks)
- Services (if needed)
- Application class (initialization)

This doesn't mean everything becomes a singleton - just that bindings are accessible. Actual lifecycle is controlled by scope annotations.

## Consequences

### Positive
- **Clean separation**: Infrastructure singletons, business logic unscoped
- **No unnecessary scoping**: Only scope what truly needs it

### Negative
- **Less restrictive**: Repositories could theoretically be injected anywhere
  - *Mitigation*: Architecture enforced through code review and package structure
- **Not ViewModelComponent**: Some voices advocate for repositories in ViewModelComponent
  - *Justification*: Repositories are data infrastructure, not presentation dependencies

## Implementation Guidelines

**When adding new dependencies:**
1. Is it expensive to create (HTTP client, database)? → `@Singleton`
2. Is it business logic (repository, use case)? → Unscoped
3. Is it stateless infrastructure (API service)? → Unscoped
4. Default to unscoped unless there's a clear reason

**Module installation:**
- All modules → `SingletonComponent`
- Consistent placement regardless of actual component lifecycle

## Decision Date
2025-10-22
