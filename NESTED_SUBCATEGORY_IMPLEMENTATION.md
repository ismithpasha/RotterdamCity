# Nested Subcategory Implementation

## Overview
This document describes the implementation of nested subcategory support in the Rotterdam City app. The app now supports multiple levels of subcategories, allowing users to navigate through a tree structure of categories.

## API Changes

### New Endpoint
- **GET** `https://rotterdam.dreamdiver.nl/api/v1/categories/{categoryId}/tree`

### Response Structure
```json
{
  "success": true,
  "data": {
    "id": 2,
    "name": "Healthcare",
    "name_en": "Healthcare",
    "icon": "1763919033_692344b930f16.png",
    "icon_url": "https://rotterdam.dreamdiver.nl/storage/icons/1763919033_692344b930f16.png",
    "status": "active",
    "subcategories": [
      {
        "id": 1,
        "name": "General Hospital",
        "name_en": "General Hospital",
        "icon": "subcategories/1764266302_call_13641122.png",
        "status": "active",
        "depth": 0,
        "children": []
      },
      {
        "id": 4,
        "name": "Clinic",
        "name_en": "Clinic",
        "icon": null,
        "status": "active",
        "depth": 0,
        "children": [
          {
            "id": 38,
            "name": "Children Clinic",
            "name_en": "Children Clinic",
            "icon": "subcategories/1765002821_9340051.png",
            "status": "active",
            "depth": 1,
            "children": [
              {
                "id": 39,
                "name": "Children Veccine",
                "name_en": "Children Veccine",
                "icon": "subcategories/1765003225_electrician_1983275.png",
                "status": "active",
                "depth": 2,
                "children": []
              }
            ]
          }
        ]
      }
    ]
  },
  "locale": "en",
  "message": "Category tree retrieved successfully."
}
```

## Implementation Details

### 1. Data Models (SubCategory.kt)

#### CategoryTreeResponse
New response model for the tree API endpoint.

```kotlin
data class CategoryTreeResponse(
    val success: Boolean,
    val data: CategoryTree,
    val locale: String? = null,
    val message: String? = null
)

data class CategoryTree(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    val icon: String? = null,
    @SerializedName("icon_url")
    val iconUrl: String?,
    val status: String,
    val subcategories: List<SubCategory>
)
```

#### Updated SubCategory Model
Added support for nested children and depth tracking.

```kotlin
data class SubCategory(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    val icon: String? = null,
    @SerializedName("icon_url")
    val iconUrl: String? = null,
    val status: String,
    val depth: Int,
    val children: List<SubCategory> = emptyList(),
    @SerializedName("services_count")
    val servicesCount: Int? = null
)
```

### 2. API Service (ApiService.kt)

Added new endpoint:
```kotlin
@GET("api/v1/categories/{categoryId}/tree")
suspend fun getCategoryTree(@Path("categoryId") categoryId: Int): CategoryTreeResponse
```

### 3. Repository (SubCategoryRepository.kt)

