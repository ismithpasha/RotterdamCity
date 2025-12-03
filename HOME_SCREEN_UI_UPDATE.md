# Home Screen UI Update - Government Services Design

## Overview
Updated the home screen to match the new modern UI design with dark header, featured services, trending section, and all services grid layout.

## New Features Implemented

### 1. **Dark Header Section**
- Dark navy blue background (#1A1A2E)
- Menu and notification icons
- Search bar with rounded corners
- Tagline: "Instant digital access to over 50 government services, all in one place"

### 2. **Frequently Used Services Section**
- Horizontal scrollable list showing featured services
- 4 service icons in a row
- Light blue background for icons
- Centered "— Frequently used —" label
- API: `GET /api/v1/services/featured`

### 3. **Trending Section**
- Horizontal scrollable cards
- Each card shows:
  - Background image with overlay
  - Title and date
  - Social media icon placeholder
  - Cyan/turquoise gradient background
- API: `GET /api/v1/trending`

### 4. **All Services Grid**
- 4 columns layout (instead of previous 3)
- Shows all service categories
- "More" button in header
- Scrollable grid with category icons and names
- API: `GET /api/v1/categories` (existing)

## API Integration

### Featured Services API
```
GET https://rotterdam.dreamdiver.nl/api/v1/services/featured
```
**Response:**
- Returns list of frequently used services
- Each service includes category, name, address, phone, image, etc.
- Displays first 4 services in horizontal row

### Trending API
```
GET https://rotterdam.dreamdiver.nl/api/v1/trending
```
**Response:**
- Returns list of trending news/announcements
- Each item includes title, summary, details, image, URL
- Displays in horizontal scrollable cards

### Categories API (Existing)
```
GET https://rotterdam.dreamdiver.nl/api/v1/categories
```
**Response:**
- Returns all service categories
- Each category includes id, name, icon_url, services_count

## Technical Changes

### New Files Created
1. **HomeRepository.kt** - Handles featured services and trending data fetching
   - `getFeaturedServices()` - Fetches featured services
   - `getTrending()` - Fetches trending items

### Data Models Added (Category.kt)
1. **FeaturedServicesResponse** - Response wrapper for featured services
2. **TrendingResponse** - Response wrapper for trending items
3. **TrendingItem** - Model for trending news/announcements
   - Fields: id, title, summary, details, url, image, status, order, etc.
   - Supports English translations (title_en, summary_en, details_en)

### Updated Files

#### ApiService.kt
- Added `getFeaturedServices()` endpoint
- Added `getTrending()` endpoint

#### HomeViewModel.kt
- Updated `HomeUiState.Success` to include:
  - `categories: List<Category>`
  - `featuredServices: List<Service>`
  - `trendingItems: List<TrendingItem>`
- Loads all three data sources in parallel
- Improved error handling

#### HomeScreen.kt - Complete Redesign
**New Components:**
1. **FeaturedServicesSection** - Shows frequently used services
2. **FeaturedServiceCard** - Individual service card (64x64 icon)
3. **TrendingSection** - Shows trending news cards
4. **TrendingCard** - Individual trending news card (280x120)
5. **AllServicesSection** - Shows all service categories grid
6. **ServiceCategoryItem** - Individual category item (4 column grid)

**Layout Structure:**
```
LazyColumn
├── Dark Header (Menu, Search, Title)
├── FeaturedServicesSection (Horizontal LazyRow)
├── TrendingSection (Horizontal LazyRow)
└── AllServicesSection (4-column LazyVerticalGrid)
```

## UI Specifications

### Colors
- **Header Background**: #1A1A2E (Dark Navy)
- **Content Background**: White
- **Featured Icon Background**: #E8F4FF (Light Blue)
- **Trending Card**: #4DD0E1 (Cyan/Turquoise)
- **Text on Dark**: White
- **Accent**: #4DD0E1 (Cyan)

### Layout
- **Featured Services**: 4 items per row, 80dp width each, horizontal scroll
- **Trending Cards**: 280dp width, 120dp height, horizontal scroll
- **All Services Grid**: 4 columns, 56dp icon size, vertical scroll

### Typography
- **Header Title**: 18sp, Bold, White
- **Section Headers**: 18sp, Bold, Black
- **Service Names**: 12sp (featured), 11sp (grid)
- **Trending Title**: 16sp, Bold, White
- **Small Labels**: 12sp, Gray

## Auto-Reload Feature
- Home data automatically reloads when returning from other tabs
- Uses `LaunchedEffect(currentDestination)` in MainActivity
- Triggers `homeViewModel.loadCategories()` on tab switch

## Language Support
All sections support English/Bangla switching:
- Service names: Uses `nameEn` field when English selected
- Category names: Uses `nameEn` field when English selected
- Trending: Uses `titleEn`, `summaryEn`, `detailsEn` fields

## Testing Checklist
- [x] Featured services load from API
- [x] Trending items load from API
- [x] Categories load from API
- [x] All sections display correctly
- [x] Horizontal scrolling works for featured & trending
- [x] 4-column grid displays properly
- [x] Icons load from URLs
- [x] Language switching works
- [x] Auto-reload on tab switch
- [x] Error handling displays properly
- [x] Loading states show spinner

## Files Modified
1. `data/model/Category.kt` - Added FeaturedServicesResponse, TrendingResponse, TrendingItem
2. `data/api/ApiService.kt` - Added getFeaturedServices(), getTrending()
3. `data/repository/HomeRepository.kt` - New file, handles new APIs
4. `ui/viewmodel/HomeViewModel.kt` - Updated to load all three data sources
5. `ui/screens/HomeScreen.kt` - Complete redesign with new sections

## Migration Notes
- Old ImageSlider component removed from home screen
- Changed from 3-column to 4-column grid layout
- Dark header replaces previous light design
- Categories now in "All Services" section instead of main view

## Known Limitations
- Search bar is decorative (not functional yet)
- Menu and notification icons have no actions
- "More" button in All Services has no action
- Twitter/social icon in trending is placeholder
- Trending card click action not implemented

