# Back Button Implementation

## Overview
Implemented hardware back button handling for the Rotterdam City app with the following behavior:

## Back Button Behavior

### 1. When in Detail Screens (Not Home)
- **Action**: Navigate back to the main navigation (Home screen)
- **Screens affected**: 
  - Notice, About Us, Privacy Policy, Terms & Conditions
  - Emergency Service, Hospital List, Educational
  - Service List, Subcategory List, Edit Profile

### 2. When in Other Main Screens (Not Home)
- **Action**: Navigate to Home screen
- **Screens affected**: 
  - Favorites, Profile, More screens

### 3. When in Home Screen
- **Action**: Show exit confirmation dialog
- **Dialog Options**:
  - **Exit**: Closes the app
  - **Cancel**: Dismisses the dialog and stays in the app

## Implementation Details

### Files Modified
- `MainActivity.kt`

### Changes Made

1. **Added Imports**:
   - `androidx.activity.compose.BackHandler`
   - `androidx.compose.material3.AlertDialog`
   - `androidx.compose.material3.TextButton`
   - `androidx.compose.ui.platform.LocalContext`

2. **Added State Variables**:
   ```kotlin
   var showExitDialog by rememberSaveable { mutableStateOf(false) }
   val activity = LocalContext.current as? ComponentActivity
   ```

3. **Implemented BackHandler**:
   ```kotlin
   BackHandler(enabled = true) {
       when {
           currentDetailScreen != null -> {
               // Go back to main navigation
               currentDetailScreen = null
               serviceListState = null
               subcategoryListState = null
           }
           currentDestination != AppDestinations.HOME -> {
               // Go to home
               currentDestination = AppDestinations.HOME
           }
           else -> {
               // Show exit confirmation
               showExitDialog = true
           }
       }
   }
   ```

4. **Added Exit Confirmation Dialog**:
   - Supports both English and Bengali languages
   - English: "Exit App" / "Are you sure you want to exit?"
   - Bengali: "অ্যাপ থেকে বের হন" / "আপনি কি নিশ্চিত যে আপনি প্রস্থান করতে চান?"

## User Experience

1. **Seamless Navigation**: Users can use the back button to navigate through the app hierarchy naturally
2. **Prevent Accidental Exit**: Exit confirmation prevents users from accidentally closing the app
3. **Always Return to Home**: The back button always brings users back to the home screen before showing the exit dialog
4. **Multi-language Support**: Dialog messages appear in the user's selected language

## Testing

Test the following scenarios:
1. Press back button from detail screens → Should go to home
2. Press back button from Favorites/Profile/More → Should go to home
3. Press back button from home → Should show exit dialog
4. Press "Exit" in dialog → App should close
5. Press "Cancel" in dialog → Dialog dismisses, stay in app
6. Test in both English and Bengali languages

## Date
December 5, 2025

