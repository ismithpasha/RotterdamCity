# SubCategory Navigation Implementation

## Summary
Successfully implemented a new navigation flow where clicking on categories from the Home page now leads to subcategories first, and then to services.

## New Flow
1. Home Page → Categories (displayed in 3 columns)
2. Click Category → SubCategory List Screen
3. Click SubCategory → Service List Screen

## Files Created

### 1. SubCategory.kt
**Path:** `app/src/main/java/com/dreamdiver/rotterdam/data/model/SubCategory.kt`
- `SubCategoryResponse`: Response model for subcategory list API
- `SubCategory`: Model with id, name, iconUrl, servicesCount
- `SubCategoryServiceResponse`: Response model for services by subcategory
- `SubCategoryInfo`: Basic subcategory info

### 2. SubCategoryRepository.kt
**Path:** `app/src/main/java/com/dreamdiver/rotterdam/data/repository/SubCategoryRepository.kt`
- `getSubCategories(categoryId)`: Fetches subcategories for a category
- `getServicesBySubCategory(subcategoryId)`: Fetches services for a subcategory

### 3. SubCategoryViewModel.kt
**Path:** `app/src/main/java/com/dreamdiver/rotterdam/ui/viewmodel/SubCategoryViewModel.kt`
- Manages UI state for subcategory list
- `loadSubCategories(categoryId, categoryName)`: Loads subcategories
- `SubCategoryUiState`: Sealed class with Loading, Success, Error states

### 4. SubCategoryListScreen.kt
**Path:** `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/SubCategoryListScreen.kt`
- Displays subcategories in a 3-column grid
- Shows subcategory icon (or initials if no icon)
- Navigates to service list when subcategory is clicked
- Includes retry functionality on error

## Files Modified

### 1. ApiService.kt
Added two new endpoints:
```kotlin
@GET("api/v1/subcategories/category/{categoryId}")
suspend fun getSubCategories(@Path("categoryId") categoryId: Int): SubCategoryResponse

@GET("api/v1/subcategories/{subcategoryId}/services")
suspend fun getServicesBySubCategory(@Path("subcategoryId") subcategoryId: Int): SubCategoryServiceResponse
```

### 2. ServiceViewModel.kt
- Added `SubCategoryRepository` dependency
- Added `loadServicesBySubCategory(subcategoryId)` method

### 3. ServiceListScreen.kt
- Updated to accept both `categoryId` and `subcategoryId` parameters (both nullable)
- Updated `LaunchedEffect` to load services based on which ID is provided
- Fixed retry button to handle both category and subcategory loading

### 4. MainActivity.kt
- Added `SUBCATEGORY_LIST` to `DetailScreen` enum
- Added `SubCategoryListState` data class for navigation state
- Added `subcategoryListState` variable
- Added subcategory list screen handling in detail screen section
- Updated home screen navigation to go to subcategories instead of services
- Updated `ServiceListState` to support both categoryId and subcategoryId (nullable)

## API Endpoints Used

### Get SubCategories
**Endpoint:** `GET https://rotterdam.dreamdiver.nl/api/v1/subcategories/category/{categoryId}`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "General Hospital",
      "icon_url": "https://rotterdam.dreamdiver.nl/storage/subcategories/xxx.png",
      "services_count": 1
    }
  ]
}
```

### Get Services by SubCategory
**Endpoint:** `GET https://rotterdam.dreamdiver.nl/api/v1/subcategories/{subcategoryId}/services`

**Response:**
```json
{
  "success": true,
  "subcategory": {
    "id": 2,
    "name": "Eye Hospital"
  },
  "data": [
    {
      "id": 8,
      "name": "Het Oogziekenhuis Rotterdam",
      "phone": "+31 10 401 7777",
      "address": "Schiedamse Vest 180, 3011 BH Rotterdam",
      "latitude": null,
      "longitude": null,
      "description": "...",
      "image_url": "https://...",
      "status": "active"
    }
  ]
}
```

## Navigation Flow Details

1. **HomeScreen**: When a category is clicked, it navigates to `SUBCATEGORY_LIST` with category ID and name
2. **SubCategoryListScreen**: Displays all subcategories for the selected category
3. **ServiceListScreen**: Can be loaded either from:
   - Category ID (legacy support)
   - SubCategory ID (new flow)

## Testing
The implementation follows Android best practices:
- Uses Retrofit for API calls
- Implements Repository pattern for data layer
- Uses ViewModel with StateFlow for UI state management
- Composable UI with Material 3 design
- Proper error handling and loading states
- Retry functionality on errors

## Notes
- The subcategory icons are loaded from the server
- If no icon is available, the first two letters of the subcategory name are shown
- All screens maintain the 3-column grid layout as requested
- The back navigation properly cleans up state

