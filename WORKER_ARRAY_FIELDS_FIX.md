# Worker Profile Array Fields Fix - Laravel Format

## Update Summary
Updated the profile update API to send array fields (skills, certifications) in Laravel's expected format using `[]` notation instead of JSON strings.

## Issue
The API expects array fields to be sent as multiple form fields with `[]` notation:
```
skills[]=Pipe Repair
skills[]=Drain Cleaning
certifications[]=Licensed Plumber
```

But the app was sending them as JSON strings:
```
skills=["Pipe Repair","Drain Cleaning"]
certifications=["Licensed Plumber"]
```

## Solution Implemented

### Laravel Array Format
Laravel expects arrays to be submitted as multiple form fields with the same name followed by `[]`:

**Correct Format:**
```http
POST /api/v1/auth/profile
Content-Type: multipart/form-data

skills[]=Pipe Repair
skills[]=Drain Cleaning
certifications[]=Licensed Plumber
```

**Old Format (Incorrect):**
```http
POST /api/v1/auth/profile
Content-Type: multipart/form-data

skills=["Pipe Repair","Drain Cleaning"]
certifications=["Licensed Plumber"]
```

## Changes Made

### 1. ApiService.kt

#### Before
```kotlin
@Part("skills") skills: RequestBody?,
@Part("certifications") certifications: RequestBody?,
```

#### After
```kotlin
@Part("skills[]") skills: List<RequestBody>?,
@Part("certifications[]") certifications: List<RequestBody>?,
```

**Key Changes:**
- ✅ Changed parameter names from `"skills"` to `"skills[]"`
- ✅ Changed parameter names from `"certifications"` to `"certifications[]"`
- ✅ Changed type from `RequestBody?` to `List<RequestBody>?`

### 2. AuthRepository.kt

#### Before (JSON String Approach)
```kotlin
val skillsPart = skills?.let {
    Gson().toJson(it).toRequestBody("application/json".toMediaTypeOrNull())
}
val certificationsPart = certifications?.let {
    Gson().toJson(it).toRequestBody("application/json".toMediaTypeOrNull())
}

// Pass single RequestBody
skills = skillsPart,
certifications = certificationsPart,
```

#### After (Laravel Array Format)
```kotlin
// Convert skills list to List<RequestBody> for Laravel array format (skills[])
val skillsParts = skills?.map { skill ->
    skill.toRequestBody("text/plain".toMediaTypeOrNull())
}

// Convert certifications list to List<RequestBody> for Laravel array format (certifications[])
val certificationsParts = certifications?.map { cert ->
    cert.toRequestBody("text/plain".toMediaTypeOrNull())
}

// Pass List<RequestBody>
skills = skillsParts,
certifications = certificationsParts,
```

**Key Changes:**
- ✅ Convert each array item to individual RequestBody
- ✅ Send as List<RequestBody> instead of JSON string
- ✅ Use text/plain content type for each item

### 3. Availability Field
The `availability` field remains as JSON string because it's an object/map, not a simple array:
```kotlin
val availabilityPart = availability?.let {
    Gson().toJson(it).toRequestBody("text/plain".toMediaTypeOrNull())
}
```

## API Request Examples

### cURL Command (Correct Format)
```bash
curl -X POST http://127.0.0.1:8000/api/v1/auth/profile \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Accept: application/json" \
  -F "_method=PUT" \
  -F "skill_category=Plumbing" \
  -F "skills[]=Pipe Repair" \
  -F "skills[]=Drain Cleaning" \
  -F "skills[]=Installation" \
  -F "bio=Experienced plumber in Rotterdam" \
  -F "hourly_rate=45.50" \
  -F "experience_years=5" \
  -F "certifications[]=Licensed Plumber" \
  -F "certifications[]=OSHA Certified" \
  -F 'availability={"monday":"9-5","tuesday":"9-5"}'
```

