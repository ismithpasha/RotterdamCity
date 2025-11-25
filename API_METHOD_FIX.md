# Profile Update API Fix - POST with _method=PUT

## Issue
The profile update endpoint was using `@PUT` annotation, but Laravel/server requires `POST` with `_method=PUT` for multipart form data uploads (file uploads).

## Root Cause
Many web frameworks, including Laravel, don't support PUT requests with multipart/form-data for file uploads. The standard workaround is to use POST with a `_method` field to specify the actual HTTP method.

## Solution Implemented

### API Endpoint Change
```
Before: PUT /api/v1/auth/profile
After:  POST /api/v1/auth/profile (with _method=PUT)
```

## Changes Made

### 1. ApiService.kt

#### Before
```kotlin
@Multipart
@PUT("api/v1/auth/profile")
suspend fun updateProfile(
    @Header("Authorization") token: String,
    @Part("name") name: RequestBody,
    // ...other parameters
): ProfileUpdateResponse
```

#### After
```kotlin
@Multipart
@POST("api/v1/auth/profile")
suspend fun updateProfile(
    @Header("Authorization") token: String,
    @Part("_method") method: RequestBody,  // ← NEW: Method spoofing
    @Part("name") name: RequestBody,
    // ...other parameters
): ProfileUpdateResponse
```

**Key Changes:**
- ✅ Changed `@PUT` to `@POST`
- ✅ Added `@Part("_method") method: RequestBody` parameter

### 2. AuthRepository.kt

#### Before
```kotlin
val response = api.updateProfile(
    token = "Bearer $token",
    name = namePart,
    // ...other parameters
)
```

#### After
```kotlin
// Laravel method spoofing for PUT with multipart
val methodPart = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())

val response = api.updateProfile(
    token = "Bearer $token",
    method = methodPart,  // ← NEW: Pass _method=PUT
    name = namePart,
    // ...other parameters
)
```

**Key Changes:**
- ✅ Created `methodPart` with value "PUT"
- ✅ Pass `method = methodPart` as first parameter after token
- ✅ Removed unused import `android.net.Uri`

## Technical Explanation

### Why POST with _method=PUT?

#### The Problem
```
HTTP PUT + multipart/form-data = Not fully supported by many frameworks
```

#### The Solution
```
HTTP POST + multipart/form-data + _method=PUT = Universally supported
```

### How It Works

1. **Client Sends POST Request**
   ```http
   POST /api/v1/auth/profile HTTP/1.1
   Content-Type: multipart/form-data; boundary=----WebKitFormBoundary
   Authorization: Bearer TOKEN
   
   ------WebKitFormBoundary
   Content-Disposition: form-data; name="_method"
   
   PUT
   ------WebKitFormBoundary
   Content-Disposition: form-data; name="name"
   
   John Doe
   ------WebKitFormBoundary
   Content-Disposition: form-data; name="avatar"; filename="photo.jpg"
   Content-Type: image/jpeg
   
   [binary data]
   ```

2. **Server Processes as PUT**
   - Server sees `_method=PUT` field
   - Routes request to PUT handler
   - Processes file upload correctly

### Laravel Method Spoofing

Laravel (and many PHP frameworks) use method spoofing:
```php
// Laravel recognizes _method field
if ($request->has('_method')) {
    $method = $request->input('_method');
    // Routes as PUT request
}
```

## API Call Example

### Using cURL
```bash
curl -X POST http://127.0.0.1:8000/api/v1/auth/profile \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Accept: application/json" \
  -F "_method=PUT" \
  -F "name=John Doe" \
  -F "phone=+31 10 123 4567" \
  -F "avatar=@/path/to/your/image.jpg" \
  -F "address=123 Main Street" \
  -F "city=Rotterdam" \
  -F "state=Zuid-Holland" \
  -F "zip_code=3011"
```

### From Android App
```kotlin
// Automatically handled by updated code
authViewModel.updateProfile(
    token = token,
    name = "John Doe",
    phone = "+31 10 123 4567",
    avatarFile = avatarFile,
    address = "123 Main Street",
    city = "Rotterdam",
    state = "Zuid-Holland",
    zipCode = "3011",
    // ...other parameters
)
```

## Request Flow

### Complete Multipart Request
```
POST /api/v1/auth/profile
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGc...
Content-Type: multipart/form-data

Parts:
├── _method: "PUT"                    ← Method spoofing
├── name: "John Doe"
├── phone: "+31 10 123 4567"
├── avatar: [image binary data]       ← File upload
├── address: "123 Main Street"
├── city: "Rotterdam"
├── state: "Zuid-Holland"
├── zip_code: "3011"
├── latitude: "51.9225"
├── longitude: "4.47917"
├── skill_category: "electrician"     ← Worker fields (optional)
├── skills: ["Wiring", "Installation"]
├── bio: "Professional electrician..."
├── hourly_rate: "75.50"
├── experience_years: "10"
├── certifications: ["Licensed Electrician"]
└── availability: {"monday": "9am-5pm"}
```

