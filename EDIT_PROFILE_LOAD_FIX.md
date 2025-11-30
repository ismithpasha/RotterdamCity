# Edit Profile Screen - Load Existing Information Fix

## Issue
The Edit Profile screen was not showing existing user information when opened. Only name, phone, city, and userType were being loaded, while all other fields (address, state, zip code, worker-specific fields) were blank.

## Solution
Updated the `EditProfileScreen.kt` to load all existing user data from PreferencesManager when the screen is opened.

## Changes Made

### 1. Added State Variable for Existing Avatar
```kotlin
var existingAvatarUrl by remember { mutableStateOf<String?>(null) }
```

### 2. Updated LaunchedEffect to Load All Fields

#### Basic Information (All Users)
- ✅ Name (already loaded)
- ✅ Phone (already loaded)
- ✅ City (already loaded)
- ✅ **Address** (now loaded)
- ✅ **State** (now loaded)
- ✅ **Zip Code** (now loaded)
- ✅ **Existing Avatar URL** (now loaded)

#### Worker-Specific Fields (When userType == "worker")
- ✅ **Skill Category** (now loaded)
- ✅ **Bio** (now loaded)
- ✅ **Hourly Rate** (now loaded)
- ✅ **Experience Years** (now loaded)
- ✅ **Skills** (now loaded as comma-separated string)
- ✅ **Certifications** (now loaded as comma-separated string)

### 3. Updated Avatar Display
The avatar section now shows three states:
1. **New image selected** - Shows the newly selected image from device
2. **Existing avatar** - Shows the current avatar from server if no new image is selected
3. **No avatar** - Shows camera icon if no avatar exists and none selected

## Code Changes

### LaunchedEffect Update
```kotlin
LaunchedEffect(Unit) {
    scope.launch {
        // Basic fields
        name = preferencesManager.userName.first() ?: ""
        phone = preferencesManager.userPhone.first() ?: ""
        city = preferencesManager.userCity.first() ?: ""
        address = preferencesManager.userAddress.first() ?: ""
        state = preferencesManager.userState.first() ?: ""
        zipCode = preferencesManager.userZipCode.first() ?: ""
        userType = preferencesManager.userType.first() ?: ""
        token = preferencesManager.authToken.first() ?: ""
        existingAvatarUrl = preferencesManager.userAvatar.first()
        
        // Worker-specific fields
        if (userType == "worker") {
            skillCategory = preferencesManager.userSkillCategory.first() ?: ""
            bio = preferencesManager.userBio.first() ?: ""
            hourlyRate = preferencesManager.userHourlyRate.first() ?: ""
            experienceYears = preferencesManager.userExperienceYears.first()?.toString() ?: ""
            
            val skillsString = preferencesManager.userSkills.first() ?: ""
            skillsText = if (skillsString.isNotBlank()) skillsString else ""
            
            val certificationsString = preferencesManager.userCertifications.first() ?: ""
            certificationsText = if (certificationsString.isNotBlank()) certificationsString else ""
        }
    }
}
```

### Avatar Display Logic
```kotlin
if (avatarUri != null) {
    // Show newly selected image
    AsyncImage(model = avatarUri, ...)
} else if (!existingAvatarUrl.isNullOrBlank()) {
    // Show existing avatar from server
    AsyncImage(model = existingAvatarUrl, ...)
} else {
    // Show camera icon
    Icon(imageVector = Icons.Default.CameraAlt, ...)
}
```

## User Experience Improvements

### Before Fix
- User opens Edit Profile screen
- All fields are blank except name, phone, and city
- User must re-enter all information even if they only want to change one field
- Existing avatar not shown
- Worker-specific fields completely empty

### After Fix
- User opens Edit Profile screen
- **All fields are pre-populated** with current values
- User can see their current information
- User can modify only the fields they want to change
- **Existing avatar is displayed** from server
- **Worker profile fields show current values** (skill category, bio, hourly rate, experience, skills, certifications)

## Testing Checklist

✅ Open Edit Profile as a General user
- Verify name, phone, address, city, state, zip code are populated
- Verify existing avatar is displayed

✅ Open Edit Profile as a Worker user
- Verify all basic fields are populated
- Verify skill category, bio, hourly rate, experience years are populated
- Verify skills and certifications are populated (as comma-separated text)
- Verify existing avatar is displayed

✅ Edit a field and save
- Verify updated value appears in both Edit Profile and Profile screens

✅ Select new avatar
- Verify new avatar preview replaces existing avatar
- Verify new avatar is uploaded and saved

## Benefits

✅ **Better UX** - Users see their current information
✅ **Time Saving** - Users don't need to re-enter everything
✅ **Clear State** - Users know what their current values are
✅ **Easy Updates** - Users can change just one field without re-entering all
✅ **Visual Feedback** - Existing avatar is visible before selecting new one
✅ **Complete Data** - All fields including worker-specific information are loaded

## Related Files
- `EditProfileScreen.kt` - Updated to load all existing data
- `PreferencesManager.kt` - Provides all user data fields
- `ProfileScreen.kt` - Displays all saved information

