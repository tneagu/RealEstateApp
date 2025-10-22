# ADR 002: Domain-Centric Error Handling with DataResult

## Status
Accepted

## Context
The application needs a consistent error handling strategy across all layers (data, domain, presentation) that:
- Distinguishes between different error types
- Provides meaningful error messages to users
- Doesn't leak implementation details from data layer to presentation
- Works well with Kotlin coroutines
- 
## Decision
I implemented a **domain-centric error handling approach** using `DataResult<T>` sealed interface and domain-specific error types.

## Architecture

### Domain Layer (core:domain)

```kotlin
sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val error: DomainError) : DataResult<Nothing>
}

sealed interface DomainError {
    val message: String

    data class NetworkError(override val message: String) : DomainError
    data class ApiError(override val message: String, val code: Int) : DomainError
    data class UnknownError(override val message: String) : DomainError
}
```

### Data Layer Usage

```kotlin
override suspend fun getListings(): DataResult<List<Listing>> {
    return try {
        val response = apiService.getListings()
        DataResult.Success(response.map { it.toDomain() })
    } catch (e: IOException) {
        DataResult.Error(DomainError.NetworkError("Network connection failed"))
    } catch (e: HttpException) {
        DataResult.Error(DomainError.ApiError("Server error", e.code()))
    } catch (e: Exception) {
        DataResult.Error(DomainError.UnknownError(e.message ?: "Unknown error"))
    }
}
```

### Presentation Layer Usage

```kotlin
when (val result = getListingsUseCase()) {
    is DataResult.Success -> {
        _state.value = ListingsState.Success(result.data)
    }
    is DataResult.Error -> {
        _state.value = ListingsState.Error(result.error.message)
    }
}
```

## Rationale

### Why DataResult<T>?

**1. Type-Safe Error Handling**
- Compile-time guarantee that errors are handled
- No forgotten try-catch blocks
- IDE warns if error case isn't handled

**2. Domain-Centric**
- Errors are expressed in business terms, not technical exceptions
- Presentation layer doesn't need to know about IOException or HttpException
- Clean Architecture boundary respected

**3. Explicit Error Cases**
- All error types are documented in sealed interface
- Easy to extend with new error types
- Pattern matching ensures exhaustive handling

**4. Testability**
- No need to throw exceptions in tests
- Easy to mock error scenarios
- Test readability improved

### Error Type Categorization

**NetworkError**: Infrastructure failures
- No internet connection
- Request timeout
- DNS resolution failure
- **User Action**: Retry with network check

**ApiError**: Server-side issues
- 4xx client errors (bad request, unauthorized)
- 5xx server errors
- **User Action**: Show specific error message, sometimes retry

**UnknownError**: Catch-all
- Unexpected exceptions
- Parsing errors
- **User Action**: Generic error message, log to crash reporting

## Consequences

### Positive

**Type Safety**
```kotlin
// Compiler forces error handling
when (result) {
    is Success -> // handle success
    is Error -> // must handle error
}
```

**No Exception Propagation**
- Exceptions caught at data layer boundary
- Never propagate raw exceptions to presentation
- ViewModels don't crash from unhandled exceptions

**Clean Architecture Compliance**
- Domain layer defines error contract
- Data layer implements, presentation layer consumes
- No dependency inversion violations

**Testability**

```kotlin
@Test
fun `repository returns network error on IOException`() {
    coEvery { apiService.getListings() } throws IOException()

    val result = repository.getListings()

    assertTrue(result is DataResult.Error)
    assertTrue((result as DataResult.Error).error is NetworkError)
}
```

### Negative

**Learning Curve**
- Team must understand the pattern
- Different from traditional try-catch approach

**Verbosity**
- More code than throwing exceptions
- Explicit error mapping required

**No Stack Traces**
- Errors are converted to domain errors, losing stack trace
- Mitigated by logging original exception before conversion

### Mitigations

**Logging**:
```kotlin
catch (e: Exception) {
    Log.e(TAG, "API call failed", e) // Log original exception
    DataResult.Error(DomainError.UnknownError(e.message ?: "Unknown"))
}
```

## Alternatives Considered

### 1. Throwing Exceptions

```kotlin
// Data layer
suspend fun getListings(): List<Listing> {
    return apiService.getListings().map { it.toDomain() }
}

// ViewModel
try {
    val listings = getListingsUseCase()
    _state.value = Success(listings)
} catch (e: IOException) {
    _state.value = Error("Network error")
}
```

**Rejected**:
- Violates Clean Architecture (leaks data layer details)
- Not type-safe (easy to forget try-catch)
- Hard to test (need to throw exceptions)
- Errors not documented in type system

### 2. Nullable Results

```kotlin
suspend fun getListings(): List<Listing>? {
    return try {
        apiService.getListings().map { it.toDomain() }
    } catch (e: Exception) {
        null
    }
}
```

**Rejected**:
- Loses error information
- Can't distinguish between different error types
- No error message for user

## Implementation Guidelines

### Feature-Specific Errors

Features can extend `DomainError` with their own error types in the feature's domain layer.

```kotlin
// features/listingdetails/domain/model/ListingDetailError.kt
sealed interface ListingDetailError : DomainError {
    data class ListingNotFound(
        override val message: String = "Property not found",
        val listingId: Int
    ) : ListingDetailError

    data class ListingExpired(
        override val message: String = "Listing has expired",
        val listingId: Int
    ) : ListingDetailError
}
```

**When to use:**
- Feature-specific business logic errors → Feature domain layer
- Infrastructure errors (network, parsing) → Core `DomainError`

### Error Message Strategy

**Data Layer**: Technical → Domain mapping
```kotlin
catch (e: SocketTimeoutException) {
    NetworkError("Connection timed out. Please check your internet.")
}
```

**Presentation Layer**: Domain → User-friendly
```kotlin
when (error) {
    is NetworkError -> "Unable to connect. Check internet and try again."
    is ApiError -> when (error.code) {
        401 -> "Session expired. Please log in again."
        404 -> "Listing not found."
        else -> error.message
    }
}
```

### Testing Pattern

```kotlin
@Test
fun `use case propagates error from repository`() = runTest {
    // Given
    val expectedError = DomainError.NetworkError("Network failed")
    coEvery { repository.getListings() } returns DataResult.Error(expectedError)

    // When
    val result = useCase()

    // Then
    assertEquals(DataResult.Error(expectedError), result)
}
```

## Future Enhancements

### Potential Extensions

1. **Retry Logic**:
```kotlin
data class RetryableError(
    override val message: String,
    val retryAction: suspend () -> DataResult<*>
) : DomainError
```

2. **Error Analytics**:
```kotlin
fun DataResult.Error.logToAnalytics() {
    when (error) {
        is NetworkError -> analytics.logNetworkError()
        is ApiError -> analytics.logApiError(error.code)
    }
}
```

## Decision Date
2025-10-22
