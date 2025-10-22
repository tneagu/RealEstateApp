# ADR 004: Caching Strategy Proposal (Not Implemented)

## Status
PROPOSED - Not implemented due to time constraints and unvalidated API contract assumptions

## Context

### Current Behavior

The app makes network requests in the following scenarios:

**Listings Screen:**
- First load: Network call to `/listings.json` (state is `NotInitialized`)
- Navigation back from details: **No network call** (state is preserved in ViewModel)
- App process death/recreation: Network call to `/listings.json`

**Listing Details Screen:**
- Every navigation to details: Network call to `/listings/{id}.json`
- Navigating to listing #42 → Network call
- Navigating back → Listings screen preserved
- Navigating to listing #42 again → **Another network call** (new ViewModel instance)

### The Problem

The details screen is the primary pain point:
- User browses through multiple listings → Each selection triggers a network call
- User returns to a previously viewed listing → Network call repeated
- Poor user experience (loading spinners on every details view)
- Unnecessary bandwidth usage and server load

The listings screen has **less** urgency for caching because:
- Network call happens only once (in current scenario)
- ViewModel state preservation handles navigation back from details

### Domain Model Observation

Both endpoints return **identical data structures**:

```kotlin
// features/listings/domain/model/Listing.kt
data class Listing(
    val id: Int,
    val bedrooms: Int?,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: Double,
    val professional: String,
    val propertyType: String,
    val offerType: OfferType,
    val rooms: Int?
)

// features/listingdetails/domain/model/ListingDetail.kt
data class ListingDetail(
    //same fields like Listing
)
```

**Observation:** The models are structurally identical (same 9 fields, same types, same nullability).

**Critical question:** Is this a coincidence in the current API version, or a guaranteed contract? Without API documentation or backend team confirmation, this cannot be validated.

### Architectural Constraint

Feature modules (`features/listings` and `features/listingdetails`) cannot depend on each other (circular dependency). Both can only depend on `core/*` modules.

---

## Decision Options Considered

### Option 1: Cache Listing Details Only (Simplest)

**Strategy:** Cache only the details endpoint responses. Leave listings uncached.

**Architecture:**
```
core/cache/
├── Cache.kt                    # Generic cache interface
├── InMemoryCache.kt            # LRU implementation with optional TTL
└── di/CacheModule.kt           # Provides @Singleton cache

features/listingdetails/
└── Inject Cache<Int, ListingDetail> into repository
```

**Pros:**
- ✅ **Extremely simple** - minimal code, single responsibility
- ✅ **Solves the main pain point** - eliminates repeated details network calls
- ✅ **No cross-module complexity** - details feature is self-contained
- ✅ **No assumptions about API contracts** - doesn't assume list/detail compatibility
- ✅ **Listings already optimized** - ViewModel state preservation works well
- ✅ **Easy to test** - single cache, clear behavior
- ✅ **Memory efficient** - caches only what's viewed, LRU eviction

**Cons:**
- ⚠️ **Potential inconsistency** - if listing data changes, cached details needs to be flushed
- ⚠️ **Doesn't cache listings** - but this may not be needed given current behavior

**When this is optimal:**
- Details are viewed multiple times (browsing behavior)
- Listings screen is not frequently reloaded (current behavior)
- API contracts are unclear or unstable

---

### Option 2: Cache Listings Only, Eliminate Details Endpoint (Most Efficient)

**Strategy:** Assume `/listings.json` and `/listings/{id}.json` return identical data. Cache listings, fetch detail by ID from cache.

**Architecture:**
```
core/cache/
├── Cache.kt
├── InMemoryCache.kt
└── di/CacheModule.kt

features/listings/
└── Inject Cache<String, List<Listing>> into repository

features/listingdetails/
└── Inject Cache<String, List<Listing>> into repository
└── Filter by ID instead of fetching from network
```

**Pros:**
- ✅ **Zero network calls for details** - if listings are cached
- ✅ **Perfect consistency** - details always match listings (same data source)
- ✅ **Optimal user experience** - instant details screen
- ✅ **Reduced server load** - eliminates half of API calls
- ✅ **Simpler mental model** - one cache, one source of truth

**Cons:**
- ⚠️ **CRITICAL ASSUMPTION:** Assumes `/listings.json` contains complete data for `/listings/{id}.json`
- ⚠️ **Breaks if APIs diverge** - if detail endpoint adds extra fields, app breaks
- ⚠️ **Requires API contract validation** - cannot implement without backend confirmation
- ⚠️ **Couples features at data level** - details feature depends on listings cache
- ⚠️ **Cache miss behavior unclear** - what if listings not cached but detail is requested?

**When this is optimal:**
- API contract guarantees schema compatibility
- Backend team confirms no planned divergence

---

### Option 3: Unified Cache Manager (Hybrid Approach)

**Strategy:** Cache both listings and details in a unified structure. When listings refresh, preserve cached details.

