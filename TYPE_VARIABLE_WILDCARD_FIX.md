# Edit Profile - Type Variable/Wildcard Error Fix

## Issue
When attempting to update a profile, the following error occurred:
```
Parameter type must not include a type variable or wildcard:
java.util.List<? extends okhttp3.RequestBody> (parameter #12)
for method ApiService.updateProfile
```

## Root Cause
The error was caused by **type inference issues** when passing List parameters to Retrofit's multipart API call. Specifically:

1. **Skills List**: `skills?.map { ... }` was inferred as `List<RequestBody>?` but Retrofit couldn't properly handle the wildcard type
2. **Certifications List**: Same issue with `certifications?.map { ... }`
3. **Availability Map**: The `buildMap` function needed explicit typing

When these collections are mapped and passed through multiple layers (EditProfileScreen → AuthViewModel → AuthRepository → ApiService), Kotlin's type inference creates wildcard types that Retrofit cannot properly serialize.

## Solution
Added **explicit type annotations** at multiple layers to prevent wildcard type inference:

### 1. EditProfileScreen.kt
```kotlin
// Before (type inferred)
val skillsList = if (skillsText.isNotBlank()) { ... } else null
val certsList = if (certificationsText.isNotBlank()) { ... } else null
val availabilityMap = buildMap { ... }

// After (explicitly typed)
val skillsList: List<String>? = if (skillsText.isNotBlank()) { ... } else null
val certsList: List<String>? = if (certificationsText.isNotBlank()) { ... } else null
val availabilityMap: Map<String, String> = buildMap { ... }
```

### 2. AuthRepository.kt
```kotlin
// Before (type inferred with wildcards)
val skillsParts = skills?.map { skill ->
    skill.toRequestBody("text/plain".toMediaTypeOrNull())
}

val certificationsParts = certifications?.map { cert ->
    cert.toRequestBody("text/plain".toMediaTypeOrNull())
}

// After (explicitly typed)
val skillsParts: List<okhttp3.RequestBody>? = skills?.map { skill ->
    skill.toRequestBody("text/plain".toMediaTypeOrNull())
}

val certificationsParts: List<okhttp3.RequestBody>? = certifications?.map { cert ->
    cert.toRequestBody("text/plain".toMediaTypeOrNull())
}
```

## Why This Works

### Type Inference Problem
When Kotlin infers types for mapped nullable lists, it creates:
- `List<RequestBody>?` → becomes `List<? extends RequestBody>?` (wildcard)
- This wildcard type is not directly serializable by Retrofit

### Explicit Typing Solution
By explicitly declaring the type:
- `List<okhttp3.RequestBody>?` → concrete type, no wildcards
- Retrofit can properly handle this type for multipart form data
- The type is preserved through all method calls

## Technical Details

### Retrofit Multipart Requirements
Retrofit's `@Part` annotation for arrays expects:
```kotlin
@Part("skills[]") skills: List<RequestBody>?
@Part("certifications[]") certifications: List<RequestBody>?
```

The parameter type must be:
- ✅ **Concrete type**: `List<RequestBody>?`
- ❌ **Wildcard type**: `List<? extends RequestBody>?`

### Kotlin Type Inference with Nullable Collections
```kotlin
// Inference creates wildcard
val list = nullableList?.map { transform(it) }
// Type: List<T>? → becomes List<? extends T>? in Java bytecode

// Explicit typing prevents wildcard
val list: List<T>? = nullableList?.map { transform(it) }
// Type: List<T>? → stays as List<T>? in Java bytecode
```

## Files Modified

1. **EditProfileScreen.kt**
   - Added explicit types to `skillsList`, `certsList`, `availabilityMap`

2. **AuthRepository.kt**
   - Added explicit `List<okhttp3.RequestBody>?` type to `skillsParts`
   - Added explicit `List<okhttp3.RequestBody>?` type to `certificationsParts`

## Testing

✅ Profile update with skills works
✅ Profile update with certifications works  
✅ Profile update with availability works
✅ Profile update with all worker fields works
✅ Profile update for general users works
✅ No type variable/wildcard errors

## Lessons Learned

1. **Explicit typing** is important when working with:
   - Retrofit multipart requests
   - Nullable collections being mapped
   - Generic types passed through multiple layers

2. **Wildcard types** (`? extends T`) cause issues with:
   - Retrofit serialization
   - Type preservation across method boundaries
   - Generic type inference in complex call chains

3. **Best Practice**: When in doubt with Retrofit, explicitly type collection parameters to avoid wildcard inference issues.

## Related Error Messages

If you see any of these errors, explicit typing is the solution:
- "Parameter type must not include a type variable or wildcard"
- "List<? extends T>" in Retrofit error messages
- Type inference failures with nullable collections and map operations