Added method to fetch category tree:
```kotlin
suspend fun getCategoryTree(categoryId: Int): Result<List<SubCategory>> {
    return try {
        val response = apiService.getCategoryTree(categoryId)
        if (response.success) {
            Result.success(response.data.subcategories)
        } else {
            Result.failure(Exception("Failed to load category tree"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 4. ViewModel (SubCategoryViewModel.kt)

Updated to use the tree API and added support for loading nested subcategories:
```kotlin
fun loadSubCategories(categoryId: Int, categoryName: String) {
    viewModelScope.launch {
        _uiState.value = SubCategoryUiState.Loading
        repository.getCategoryTree(categoryId)
            .onSuccess { subCategories ->
                _uiState.value = SubCategoryUiState.Success(categoryName, subCategories)
            }
            .onFailure { error ->
                _uiState.value = SubCategoryUiState.Error(error.message ?: "Unknown error")
            }
    }
}
```

### 5. UI Screens (SubCategoryListScreen.kt)

#### SubCategoryListScreen
Updated to handle nested subcategory navigation:
- If a subcategory has children, navigate to `NestedSubCategoryListScreen`
- If a subcategory has no children, navigate to the services list

```kotlin
SubCategoryCard(
    subCategory = subCategory,
    onClick = {
        if (subCategory.children.isNotEmpty()) {
            onNestedSubCategoryClick(subCategory)
        } else {
            onSubCategoryClick(subCategory.id, subCategory.name)
        }
    }
)
```

#### NestedSubCategoryListScreen
New screen to display nested subcategories:
- Shows the children of a selected subcategory
- Supports infinite depth navigation
- Same navigation logic as SubCategoryListScreen

#### SubCategoryCard
Updated to properly load icons:
```kotlin
val iconUrl = subCategory.iconUrl ?: subCategory.icon?.let {
    "https://rotterdam.dreamdiver.nl/storage/$it"
}
```

### 6. Navigation (MainActivity.kt)

#### New DetailScreen Entry
```kotlin
enum class DetailScreen {
    // ...existing entries...
    NESTED_SUBCATEGORY_LIST,
    // ...
}
```

#### Navigation State
Added state to track nested subcategories:
```kotlin
var nestedSubcategoryState by remember { mutableStateOf<SubCategory?>(null) }
```

#### Navigation Routes
```kotlin
DetailScreen.SUBCATEGORY_LIST -> {
    SubCategoryListScreen(
        // ...
        onNestedSubCategoryClick = { subCategory ->
            nestedSubcategoryState = subCategory
            currentDetailScreen = DetailScreen.NESTED_SUBCATEGORY_LIST
        }
    )
}

DetailScreen.NESTED_SUBCATEGORY_LIST -> {
    nestedSubcategoryState?.let { subCategory ->
        NestedSubCategoryListScreen(
            subCategory = subCategory,
            // ...
            onNestedSubCategoryClick = { childSubCategory ->
                nestedSubcategoryState = childSubCategory
                // Stay on NESTED_SUBCATEGORY_LIST screen
            }
        )
    }
}
```

## Navigation Flow

1. **Home Screen** → Click category icon
2. **SubCategory List** → Shows top-level subcategories
   - If subcategory has children → Navigate to NestedSubCategoryListScreen
   - If subcategory has no children → Navigate to Services List
3. **Nested SubCategory List** → Shows children of selected subcategory
   - If child has children → Update state to show new children
   - If child has no children → Navigate to Services List
4. **Services List** → Shows services for the selected subcategory

## Key Features

### Infinite Depth Support
The implementation supports unlimited nesting levels through recursive navigation.

### Icon Loading
Icons are loaded using Coil from:
1. `iconUrl` field if available (full URL)
2. `icon` field (relative path) constructed as: `https://rotterdam.dreamdiver.nl/storage/{icon}`
3. Placeholder text if no icon available

### Back Button Handling
- Hardware back button properly clears nested subcategory state
- Navigation icon in top bar navigates to previous screen

### Error Handling
- Loading states with CircularProgressIndicator
- Error states with retry button
- Empty state for subcategories with no children

## Testing Checklist

- [ ] Category with no subcategories → Direct to services
- [ ] Category with subcategories (no children) → Services list
- [ ] Category with nested subcategories → Navigate through tree
- [ ] Multi-level nesting (depth > 2) → Navigate correctly
- [ ] Icons load correctly from both `icon` and `iconUrl` fields
- [ ] Back button navigation works at all levels
- [ ] Hardware back button works correctly
- [ ] Loading states display properly
- [ ] Error states display with retry option

## Files Modified

1. `app/src/main/java/com/dreamdiver/rotterdam/data/model/SubCategory.kt`
2. `app/src/main/java/com/dreamdiver/rotterdam/data/api/ApiService.kt`
3. `app/src/main/java/com/dreamdiver/rotterdam/data/repository/SubCategoryRepository.kt`
4. `app/src/main/java/com/dreamdiver/rotterdam/ui/viewmodel/SubCategoryViewModel.kt`
5. `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/SubCategoryListScreen.kt`
6. `app/src/main/java/com/dreamdiver/rotterdam/MainActivity.kt`

## API Integration

The implementation uses the new `/api/v1/categories/{categoryId}/tree` endpoint which returns the complete category tree structure in a single API call, eliminating the need for multiple requests to load nested subcategories.

## Future Enhancements

1. Add breadcrumb navigation to show current path
2. Cache tree structure to reduce API calls
3. Add search functionality across all subcategories
4. Add subcategory count indicators
5. Implement swipe gestures for navigation