**Architecture:**
```
core/cache/
├── Cache.kt
├── InMemoryCache.kt
├── ListingCacheManager.kt      # Unified cache coordinator
└── di/CacheModule.kt

Both features inject ListingCacheManager
```

**Implementation example:**
```kotlin
// core/cache/ListingCacheManager.kt
data class CachedListing(
    val summary: Listing,              // From listings endpoint
    val detail: ListingDetail? = null  // From details endpoint (if fetched)
)

@Singleton
class ListingCacheManager @Inject constructor() {
    private val cache = ConcurrentHashMap<Int, CachedListing>()

    // Listings repository calls this
    fun cacheListings(listings: List<Listing>) {
        listings.forEach { listing ->
            val existing = cache[listing.id]
            cache[listing.id] = CachedListing(
                summary = listing,
                detail = existing?.detail  // Preserve detailed data if exists
            )
        }
    }

    // Details repository calls this
    fun cacheDetail(detail: ListingDetail) {
        val existing = cache[detail.id]
        cache[detail.id] = CachedListing(
            summary = existing?.summary ?: convertToListing(detail),
            detail = detail
        )
    }

    fun getListingSummary(id: Int): Listing? = cache[id]?.summary
    fun getListingDetail(id: Int): ListingDetail? = cache[id]?.detail
    fun getAllListings(): List<Listing> = cache.values.map { it.summary }
    fun clear() = cache.clear()

    private fun convertToListing(detail: ListingDetail): Listing {
        // Since schemas are identical, direct conversion
        return Listing(
            id = detail.id,
            bedrooms = detail.bedrooms,
            city = detail.city,
            area = detail.area,
            imageUrl = detail.imageUrl,
            price = detail.price,
            professional = detail.professional,
            propertyType = detail.propertyType,
            offerType = detail.offerType,
            rooms = detail.rooms
        )
    }
}
```

**Pros:**
- ✅ **Best of both worlds** - caches both listings and details
- ✅ **Can pre-populate details from listings** - instant first view
- ✅ **Fallback to network** - if schemas diverge, fetches real detail

**Cons:**
- ⚠️ **Most complex option** 
- ⚠️ **Complex sync logic** - needs complex logic to keep in sync list data with details 
- ⚠️ **Assumes schema compatibility** - for pre-population feature
- ⚠️ **Couples features at cache level** - both features know about cache manager
- ⚠️ **Conversion logic required** - Listing ↔ ListingDetail transformations

**When this is optimal:**
- Need both listings and details cached
- Willing to accept complexity for best UX

---

## Recommended Decision: Option 1 (Cache Details Only)

### Justification

**Why Option 1 is the best choice for this project:**

1. **Solves the real problem:**
   - Details screen is the pain point (repeated network calls)
   - Listings screen already works well (ViewModel state preservation)
   - 80/20 rule: 80% of benefit with 20% of complexity

2. **No risky assumptions:**
   - Doesn't assume API schema compatibility
   - Works regardless of whether schemas are identical
   - Robust to future API changes

3. **Simplest implementation:**
   - Single cache, single feature, single responsibility
   - Easy to test, debug, and maintain

4. **Aligns with current architecture:**
   - Listings feature already optimized (state preservation)
   - Details feature is the bottleneck
   - Follows YAGNI principle

5. **Low risk, high reward:**
   - Can't break existing behavior
   - Pure optimization (no functional changes)
   - Easy to remove if not needed

---

## Why Not Implemented

This caching strategy was **not implemented** for the following reasons:

### 1. Time Constraints
- Assessment focused on demonstrating Clean Architecture, MVI pattern, and comprehensive testing
- Caching is a valuable optimization but not a mandatory requirement in the scope
- Prioritized delivering core requirements with high quality over optional features

### 2. Unvalidated API Contract Assumptions

While Option 1 doesn't strictly require API contract validation, a production implementation would still need to consider if there is data consistency guarantees between endpoints

---

## Alternatives Considered and Rejected

### Room Database (Persistent Cache)

**Rejected because:**
- ❌ **Overkill for session-based caching** - in-memory cache is sufficient
- ❌ **Real estate data changes frequently** 
- ❌ **Offline-first not needed** - browsing use case doesn't require offline access
- ❌ **Adds complexity** - database migrations, entity mappers, schema management
- ❌ **Performance overhead** - disk I/O slower than memory access

**When Room WOULD be appropriate:**
- User-generated data (saved searches, favorites, notes)
- Offline-first requirement
- Long-term persistence across app updates

### Retrofit HTTP Cache (OkHttp Interceptor)

**Rejected because:**
- ❌ **No control over cross-endpoint consistency** - can't coordinate list/detail caches
- ❌ **Cannot pre-populate details from listings** - each endpoint cached independently
- ❌ **Harder to invalidate** - no programmatic clear cache API
- ❌ **Less testable** - HTTP cache behavior is opaque

**When HTTP cache WOULD be appropriate:**
- No cross-endpoint coordination needed
- Static content (images, documents)

---

