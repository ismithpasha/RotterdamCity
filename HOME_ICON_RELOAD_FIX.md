# Home Screen Icon Loading Fix

## Problem
1. Category icons were not showing on the home screen
2. Home screen data was not refreshing when returning from other bottom navigation tabs

## Root Cause
- API returns `icon_url` field but Category model was expecting `icon` field
- Missing network security configuration to allow cleartext traffic
- No mechanism to reload home data when switching back from other tabs

## Solution Implemented

### 1. API Field Mapping Fix
**Updated:** `app/src/main/java/com/dreamdiver/rotterdam/data/model/Category.kt`
- Added `@SerializedName("icon_url")` annotation to map the API's `icon_url` field to the `icon` property
- This ensures Gson correctly deserializes the icon URL from the API response

```kotlin
data class Category(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    @SerializedName("icon_url")  // ← Added this annotation
    val icon: String,
    // ...other fields
)
```

### 2. Network Security Configuration
**Created:** `app/src/main/res/xml/network_security_config.xml`
- Allows cleartext traffic for the rotterdam.dreamdiver.nl domain
- Trusts system and user certificates
- Enables HTTP/HTTPS image loading from the API

**Updated:** `app/src/main/AndroidManifest.xml`
- Added `android:networkSecurityConfig="@xml/network_security_config"`
- Added `android:usesCleartextTraffic="true"` as fallback

### 3. Home Screen Auto-Reload
**Updated:** `app/src/main/java/com/dreamdiver/rotterdam/MainActivity.kt`

**Changes:**
1. Added imports:
   - `androidx.compose.runtime.LaunchedEffect`
   - `androidx.lifecycle.viewmodel.compose.viewModel`
   - `com.dreamdiver.rotterdam.ui.viewmodel.HomeViewModel`

2. Created shared `HomeViewModel` instance in `CumillaCityApp`:
   ```kotlin
   val homeViewModel: HomeViewModel = viewModel()
   ```

3. Added `LaunchedEffect` to reload data when HOME destination is active:
   ```kotlin
   LaunchedEffect(currentDestination) {
       if (currentDestination == AppDestinations.HOME) {
           homeViewModel.loadCategories()
       }
   }
   ```

4. Passed the shared `homeViewModel` to `HomeScreen` composable

## How It Works

### Icon Loading
- AsyncImage (Coil) now has permission to load images from HTTP/HTTPS sources
- Network security config allows cleartext traffic for the API domain
- Images from `https://rotterdam.dreamdiver.nl/storage/icons/*` will load properly

### Auto-Reload Behavior
- When user navigates to HOME from any other tab (Favorites, Profile, More)
- `LaunchedEffect` detects the destination change
- Triggers `homeViewModel.loadCategories()` to refresh the data
- Shows loading indicator while fetching
- Updates UI with fresh category data and icons

## Testing
1. Launch the app
2. Navigate to Home - icons should be visible
3. Go to another tab (Favorites, Profile, or More)
4. Return to Home - data should reload (brief loading indicator)
5. Icons should remain visible throughout

## Files Modified
1. `app/src/main/java/com/dreamdiver/rotterdam/data/model/Category.kt` - Added `@SerializedName("icon_url")` annotation
2. `app/src/main/AndroidManifest.xml` - Added network security config
3. `app/src/main/res/xml/network_security_config.xml` - Created new file
4. `app/src/main/java/com/dreamdiver/rotterdam/MainActivity.kt` - Added auto-reload logic

## Dependencies
- Coil 2.5.0 (already configured)
- Kotlin Coroutines (already configured)
- Jetpack Compose (already configured)

