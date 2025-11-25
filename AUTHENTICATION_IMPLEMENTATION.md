# Rotterdam City App - Authentication System Implementation

## Overview
A complete user registration and login system has been implemented for the Rotterdam City Android app with support for three user types: Admin, General, and Worker. Only General and Worker users can register from the app.

## API Endpoints

### Base URL
```
https://rotterdam.dreamdiver.nl
```

### Authentication Endpoints

#### 1. Register (POST)
**Endpoint:** `/api/v1/auth/register`

**Request Body:**
```json
{
  "name": "John Worker",
  "email": "worker@example.com",
  "password": "password",
  "password_confirmation": "password",
  "user_type": "worker",  // "general" or "worker"
  "phone": "+1234567890",
  "city": "New York",
  "skill_category": "electrician",  // Optional, for workers only
  "hourly_rate": 75.00  // Optional, for workers only
}
```

#### 2. Login (POST)
**Endpoint:** `/api/v1/auth/login`

**Request Body:**
```json
{
  "email": "worker@example.com",
  "password": "password"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user": {
      "id": 1,
      "name": "John Worker",
      "email": "worker@example.com",
      "user_type": "worker",
      "phone": "+1234567890",
      "city": "New York",
      "skill_category": "electrician",
      "hourly_rate": 75.00,
      "created_at": "2025-11-25T10:00:00.000000Z",
      "updated_at": "2025-11-25T10:00:00.000000Z"
    },
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
  }
}
```

#### 3. Update Profile (PUT)
**Endpoint:** `/api/v1/auth/profile`

**Headers:**
```
Authorization: Bearer YOUR_TOKEN
Content-Type: multipart/form-data
```

**Request Body (All Users):**
```json
{
  "name": "John Doe",
  "phone": "+31 6 12345678",
  "avatar": "file upload (image)",
  "address": "Coolsingel 40",
  "city": "Rotterdam",
  "state": "Zuid-Holland",
  "zip_code": "3011 AD",
  "latitude": 51.9225,
  "longitude": 4.47917
}
```

**Additional Fields for Workers:**
```json
{
  "skill_category": "electrician",
  "skills": ["Wiring", "Panel Installation", "Lighting"],
  "bio": "Professional electrician with 10+ years experience",
  "hourly_rate": 75.50,
  "experience_years": 10,
  "certifications": ["Licensed Electrician", "OSHA Certified"],
  "availability": {
    "monday": "9am-5pm",
    "tuesday": "9am-5pm",
    "wednesday": "9am-5pm"
  }
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Profile updated successfully",
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
    "avatar_url": "http://yourapp.com/storage/avatars/12345_1234567890.jpg",
    "average_rating": 4.8,
    "total_ratings": 15
  }
}
```

**Response (Validation Error):**
```json
{
  "success": false,
  "message": "Validation error",
  "errors": {
    "phone": ["The phone field must not be greater than 20 characters."],
    "hourly_rate": ["The hourly rate must be at least 0."]
  }
}
```

## Files Created

### 1. Data Models
**File:** `app/src/main/java/com/dreamdiver/rotterdam/data/model/Auth.kt`
- `RegisterRequest` - Request model for registration
- `LoginRequest` - Request model for login
- `AuthResponse` - Response model for authentication
- `AuthData` - User data with token
- `User` - User model with comprehensive profile information
- `WorkerProfile` - Worker-specific profile data
- `ProfileUpdateResponse` - Response model for profile updates
- `ProfileData` - Enhanced profile data with ratings

### 2. API Service
**File:** `app/src/main/java/com/dreamdiver/rotterdam/data/api/ApiService.kt`
- Added `register()` endpoint
- Added `login()` endpoint
- Added `updateProfile()` endpoint with multipart support for avatar upload

### 3. Repository
**File:** `app/src/main/java/com/dreamdiver/rotterdam/data/repository/AuthRepository.kt`
- Handles all authentication API calls
- Returns Result<T> for success/failure handling
- Supports multipart file upload for avatar images
- Handles JSON serialization for arrays and objects

### 4. ViewModel
**File:** `app/src/main/java/com/dreamdiver/rotterdam/ui/viewmodel/AuthViewModel.kt`
- Manages authentication state (Idle, Loading, Success, Error)
- Handles login, register, logout, and profile update operations
- Manages user session with PreferencesManager
- Handles validation error display from API

### 5. UI Screens

#### LoginScreen
**File:** `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/LoginScreen.kt`
- Email and password fields
- Toggle password visibility
- Error message display
- Navigation to registration
- Bilingual support (English/Dutch)

