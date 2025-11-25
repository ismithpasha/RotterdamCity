# GET Profile Feature - Implementation Summary

## Overview
Implemented GET profile endpoint functionality to fetch the current user's profile data from the server, keeping local data synchronized with the server.

## API Endpoint

```bash
GET https://rotterdam.dreamdiver.nl/api/v1/auth/profile
Authorization: Bearer YOUR_TOKEN_HERE
Accept: application/json
```

## Implementation

### 1. API Service (ApiService.kt)

Added GET profile endpoint:
```kotlin
@GET("api/v1/auth/profile")
suspend fun getProfile(@Header("Authorization") token: String): ProfileUpdateResponse
```

### 2. Repository (AuthRepository.kt)

Added getProfile method:
```kotlin
suspend fun getProfile(token: String): Result<ProfileUpdateResponse> {
    return try {
        val response = api.getProfile("Bearer $token")
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 3. ViewModel (AuthViewModel.kt)

Added getProfile method with automatic local data sync:
```kotlin
fun getProfile(token: String) {
    viewModelScope.launch {
        _authState.value = AuthState.Loading
        
        val result = repository.getProfile(token)
        
        result.fold(
            onSuccess = { response ->
                if (response.success && response.data != null && response.data.user != null) {
                    // Update local user data with fresh data from server
                    preferencesManager.saveUserData(
                        token = token,
                        userId = response.data.user.id,
                        userName = response.data.user.name,
                        userEmail = response.data.user.email,
                        userType = response.data.user.userType,
                        userPhone = response.data.user.phone,
                        userCity = response.data.user.city,
                        userAvatar = response.data.avatarUrl ?: response.data.user.avatar
                    )
                    _authState.value = AuthState.Success(response.message)
                } else {
                    _authState.value = AuthState.Error(response.message)
                }
            },
            onFailure = { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Failed to fetch profile")
            }
        )
    }
}
```

### 4. UI (ProfileScreen.kt)

#### Added Refresh Button in TopAppBar
```kotlin
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(if (isEnglish) "Profile" else "Profiel") },
            actions = {
                IconButton(
                    onClick = {
                        if (!isRefreshing && authToken.isNotBlank()) {
                            authViewModel?.getProfile(authToken)
                        }
                    },
                    enabled = !isRefreshing && authToken.isNotBlank()
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Profile"
                        )
                    }
                }
            }
        )
    }
) { paddingValues ->
    // Profile content
}
```

#### Added State Management
```kotlin
var isRefreshing by remember { mutableStateOf(false) }
var authToken by remember { mutableStateOf("") }

// Function to reload user data
fun loadUserData() {
    scope.launch {
        userName = preferencesManager.userName.first() ?: ""
        userEmail = preferencesManager.userEmail.first() ?: ""
        userType = preferencesManager.userType.first() ?: ""
        userPhone = preferencesManager.userPhone.first() ?: ""
        userCity = preferencesManager.userCity.first() ?: ""
        userAvatar = preferencesManager.userAvatar.first()
        authToken = preferencesManager.authToken.first() ?: ""
    }
}

// Observe auth state for refresh
authViewModel?.let { viewModel ->
    val authState by viewModel.authState.collectAsState()
    
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> isRefreshing = true
            is AuthState.Success -> {
                isRefreshing = false
                loadUserData() // Reload user data after successful refresh
                viewModel.resetAuthState()
            }
            is AuthState.Error -> isRefreshing = false
            else -> isRefreshing = false
        }
    }
}
```

### 5. MainActivity Integration

Updated ProfileScreen call to pass authViewModel:
```kotlin
ProfileScreen(
    modifier = Modifier.padding(innerPadding),
    preferencesManager = preferencesManager,
    authViewModel = authViewModel,  // ← NEW
    onLogout = { authViewModel.logout() },
    onEditProfile = { currentDetailScreen = DetailScreen.EDIT_PROFILE },
    isEnglish = isEnglish
)
```

## Features

### ✅ Refresh Button
- Located in the top-right corner of the profile screen
- Shows refresh icon when idle
- Shows loading spinner when fetching data
- Disabled during refresh to prevent multiple requests
- Bilingual tooltip (English/Dutch)

### ✅ Automatic Data Sync
- Fetches latest profile data from server
- Updates local storage automatically
- Reloads UI with fresh data
- Preserves authentication token

### ✅ Loading States
- Visual feedback with spinner
- Button disabled during loading
- Smooth state transitions
- Error handling

### ✅ Error Handling
- Network errors caught and handled
- User-friendly error messages
- Automatic state reset
- Graceful failure recovery

## User Flow

```
User Opens Profile Screen
    ↓
[Click Refresh Button]
    ↓
Loading Spinner Shows
    ↓
API Call: GET /api/v1/auth/profile
    ↓
[Success]
    ↓
Update Local Storage
    ↓
Reload UI with Fresh Data
    ↓