## Response Handling

### Success Response
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": {
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "+31 10 123 4567",
      "avatar": "avatars/12345_1234567890.jpg",
      "address": "123 Main Street",
      "city": "Rotterdam",
      "state": "Zuid-Holland",
      "zip_code": "3011"
    },
    "avatar_url": "https://rotterdam.dreamdiver.nl/storage/avatars/12345_1234567890.jpg"
  }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Validation error",
  "errors": {
    "phone": ["The phone field must not be greater than 20 characters."],
    "avatar": ["The avatar must be an image."]
  }
}
```

## Why This Pattern?

### HTTP Protocol Limitations
- **PUT requests** were designed for sending complete representations
- **Multipart encoding** was designed for POST forms with files
- **Combining them** creates compatibility issues

### Framework Support
| Framework | PUT + Multipart | POST + _method |
|-----------|-----------------|----------------|
| Laravel | ⚠️ Limited | ✅ Full |
| Symfony | ⚠️ Limited | ✅ Full |
| Rails | ⚠️ Limited | ✅ Full |
| Django | ⚠️ Limited | ✅ Full |
| Express | ✅ Some | ✅ Full |

### Industry Standard
- Used by major APIs (Twitter, Facebook, etc.)
- Documented in Laravel, Symfony, Rails
- Recommended by REST best practices for file uploads

## Benefits

✅ **Universal Compatibility** - Works with all web frameworks
✅ **File Upload Support** - Handles multipart/form-data correctly
✅ **RESTful Semantics** - Still follows REST principles (PUT for update)
✅ **Laravel Native** - Uses Laravel's built-in method spoofing
✅ **Proven Pattern** - Industry-standard approach

## Testing

### Test Cases

1. **Profile Update Without Avatar**
   ```kotlin
   updateProfile(
       name = "Jane Doe",
       phone = "+31 6 12345678",
       avatarFile = null  // No file
   )
   ```
   ✅ Should update profile without avatar

2. **Profile Update With Avatar**
   ```kotlin
   updateProfile(
       name = "Jane Doe",
       phone = "+31 6 12345678",
       avatarFile = File("path/to/image.jpg")  // With file
   )
   ```
   ✅ Should update profile and upload avatar

3. **Worker Profile Update**
   ```kotlin
   updateProfile(
       name = "John Worker",
       skillCategory = "electrician",
       skills = listOf("Wiring", "Installation"),
       hourlyRate = 75.50
   )
   ```
   ✅ Should update worker-specific fields

4. **Validation Errors**
   ```kotlin
   updateProfile(
       name = "",  // Invalid
       phone = "invalid"  // Invalid
   )
   ```
   ✅ Should return validation errors

## Files Modified

1. ✅ **ApiService.kt**
   - Changed `@PUT` to `@POST`
   - Added `_method` parameter

2. ✅ **AuthRepository.kt**
   - Added `methodPart` creation
   - Pass `method` parameter to API
   - Removed unused import

## Backward Compatibility

### Server Side
- Server must support `_method` field
- Laravel has this built-in
- Most modern frameworks support it

### Client Side
- All existing code continues to work
- No changes needed in ViewModels
- No changes needed in UI screens
- Transparent to users

## Common Issues & Solutions

### Issue: "Method Not Allowed"
**Cause:** Server doesn't support POST for this endpoint
**Solution:** Ensure server routes accept POST with `_method`

### Issue: "_method not recognized"
**Cause:** Server framework doesn't support method spoofing
**Solution:** Check framework documentation for enabling it

### Issue: "File not uploaded"
**Cause:** Using PUT instead of POST
**Solution:** ✅ Already fixed with this update

## Verification

### Check Request in Logs
```
POST /api/v1/auth/profile HTTP/1.1
Content-Type: multipart/form-data

Look for:
_method=PUT  ← This should be present
avatar=[binary]  ← File should be included
```

### Check Response
```json
{
  "success": true,
  "data": {
    "user": {
      "avatar": "avatars/new_file.jpg"  ← Avatar updated
    },
    "avatar_url": "https://..."  ← Full URL provided
  }
}
```

## Documentation References

- [Laravel HTTP Methods](https://laravel.com/docs/routing#form-method-spoofing)
- [RESTful API File Uploads](https://restfulapi.net/http-methods/)
- [Multipart Form Data RFC](https://www.ietf.org/rfc/rfc2388.txt)

## Summary

✅ **Fixed profile update API to use POST with _method=PUT**
✅ **Added method spoofing parameter**
✅ **Maintained all existing functionality**
✅ **Compatible with Laravel backend**
✅ **Supports file uploads correctly**
✅ **No changes needed in UI layer**

The profile update now uses the correct HTTP method pattern for multipart form data uploads with Laravel!

