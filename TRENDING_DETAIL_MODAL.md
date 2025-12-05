# Trending Details Modal Implementation

## Summary
Successfully implemented the functionality to show trending item details when clicking on trending cards on the Home screen.

## Changes Made

### 1. HomeScreen.kt Updates

#### Added State Management
- Added `selectedTrendingItem` state variable to track which trending item is selected
- Type: `TrendingItem?` (nullable)

#### Trending Detail Modal Integration
- Added `TrendingDetailModal` display when a trending item is selected
- Modal shows after the `ServiceDetailModal` in the composable
- Automatically dismisses when user closes it

#### Updated TrendingSection
- Added `onTrendingClick: (TrendingItem) -> Unit` callback parameter
- Passes click events from cards to parent composable

#### Updated TrendingCard
- Changed from empty `clickable { }` to `clickable(onClick = onClick)`
- Added `onClick: () -> Unit` callback parameter
- Triggers callback when card is tapped

#### Added New Imports
- `ModalBottomSheet`, `rememberModalBottomSheetState` for modal functionality
- `rememberScrollState`, `verticalScroll` for scrollable content
- `Icons.Default.Close`, `Icons.Default.OpenInBrowser` for UI icons
- `LocalContext` for accessing Android context
- `Intent`, `Uri` from Android for opening URLs
- `MaterialTheme` for theme colors

### 2. TrendingDetailModal Component

Created a new composable function that displays full trending item details in a modal bottom sheet.

#### Modal Content:
- **Header**: "Trending Details" with close button
- **Image**: Full trending item image (200dp height)
- **Title**: Display in English or default language based on `isEnglish` flag
- **Date**: Published date from `createdAt` field
- **Summary Section**: 
  - Section header
  - Full summary text (supports multi-language)
- **Details Section**:
  - Section header
  - Full details/description text (supports multi-language)
- **URL Action** (conditional):
  - Only shows if URL is not null and not empty
  - Clickable card with browser icon
  - Opens the URL in default browser when tapped
  - Shows "Read Full Article" and "Open in browser" text

## Implementation Details

### Click Flow
1. User taps on a trending card
2. `TrendingCard` triggers `onClick` callback
3. `TrendingSection` forwards the click to parent with the item
4. `HomeScreen` sets `selectedTrendingItem` state
5. Modal automatically appears due to state change
6. Modal displays full trending item details

### Multi-Language Support
The modal respects the `isEnglish` parameter and displays:
- `titleEn` or `title` based on language setting
- `summaryEn` or `summary` based on language setting
- `detailsEn` or `details` based on language setting

### URL Handling
```kotlin
trendingItem.url?.let { url ->
    if (url.isNotEmpty()) {
        // Show clickable card to open URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}
```

## Code Structure

### State in HomeScreen
```kotlin
var selectedTrendingItem by remember { mutableStateOf<TrendingItem?>(null) }
```

### Pass Click Handler
```kotlin
TrendingSection(
    trendingItems = state.trendingItems,
    isEnglish = isEnglish,
    onTrendingClick = { item -> selectedTrendingItem = item }
)
```

### Show Modal
```kotlin
selectedTrendingItem?.let { item ->
    TrendingDetailModal(
        trendingItem = item,
        isEnglish = isEnglish,
        onDismiss = { selectedTrendingItem = null }
    )
}
```

## API Integration
Uses existing trending data from:
- **Endpoint**: `https://rotterdam.dreamdiver.nl/api/v1/trending`
- **Response**: List of TrendingItem objects with all required fields

### TrendingItem Fields Used:
- `id`: Unique identifier
- `title` / `titleEn`: Main heading
- `summary` / `summaryEn`: Brief description
- `details` / `detailsEn`: Full content
- `url`: External link (optional)
- `image`: Image URL
- `createdAt`: Publication date
- `status`, `order`, `locale`: Metadata

## UI/UX Features

✅ **Smooth Modal Animation**: Uses Material 3 ModalBottomSheet with slide-up animation
✅ **Scrollable Content**: Long content scrolls within the modal
✅ **Skip Partial Expansion**: Opens fully by default for better readability
✅ **Image Display**: Shows trending image at the top (200dp height)
✅ **Structured Layout**: Clear sections with headers for Summary and Details
✅ **External Links**: Opens URLs in external browser with visual feedback
✅ **Close Options**: Close button or swipe down gesture
✅ **Material Design**: Uses theme colors and Material 3 components

## Modal Sections Breakdown

1. **Header Bar**
   - Title: "Trending Details"
   - Close button (X icon)

2. **Hero Image**
   - Full width
   - 200dp height
   - Rounded corners (12dp)
   - Horizontal padding (16dp)

3. **Title & Date**
   - Large bold title (24sp)
   - Publication date below

4. **Summary Block**
   - "Summary" header (16sp, SemiBold)
   - Summary text (14sp)

5. **Details Block**
   - "Details" header (16sp, SemiBold)
   - Full details text (14sp)

6. **Action Card** (if URL exists)
   - Browser icon
   - "Read Full Article" label
   - "Open in browser" subtitle
   - Clickable to open URL

## Files Modified
1. `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/HomeScreen.kt`
   - Added imports for modal, scroll, icons, and Android Intent
   - Added `selectedTrendingItem` state
   - Updated `TrendingSection` to accept click callback
   - Updated `TrendingCard` to trigger onClick
   - Created `TrendingDetailModal` composable

## Testing Checklist

- [x] Code compiles without errors
- [ ] Trending cards are clickable
- [ ] Modal opens on click with smooth animation
- [ ] All trending details display properly (title, date, summary, details)
- [ ] Images load correctly
- [ ] Multi-language support works (English/Default)
- [ ] URL button shows only when URL exists
- [ ] URL opens in browser correctly
- [ ] Modal dismisses on close button
- [ ] Modal dismisses on swipe down gesture
- [ ] Long content scrolls properly
- [ ] Multiple trending items can be opened sequentially

## Benefits

✅ Users can read full trending article details
✅ Provides deep dive into trending content
✅ External links allow further reading
✅ Clean, organized presentation of information
✅ Consistent UI with service detail modal
✅ Supports multi-language content
✅ Mobile-friendly with scroll and gestures

## Date
December 5, 2025