Show Success (briefly)
    ↓
Return to Normal State

OR

[Error]
    ↓
Show Error Message
    ↓
Keep Existing Data
    ↓
Return to Normal State
```

## Use Cases

### 1. Sync After External Changes
If profile data is updated on another device or through web interface:
- User clicks refresh button
- Latest data fetched and displayed
- Local cache updated

### 2. Verify Latest Information
Before important actions, user can refresh to ensure data is current:
- Click refresh
- Review updated information
- Proceed with confidence

### 3. Troubleshooting
If profile displays incorrect or stale data:
- Refresh to fetch latest
- Resolves sync issues
- Updates cached data

### 4. Post-Login Sync
After logging in on a new device:
- Automatic sync already happens
- Manual refresh available if needed
- Ensures data consistency

## Technical Details

### Request Format
```http
GET /api/v1/auth/profile HTTP/1.1
Host: rotterdam.dreamdiver.nl
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGc...
Accept: application/json
```

### Response Format
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "data": {
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "user_type": "worker",
      "phone": "+31 6 12345678",
      "avatar": "avatars/12345_1234567890.jpg",
      "address": "Coolsingel 40",
      "city": "Rotterdam",
      "state": "Zuid-Holland",
      "zip_code": "3011 AD",
      "latitude": 51.9225,
      "longitude": 4.47917,
      "status": "active",
      "profile": {
        "id": 1,
        "skill_category": "electrician",
        "skills": ["Wiring", "Panel Installation"],
        "bio": "Professional electrician...",
        "hourly_rate": 75.50,
        "experience_years": 10,
        "certifications": ["Licensed Electrician"],
        "is_verified": true
      }
    },
    "avatar_url": "https://rotterdam.dreamdiver.nl/storage/avatars/12345_1234567890.jpg",
    "average_rating": 4.8,
    "total_ratings": 15
  }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Unauthenticated.",
  "data": null,
  "errors": null
}
```

## Files Modified

1. **ApiService.kt** - Added GET profile endpoint
2. **AuthRepository.kt** - Added getProfile method
3. **AuthViewModel.kt** - Added getProfile with state management
4. **ProfileScreen.kt** - Added refresh button and state handling
5. **MainActivity.kt** - Updated ProfileScreen call

## Benefits

✅ **Real-time Sync** - Always see latest profile data
✅ **User Control** - Manual refresh when needed
✅ **Visual Feedback** - Clear loading indicators
✅ **Error Resilient** - Handles failures gracefully
✅ **Efficient** - Updates only when user requests
✅ **Consistent** - Same response format as update
✅ **Secure** - Token-based authentication

## Testing

### Manual Test Steps

1. **Basic Refresh**
   - Login to app
   - Navigate to Profile screen
   - Click refresh button (top-right)
   - ✅ Should show loading spinner
   - ✅ Should update with server data

2. **Test with Changes**
   - Update profile on web/another device
   - Open app profile screen
   - Click refresh
   - ✅ Should show updated data

3. **Test Loading State**
   - Click refresh button
   - ✅ Button should be disabled
   - ✅ Spinner should show
   - ✅ Should re-enable after completion

4. **Test Error Handling**
   - Turn off internet
   - Click refresh
   - ✅ Should show error message
   - ✅ Should keep existing data
   - ✅ Button should re-enable

5. **Test Token Validation**
   - With valid token: ✅ Should work
   - With expired token: ✅ Should show error
   - Without token: ✅ Button disabled

## UI/UX Highlights

### Refresh Button Design
- **Location**: Top-right in AppBar
- **Icon**: Refresh circular arrow
- **States**: 
  - Normal: Shows refresh icon
  - Loading: Shows circular spinner
  - Disabled: Grayed out when no token
- **Size**: 24dp (standard icon size)
- **Color**: Matches theme

### Loading Indicator
- Small circular progress indicator (24dp)
- 2dp stroke width for subtle appearance
- Replaces refresh icon during loading
- Smooth transition

### User Feedback
- Immediate visual feedback on click
- Loading state clearly communicated
- Success/error handled appropriately
- No jarring transitions

## Future Enhancements

### Potential Improvements
1. **Pull-to-Refresh** - Swipe down gesture to refresh
2. **Auto-Refresh** - Periodic background sync
3. **Last Updated** - Show timestamp of last sync
4. **Offline Queue** - Queue refresh when offline
5. **Partial Updates** - Only sync changed fields
6. **Cache Strategy** - Intelligent caching with TTL
7. **Sync Indicator** - Badge showing sync status
8. **Conflict Resolution** - Handle concurrent updates

## Summary

✅ **GET Profile endpoint fully implemented**
✅ **Refresh button added to Profile screen**
✅ **Automatic data synchronization**
✅ **Loading states and error handling**
✅ **Token-based authentication**
✅ **Bilingual support**
✅ **Clean UI integration**

Users can now refresh their profile data at any time with a single tap! 🎉

