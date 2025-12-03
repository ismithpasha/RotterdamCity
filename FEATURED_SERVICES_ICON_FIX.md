# Featured Services Icon Loading Fix

## Problem
Icons were not loading in the "Frequently used" section (Featured Services) on the home screen.

## Root Cause
The API has **inconsistent field naming** across different endpoints:

1. **Categories Endpoint** (`/api/v1/categories`):
   - Returns `icon_url` field for icons
   - Example: `"icon_url": "https://rotterdam.dreamdiver.nl/storage/icons/..."`

2. **Featured Services Endpoint** (`/api/v1/services/featured`):
   - Returns nested `category` object with `icon` field (NOT `icon_url`)
   - Example: `"category": {"id": 4, "icon": "https://..."}`

The Category model was using `@SerializedName("icon_url")` which only worked for the categories endpoint but failed for the featured services endpoint's nested category.

## Solution
Updated the `Category` data model to handle **both field names** using Gson's `alternate` parameter:

```kotlin
data class Category(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    @SerializedName(value = "icon", alternate = ["icon_url"])  // ← Handles both!
    val icon: String,
    val status: String,
    @SerializedName("services_count")
    val servicesCount: Int? = null,  // Made optional for nested categories
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
```

### How It Works
- `@SerializedName(value = "icon", alternate = ["icon_url"])`
  - **Primary field name**: `"icon"` - Used for featured services API
  - **Alternate field name**: `"icon_url"` - Used for categories API
  - Gson will try the primary name first, then fall back to alternates

### Additional Changes
- Made `servicesCount` optional (`Int?`) since nested category objects in featured services don't include this field

## Files Modified
- `app/src/main/java/com/dreamdiver/rotterdam/data/model/Category.kt`

## Testing
After this fix:
1. ✅ Featured services icons load correctly from `/api/v1/services/featured`
2. ✅ Category icons still load correctly from `/api/v1/categories`
3. ✅ All Services section icons continue to work
4. ✅ SubCategory icons work (they use `icon_url`)

## API Inconsistency Note
The backend has inconsistent naming:
- `/api/v1/categories` → uses `icon_url`
- `/api/v1/services/featured` → nested category uses `icon`

This fix handles both cases gracefully without requiring backend changes.