### Android App Usage (Automatic)
```kotlin
authViewModel.updateProfile(
    token = token,
    name = "John Plumber",
    skillCategory = "Plumbing",
    skills = listOf("Pipe Repair", "Drain Cleaning", "Installation"),
    bio = "Experienced plumber in Rotterdam",
    hourlyRate = 45.50,
    experienceYears = 5,
    certifications = listOf("Licensed Plumber", "OSHA Certified"),
    availability = mapOf(
        "monday" to "9-5",
        "tuesday" to "9-5"
    )
)
```

## Request Structure

### Complete Multipart Request
```http
POST /api/v1/auth/profile HTTP/1.1
Authorization: Bearer TOKEN
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary

------WebKitFormBoundary
Content-Disposition: form-data; name="_method"

PUT
------WebKitFormBoundary
Content-Disposition: form-data; name="skill_category"

Plumbing
------WebKitFormBoundary
Content-Disposition: form-data; name="skills[]"

Pipe Repair
------WebKitFormBoundary
Content-Disposition: form-data; name="skills[]"

Drain Cleaning
------WebKitFormBoundary
Content-Disposition: form-data; name="skills[]"

Installation
------WebKitFormBoundary
Content-Disposition: form-data; name="bio"

Experienced plumber in Rotterdam
------WebKitFormBoundary
Content-Disposition: form-data; name="hourly_rate"

45.50
------WebKitFormBoundary
Content-Disposition: form-data; name="experience_years"

5
------WebKitFormBoundary
Content-Disposition: form-data; name="certifications[]"

Licensed Plumber
------WebKitFormBoundary
Content-Disposition: form-data; name="certifications[]"

OSHA Certified
------WebKitFormBoundary
Content-Disposition: form-data; name="availability"

{"monday":"9-5","tuesday":"9-5"}
------WebKitFormBoundary--
```

## Field Types Summary

| Field | Format | Example |
|-------|--------|---------|
| skill_category | Single value | `Plumbing` |
| skills | Array (multiple fields) | `skills[]=Pipe Repair`<br>`skills[]=Drain Cleaning` |
| bio | Single value | `Experienced plumber...` |
| hourly_rate | Single value | `45.50` |
| experience_years | Single value | `5` |
| certifications | Array (multiple fields) | `certifications[]=Licensed`<br>`certifications[]=OSHA` |
| availability | JSON object | `{"monday":"9-5"}` |

## Laravel Server-Side Handling

### How Laravel Processes This
```php
// Laravel automatically converts skills[] to array
$request->input('skills');
// Returns: ['Pipe Repair', 'Drain Cleaning', 'Installation']

$request->input('certifications');
// Returns: ['Licensed Plumber', 'OSHA Certified']

$request->input('availability');
// Returns: {"monday":"9-5","tuesday":"9-5"} (string)
// Need to json_decode() on server
```

## Data Flow

### Client to Server
```
Android List<String>
    ↓
Convert to List<RequestBody>
    ↓
Send as multiple form fields (skills[], skills[], skills[])
    ↓
Laravel receives as PHP array
    ↓
Stored in database as JSON
```

### Server to Client
```
Database JSON array
    ↓
Laravel returns as JSON array
    ↓
Android receives as List<String>
    ↓
Display in UI
```

## Why This Format?

### HTML Form Standard
This format comes from HTML form submissions:
```html
<form>
  <input name="skills[]" value="Pipe Repair">
  <input name="skills[]" value="Drain Cleaning">
</form>
```

### PHP/Laravel Convention
PHP and Laravel automatically convert `skills[]` to an array:
```php
// Submitted: skills[]=A&skills[]=B&skills[]=C
// PHP creates: $_POST['skills'] = ['A', 'B', 'C']
```

### Benefits
- ✅ Native PHP/Laravel support
- ✅ Automatic array handling
- ✅ No JSON parsing needed
- ✅ Works with validation rules
- ✅ Standard web convention

## Comparison

### JSON String Approach (Old)
```
Pros:
- Simple to encode
- Single field

Cons:
- Laravel needs manual json_decode()
- Validation more complex
- Not standard form format
- May break with special characters
```