#### RegisterScreen
**File:** `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/RegisterScreen.kt`
- User type selection (General/Worker)
- All required fields (name, email, phone, city, password)
- Worker-specific fields (skill category, hourly rate)
- Password confirmation
- Error message display
- Navigation to login
- Bilingual support (English/Dutch)

#### ProfileScreen (Updated)
**File:** `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/ProfileScreen.kt`
- Displays user information (name, email, phone, city, account type)
- Avatar with user icon
- Edit Profile button
- Logout button
- Bilingual support (English/Dutch)

#### EditProfileScreen (New)
**File:** `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/EditProfileScreen.kt`
- Comprehensive profile editing form
- Avatar/photo upload with image picker
- All user fields (name, phone, address, city, state, zip, coordinates)
- Worker-specific fields:
  - Skill category
  - Skills list (comma-separated)
  - Bio/description
  - Hourly rate
  - Years of experience
  - Certifications list (comma-separated)
  - Weekly availability schedule
- Real-time validation
- Error message display
- Multipart file upload support
- Bilingual support (English/Dutch)

### 6. Data Storage
**File:** `app/src/main/java/com/dreamdiver/rotterdam/data/PreferencesManager.kt` (Updated)
Added user session management:
- `authToken` - Authentication token
- `userId` - User ID
- `userName` - User name
- `userEmail` - User email
- `userType` - User type (admin/general/worker)
- `userPhone` - User phone number
- `userCity` - User city
- `isLoggedIn` - Login status

Methods:
- `saveUserData()` - Save user session
- `clearUserData()` - Clear user session (logout)

### 7. MainActivity (Updated)
**File:** `app/src/main/java/com/dreamdiver/rotterdam/MainActivity.kt`
- Integrated AuthViewModel
- Added authentication screen navigation
- Shows login/register screens for unauthenticated users
- Added EditProfileScreen navigation
- Passes required parameters to ProfileScreen and EditProfileScreen

## Features

### 1. User Types
- **General User:** Standard user with basic information
- **Worker:** User with additional professional information (skill category, hourly rate)
- **Admin:** Cannot register from app (admin-only)

### 2. Registration Flow
1. User selects account type (General or Worker)
2. Fills in required information:
   - Name
   - Email
   - Phone
   - City (optional)
   - Password & confirmation
3. Workers also provide:
   - Skill category
   - Hourly rate
4. On success, user is automatically logged in

### 3. Login Flow
1. User enters email and password
2. On success, user data and token are saved
3. User is redirected to the app

### 4. Session Management
- User session persists using DataStore
- Authentication token stored securely
- User data cached locally
- Logout clears all session data

### 5. Profile Screen
- Displays current user information
- Shows account type badge
- Edit Profile button
- Logout functionality
- Bilingual interface

### 6. Profile Editing
- Full profile update functionality
- Image upload for avatar
- All basic fields editable (name, phone, address, location)
- Worker-specific professional information
- Skills and certifications management
- Availability schedule for workers
- Form validation with error messages
- Success/error feedback
- Auto-saves updated data to local storage

### 7. Security Features
- Password visibility toggle
- Password confirmation validation
- Secure token storage
- Automatic session management

## Bilingual Support
All authentication screens support both English and Dutch:
- Login/Register screens
- Form labels and buttons
- Error messages
- Profile information

## User Flow

### First Time User
1. App opens → Login Screen
2. Click "Sign Up" → Registration Screen
3. Fill form and register → Auto login → Home Screen

### Returning User
1. App opens → Checks session
2. If logged in → Home Screen
3. If not logged in → Login Screen

### Logged In User
1. Navigate to Profile tab
2. View personal information
3. Click "Logout" to sign out

## Testing
To test the authentication system:

1. **Registration:**
   - Open app (will show login screen if not logged in)
   - Click "Sign Up"
   - Select user type (General or Worker)
   - Fill in all required fields
   - Submit registration

2. **Login:**
   - Enter registered email and password
   - Click "Sign In"
   - Should redirect to home screen

3. **Profile:**
   - Navigate to Profile tab
   - View user information
   - Click "Edit Profile" button
   - Update profile fields
   - Upload new avatar (optional)
   - For workers: Update professional info
   - Click "Update Profile"
   - Verify changes are saved
   - Test logout functionality

4. **Session Persistence:**
   - Login successfully
   - Close app completely
   - Reopen app
   - Should remain logged in

## Dependencies
All required dependencies are already included in the project:
- Retrofit for API calls
- OkHttp for logging
- DataStore for local storage
- Jetpack Compose for UI
- Material3 for design components

## Notes
- Admin users cannot register from the app
- All API errors are handled and displayed to users
- Form validation ensures data integrity
- Passwords are securely transmitted to the server
- The authentication token should be included in future API requests for protected endpoints

