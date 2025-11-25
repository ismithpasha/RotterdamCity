# NullPointerException Fix - Profile Update

## Issue
```
FATAL EXCEPTION: main
Process: com.dreamdiver.rotterdam, PID: 8114
java.lang.NullPointerException: Attempt to invoke virtual method 'int com.dreamdiver.rotterdam.data.model.User.getId()' on a null object reference
at com.dreamdiver.rotterdam.ui.viewmodel.AuthViewModel$updateProfile$1.invokeSuspend(AuthViewModel.kt:176)
```

## Root Cause
The `User` object in the API response was null, but the code was trying to access its properties without null checking. This occurred in:

1. **AuthData.user** - Was non-nullable but API could return null
2. **ProfileData.user** - Was non-nullable but API could return null
3. **AuthViewModel** - Didn't check if user object was null before accessing properties

## Changes Made

### 1. Updated Data Models (Auth.kt)

#### Before:
```kotlin
data class AuthData(
    val user: User,
    val token: String
)

data class ProfileData(
    val user: User,
    val avatarUrl: String?,
    ...
)
```

#### After:
```kotlin
data class AuthData(
    val user: User?,  // Now nullable
    val token: String
)

data class ProfileData(
    val user: User?,  // Now nullable
    val avatarUrl: String?,
    ...
)
```

### 2. Updated AuthViewModel - Register Method

#### Before:
```kotlin
if (response.success && response.data != null) {
    // Save user data
    preferencesManager.saveUserData(
        token = response.data.token,
        userId = response.data.user.id,  // Could crash here
        ...
    )
}
```

#### After:
```kotlin
if (response.success && response.data != null && response.data.user != null) {
    // Save user data - now safe
    preferencesManager.saveUserData(
        token = response.data.token,
        userId = response.data.user.id,  // Safe now
        ...
    )
}
```

### 3. Updated AuthViewModel - Login Method

Same null safety check added:
```kotlin
if (response.success && response.data != null && response.data.user != null) {
    // Safe to access user properties
}
```

### 4. Updated AuthViewModel - Update Profile Method

Same null safety check added:
```kotlin
if (response.success && response.data != null && response.data.user != null) {
    // Safe to access user properties
}
```

## Why This Happened

The API might return:
- Success but with null data
- Success with data but null user
- Partial response during errors
- Network issues causing incomplete responses

## Prevention Strategy

### Always Check for Null in This Order:
1. ✅ Check `response.success`
2. ✅ Check `response.data != null`
3. ✅ Check `response.data.user != null`
4. ✅ Then access user properties

### Example Pattern:
```kotlin
result.fold(
    onSuccess = { response ->
        if (response.success && response.data != null && response.data.user != null) {
            // Safe to proceed
            val user = response.data.user
            // Use user properties
        } else {
            // Handle error
            _authState.value = AuthState.Error(response.message)
        }
    },
    onFailure = { exception ->
        _authState.value = AuthState.Error(exception.message ?: "Operation failed")
    }
)
```

## Files Modified

1. **Auth.kt**
   - Made `AuthData.user` nullable
   - Made `ProfileData.user` nullable

2. **AuthViewModel.kt**
   - Added null check in `register()` method
   - Added null check in `login()` method
   - Added null check in `updateProfile()` method

## Testing

After this fix, the app will:
- ✅ Handle null user responses gracefully
- ✅ Show error message instead of crashing
- ✅ Allow user to retry the operation
- ✅ Maintain app stability

## Result

🎉 **The NullPointerException is now fixed!**

The app will no longer crash when:
- API returns incomplete data
- Network issues cause partial responses
- Server errors result in null user objects
- Any edge case where user data is missing

The user will see a proper error message instead of a crash.