### Array Fields Approach (New)
```
Pros:
- Native Laravel support ✅
- Automatic array conversion ✅
- Standard form format ✅
- Better validation ✅
- More reliable ✅

Cons:
- Slightly more code client-side
- Multiple form fields
```

## Testing

### Test Cases

1. **Empty Arrays**
   ```kotlin
   skills = emptyList()  // Don't send any skills[] fields
   ```
   ✅ Should update profile without skills

2. **Single Item Array**
   ```kotlin
   skills = listOf("Pipe Repair")  // Send one skills[] field
   ```
   ✅ Should send one skills[] field correctly

3. **Multiple Items Array**
   ```kotlin
   skills = listOf("A", "B", "C")  // Send three skills[] fields
   ```
   ✅ Should send multiple skills[] fields correctly

4. **Special Characters**
   ```kotlin
   skills = listOf("Pipe & Drain", "24/7 Service")
   ```
   ✅ Should handle special characters correctly

5. **Null Arrays**
   ```kotlin
   skills = null  // Don't send skills[] at all
   ```
   ✅ Should update other fields without sending skills

## Validation

### Server-Side Validation (Laravel)
```php
$request->validate([
    'skill_category' => 'required|string',
    'skills' => 'array',
    'skills.*' => 'string|max:255',
    'certifications' => 'array',
    'certifications.*' => 'string|max:255',
    'availability' => 'json'
]);
```

### Client-Side Validation
```kotlin
// Validate before sending
val validSkills = skills?.filter { it.isNotBlank() }
val validCertifications = certifications?.filter { it.isNotBlank() }
```

## Error Handling

### Invalid Array Format
**Error:** `The skills field must be an array.`
**Cause:** Sent as JSON string instead of array fields
**Solution:** ✅ Fixed with this update

### Array Item Validation
**Error:** `The skills.0 field must be a string.`
**Cause:** Invalid data type in array
**Solution:** Ensure all items are strings

## Files Modified

1. ✅ **ApiService.kt**
   - Changed `@Part("skills")` to `@Part("skills[]")`
   - Changed `@Part("certifications")` to `@Part("certifications[]")`
   - Changed types to `List<RequestBody>?`

2. ✅ **AuthRepository.kt**
   - Changed from JSON string encoding to List mapping
   - Each array item becomes individual RequestBody
   - Updated API call parameters

## Backward Compatibility

### Server Requirements
- ✅ Server must accept `skills[]` format
- ✅ Server must accept `certifications[]` format
- ✅ Laravel handles this natively

### Client Requirements
- ✅ No changes needed in UI
- ✅ No changes needed in ViewModel
- ✅ Works transparently

## Benefits of This Update

✅ **Native Laravel Support** - Works with Laravel's array handling
✅ **Better Validation** - Laravel validates each array item
✅ **Standard Format** - Follows web form conventions
✅ **More Reliable** - No JSON parsing issues
✅ **Cleaner Server Code** - Server doesn't need json_decode()
✅ **Better Error Messages** - Validation errors per item

## Example Responses

### Success Response
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": {
    "user": {
      "skill_category": "Plumbing",
      "profile": {
        "skills": ["Pipe Repair", "Drain Cleaning", "Installation"],
        "bio": "Experienced plumber in Rotterdam",
        "hourly_rate": 45.50,
        "experience_years": 5,
        "certifications": ["Licensed Plumber", "OSHA Certified"]
      }
    }
  }
}
```

### Validation Error Response
```json
{
  "success": false,
  "message": "Validation error",
  "errors": {
    "skills.0": ["The skills.0 field must not be greater than 255 characters."],
    "hourly_rate": ["The hourly rate must be a number."]
  }
}
```

## Summary

✅ **Changed skills from JSON string to array fields**
✅ **Changed certifications from JSON string to array fields**
✅ **Using Laravel's native array format ([])**
✅ **Better server-side validation support**
✅ **More reliable and standard approach**
✅ **No UI changes required**

The worker profile array fields now use Laravel's standard array format for better compatibility and reliability!

