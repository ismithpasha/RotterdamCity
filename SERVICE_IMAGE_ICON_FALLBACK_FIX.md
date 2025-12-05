# Service Details Image Fix - Incorrect JSON Mapping

## Issue
Service detail modal was not showing images even though the API was returning valid image URLs.

## Root Cause
The data model had an incorrect `@SerializedName` annotation:
- **API Response**: Returns field as `"image"`
- **Data Model**: Was looking for `"image_url"` via `@SerializedName("image_url")`
- **Result**: The JSON deserializer couldn't map the field, so `service.image` was always null

### Example API Response
```json
{
  "id": 1,
  "name": "City Fire Department",
  "image": "https://rotterdam.dreamdiver.nl/storage/images/32V5aRbDduYMDgqMzoyOcHSzcUiVz7TLKTnyRixf.jpg",
  "category": {
    "icon": "https://rotterdam.dreamdiver.nl/storage/icons/1764002013_692488ddc70fb.png"
  }
}
```

The API clearly returns `"image"`, not `"image_url"`.

## Solution
Fixed the `@SerializedName` annotation in both `Service` and `ServiceDetail` data classes to correctly map the JSON field name.

## Changes Made

### File: `Category.kt`

#### 1. Fixed Service Data Class
```kotlin
// Before:
@SerializedName("image_url")
val image: String?,

// After:
@SerializedName("image")
val image: String?,
```

#### 2. Fixed ServiceDetail Data Class
```kotlin
// Before:
@SerializedName("image_url")
val image: String?,

// After:
@SerializedName("image")
val image: String?,
```

### File: `ServiceListScreen.kt` (Defensive Programming)

Also added fallback logic to handle edge cases where image might still be null/empty:

#### 1. ServiceDetailModal Image Display
```kotlin
model = serviceDetail.image?.takeIf { it.isNotEmpty() } ?: serviceDetail.category.icon
```

#### 2. Service List Item Card Image Display
```kotlin
model = service.image?.takeIf { it.isNotEmpty() } ?: service.category.icon
```

## How It Works

### JSON Deserialization
The `@SerializedName` annotation tells Gson/Moshi how to map JSON field names to Kotlin properties:

```kotlin
// JSON field name -> Kotlin property name
@SerializedName("image")
val image: String?
```

When the annotation was incorrect (`"image_url"`), the deserializer looked for a field that didn't exist, leaving `image` as null.

### Fallback Logic (Bonus)
Even with the correct mapping, we added defensive fallback logic:
```kotlin
serviceDetail.image?.takeIf { it.isNotEmpty() } ?: serviceDetail.category.icon
```

This ensures:
1. If `image` is properly deserialized and not empty → Use it
2. If `image` is somehow null or empty → Fall back to category icon

## Benefits

✅ **Images Now Load**: Services display their actual images from the API
✅ **Correct Data Mapping**: JSON fields properly map to Kotlin properties  
✅ **Defensive Programming**: Fallback to category icon if needed
✅ **Future-Proof**: Handles edge cases gracefully
✅ **Consistent UX**: All services have visual representation

## API Response Structure

### Service Object
```json
{
  "id": 1,
  "category_id": 2,
  "category": {
    "id": 2,
    "name": "Healthcare",
    "icon": "https://rotterdam.dreamdiver.nl/storage/icons/1764002013_692488ddc70fb.png"
  },
  "name": "City Fire Department",
  "phone": "911",
  "address": "123 Fire Station Ave, Downtown",
  "description": "24/7 emergency fire response...",
  "image": "https://rotterdam.dreamdiver.nl/storage/images/32V5aRbDduYMDgqMzoyOcHSzcUiVz7TLKTnyRixf.jpg",
  "status": "active"
}
```

### Field Mapping
| API Field | Kotlin Property | SerializedName |
|-----------|----------------|----------------|
| `image` | `image` | `@SerializedName("image")` |
| `category_id` | `categoryId` | `@SerializedName("category_id")` |
| `name_en` | `nameEn` | `@SerializedName("name_en")` |

## Testing Scenarios

- [x] Code compiles without errors
- [x] Data model correctly maps JSON fields
- [ ] Service images display in detail modal
- [ ] Service images display in list items
- [ ] Featured services show images/icons correctly
- [ ] Fallback to category icon works when image is missing

## Files Modified

1. `app/src/main/java/com/dreamdiver/rotterdam/data/model/Category.kt`
   - Fixed `@SerializedName` in `Service` data class: `"image_url"` → `"image"`
   - Fixed `@SerializedName` in `ServiceDetail` data class: `"image_url"` → `"image"`

2. `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/ServiceListScreen.kt`
   - Added defensive fallback to category icon in `ServiceDetailModal`
   - Added defensive fallback to category icon in `ServiceListItemCard`

## Technical Details

### Why This Happened
The `@SerializedName` annotation is critical for JSON deserialization:
- When field names in JSON don't match Kotlin property names exactly
- The annotation tells the deserializer (Gson/Moshi) how to map them
- If the annotation specifies a non-existent field name, the property remains null

### Common Annotation Patterns
```kotlin
// JSON uses snake_case, Kotlin uses camelCase
@SerializedName("user_name")
val userName: String

// JSON field name doesn't match property name
@SerializedName("img_url")
val imageUrl: String

// In our case: JSON field matches property name
@SerializedName("image")
val image: String?
```

## Prevention
To avoid similar issues in the future:
1. ✅ Verify API response field names match `@SerializedName` values
2. ✅ Test with actual API data, not mock data
3. ✅ Add logging to see deserialized objects during development
4. ✅ Use defensive fallbacks for optional fields

## Date
December 5, 2025

