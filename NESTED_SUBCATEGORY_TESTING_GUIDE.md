# Quick Testing Guide - Nested Subcategories

## Test Scenarios

### 1. Basic Navigation Test
**Steps:**
1. Open the app and go to Home screen
2. Click on "Healthcare" category (ID: 2)
3. Verify subcategories are displayed in a 2-column grid
4. Click on "Clinic" (should have children)
5. Verify nested subcategories appear
6. Click on "Children Clinic" (should have more children)
7. Verify it shows "Children Veccine"
8. Click on "Children Veccine" (no children)
9. Verify it navigates to the services list

### 2. Icon Loading Test
**Steps:**
1. Navigate to any subcategory list
2. Verify icons load for subcategories with `icon` field
3. Verify placeholders appear for subcategories without icons
4. Check icons are properly sized (64.dp)

### 3. Back Button Test
**Steps:**
1. Navigate through multiple nested levels
2. Press hardware back button
3. Verify it goes back to home screen (not previous subcategory)
4. Navigate again through nested levels
5. Press top bar back icon
6. Verify it goes to previous subcategory screen

### 4. No Children Test
**Steps:**
1. Click a category with no children (e.g., "General Hospital")
2. Verify it directly navigates to services list
3. Verify service list displays correctly

### 5. Mixed Navigation Test
**Steps:**
1. Navigate to a subcategory with children
2. Go to nested subcategories
3. Press back to go home
4. Navigate to a different category
5. Verify state is properly cleared
6. Verify new subcategories load correctly

## Expected API Responses

### Healthcare Category Tree (ID: 2)
```
GET https://rotterdam.dreamdiver.nl/api/v1/categories/2/tree
```

Should return subcategories including:
- General Hospital (no children) → Services
- Eye Hospital (no children) → Services
- Child Hospital (no children) → Services
- Clinic (has children) → Nested view
  - Children Clinic (has children) → Nested view
    - Children Veccine (has children) → Nested view
      - New Veccine (no children) → Services
- Dental Clinic (no children) → Services
- College (has children) → Nested view
- University (no children) → Services
- Training Center (no children) → Services

## Common Issues & Solutions

### Issue: Icons not loading
**Solution:** Check if icon URL is being constructed correctly:
- If `iconUrl` exists, use it directly
- If only `icon` exists, build URL: `https://rotterdam.dreamdiver.nl/storage/{icon}`

### Issue: Crash on null children
**Solution:** Verify SubCategory model has default empty list:
```kotlin
val children: List<SubCategory> = emptyList()
```

### Issue: Navigation not working
**Solution:** 
- Verify `onNestedSubCategoryClick` is properly passed
- Check `nestedSubcategoryState` is being updated
- Ensure `DetailScreen.NESTED_SUBCATEGORY_LIST` is handled in when statement

### Issue: State not clearing on back
**Solution:** 
- Verify `nestedSubcategoryState = null` in back handler
- Check all state variables are cleared together

## Debug Tips

1. **Check API Response:**
   - Use Logcat to see API responses
   - Verify `success: true` in response
   - Check `children` array in response

2. **Check State:**
   - Add logs in `onNestedSubCategoryClick`
   - Verify `currentDetailScreen` changes
   - Check `nestedSubcategoryState` value

3. **Check Navigation:**
   - Add logs in each navigation handler
   - Verify correct screen is being shown
   - Check if state is properly passed

## API Endpoints Used

1. **Get Category Tree:**
   ```
   GET /api/v1/categories/{categoryId}/tree
   ```

2. **Get Services by Subcategory:**
   ```
   GET /api/v1/subcategories/{subcategoryId}/services
   ```

## UI Specifications

- **Grid:** 2 columns
- **Card Aspect Ratio:** 1:1 (square)
- **Icon Size:** 64.dp
- **Card Padding:** 8.dp
- **Grid Spacing:** 8.dp horizontal and vertical
- **Text:** 12.sp, Medium weight, max 2 lines
- **Colors:** Material 3 theme colors

## Performance Considerations

1. Tree API loads entire structure in one call
2. Icons loaded asynchronously with Coil
3. State preserved during configuration changes (except nested state - uses remember)
4. Lazy grid for efficient rendering

## Accessibility

- All images have contentDescription
- Text is readable with proper contrast
- Touch targets are adequately sized
- Navigation is logical and consistent

