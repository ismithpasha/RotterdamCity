# Service Details Page Redesign

## Date: December 9, 2025

## Overview
Complete redesign of the Service Details modal to match the reference design with improved UI, tabbed content, and full bilingual (English/Dutch) support.

## Changes Made

### 1. Data Model Updates (`Category.kt`)

#### Updated `ServiceDetail` Model
Added support for the new API structure with:
- Bilingual fields for name, address, and description (`_en` and `_nl` suffixes)
- New `descriptions` array field for tabbed content
- New `url` field for website links

#### New `ServiceDescription` Model
```kotlin
data class ServiceDescription(
    val id: Int,
    val tabTitle: String? = null,
    val tabTitleEn: String? = null,
    val tabTitleNl: String? = null,
    val content: String? = null,
    val contentEn: String? = null,
    val contentNl: String? = null,
    val order: Int = 0
)
```

### 2. UI Redesign (`ServiceListScreen.kt`)

#### New ServiceDetailModal Features

**1. Hero Card Section**
- Large banner image (160dp height) with service image or category icon
- Dark overlay (40% opacity) for better text readability
- Centered service name in white text
- Rounded corners (12dp radius)
- Professional teal background color (#1E5A4C)

**2. Service Header**
- Service name (20sp, Bold)
- Category icon and name below (14sp, gray)
- Clean spacing and alignment

**3. Tabbed Content System**
- Dynamic tabs from API `descriptions` array
- ScrollableTabRow with primary blue color (#0083CA)
- Tab indicator with smooth animation
- Fallback to default tabs (Overview, Contact) if no descriptions provided
- Tabs sorted by `order` field

**4. Content Sections**
- **Description**: Full service description with proper line spacing
- **Address**: Complete address information
- **Phone**: Phone number with formatting
- **Website**: Clickable URL with underline decoration
- All sections with consistent typography and spacing

**5. Action Buttons**
Three equal-width buttons at bottom:
- **Call Button**: Direct phone dial
- **Visit Website Button**: Opens URL in browser
- **Open Map Button**: Opens location in maps app
  - Supports latitude/longitude coordinates
  - Falls back to google_maps_url
  - Falls back to address search
- Styled with:
  - White background
  - Blue content color (#0083CA)
  - 1dp light gray border
  - 12dp rounded corners
  - Icon + text layout

**6. Bilingual Support**
Helper function for localized text selection:
```kotlin
fun getLocalizedText(en: String?, nl: String?, fallback: String?): String
```
- Prioritizes language-specific field based on `isEnglish` flag
- Falls back to base field if language-specific is empty
- Returns empty string if all are null/blank

### 3. Utility Updates (`Strings.kt`)

Added bilingual strings for service details:
- `serviceDetails()` - Service Details / Service Details
- `overview()` - Overview / Overzicht
- `contact()` - Contact / Contact
- `description()` - Description / Beschrijving
- `address()` - Address / Adres
- `phone()` - Phone / Telefoon
- `phoneNumber()` - Phone Number / Telefoonnummer
- `website()` - Website / Website
- `openingHours()` - Opening Hours / Openingstijden
- `call()` - Call / Bellen
- `visitWebsite()` - Visit Website / Bezoek Website
- `openMap()` - Open Map / Open Kaart
- `location()` - Location / Locatie
- `noDescriptionAvailable()` - No description available / Geen beschrijving beschikbaar

### 4. Import Updates

Added necessary imports:
- `androidx.compose.foundation.BorderStroke`
- `androidx.compose.foundation.background`
- `androidx.compose.foundation.layout.fillMaxHeight`
- `androidx.compose.material.icons.filled.Language`
- `androidx.compose.material3.ButtonDefaults`
- `androidx.compose.material3.Divider`
- `androidx.compose.material3.ScrollableTabRow`
- `androidx.compose.material3.Tab`
- `androidx.compose.material3.TabRowDefaults`
- `androidx.compose.ui.graphics.Color`
- `androidx.compose.ui.res.painterResource`
- `androidx.compose.ui.text.style.TextDecoration`
- `com.dreamdiver.rotterdam.R`

## API Integration

### Service Details API
**Endpoint**: `GET /api/v1/services/{id}`

**Response Structure**:
```json
{
  "success": true,
  "data": {
    "id": 8,
    "category_id": 2,
    "category": { ... },
    "name": "Mental Health Center",
    "name_en": "Mental Health Center",
    "name_nl": "Geestelijke Gezondheidscentrum",
    "phone": "(555) 345-6789",
    "address": "150 Mind Care Blvd",
    "address_en": "150 Mind Care Blvd",
    "address_nl": "...",
    "latitude": null,
    "longitude": null,
    "google_maps_url": null,
    "description": "...",
    "description_en": "...",
    "description_nl": "...",
    "descriptions": [
      {
        "id": 6,
        "tab_title": "Overview",
        "tab_title_en": "Overview",
        "tab_title_nl": "Overzicht",
        "content": "...",
        "content_en": "...",
        "content_nl": "...",
        "order": 0
      }
    ],
    "image": "https://...",
    "url": "https://...",
    "status": "active",
    "locale": "en"
  }
}
```

## Design Specifications

### Colors
- **Primary Blue**: #0083CA (buttons, links, active tabs)
- **Teal Background**: #1E5A4C (hero card)
- **Text Primary**: #1E1E1E (main content)
- **Text Secondary**: #666666 (category label)
- **Border**: #E0E0E0 (button borders)
- **White**: #FFFFFF (backgrounds, button containers)
- **Divider**: #EEEEEE (tab divider)

### Typography
- **Hero Title**: 22sp, Bold, White
- **Service Name**: 20sp, Bold, Primary Text
- **Tab Labels**: Default size, SemiBold when selected
- **Section Headers**: 16sp, Bold, Primary Text
- **Body Text**: 15sp, Regular, Primary Text, 22sp line height
- **Category**: 14sp, Regular, Secondary Text
- **Button Text**: 12sp, Regular, Primary Blue

### Spacing
- **Outer Padding**: 16dp
- **Section Spacing**: 16dp between sections
- **Element Spacing**: 8dp between related elements
- **Card Radius**: 12dp
- **Button Radius**: 12dp
- **Hero Height**: 160dp
- **Icon Size**: 18-20dp
- **Modal Height**: 95% of screen

### Layout
- Full-width modal bottom sheet
- Fixed header with back button
- Scrollable content area
- Fixed tab row
- Three-column button layout at bottom
- Responsive to content length

## Language Support

The modal automatically adapts content based on the `isEnglish` parameter:

**English Mode** (`isEnglish = true`):
- Uses `*_en` fields from API
- Falls back to base fields if empty
- Shows English UI labels

**Dutch Mode** (`isEnglish = false`):
- Uses `*_nl` fields from API
- Falls back to base fields if empty
- Shows Dutch UI labels

## User Interactions

1. **Back Button**: Closes modal and returns to service list
2. **Tab Selection**: Switches between different content sections
3. **Call Button**: Opens phone dialer with pre-filled number
4. **Visit Website**: Opens external browser with service URL
5. **Open Map**: Opens maps app with location:
   - Uses lat/lng if available
   - Falls back to google_maps_url
   - Falls back to address search
6. **Website Link**: In content, clickable and opens browser

## Error Handling

- Handles missing/null values gracefully
- Shows default tabs if `descriptions` array is empty
- Hides buttons if corresponding data is unavailable
- Uses placeholder image if service image is missing
- Falls back to category icon for images

## Accessibility

- Proper content descriptions for icons
- Readable text sizes (minimum 12sp)
- High contrast colors
- Touch target sizes meet minimum requirements
- Scrollable content for screen readers

## Performance Considerations

- Lazy loading of tab content
- Efficient image loading with Coil
- Minimal recomposition with remember
- Proper state management

## Testing Notes

Test with various data scenarios:
1. Service with all fields populated
2. Service with minimal data
3. Service with empty descriptions array
4. Service with only English content
5. Service with only Dutch content
6. Service with mixed language content
7. Service without image
8. Service without location data
9. Service without phone/website
10. Long content in tabs

## Files Modified

1. `app/src/main/java/com/dreamdiver/rotterdam/data/model/Category.kt`
   - Added `ServiceDescription` data class
   - Updated `ServiceDetail` with bilingual fields and descriptions array

2. `app/src/main/java/com/dreamdiver/rotterdam/ui/screens/ServiceListScreen.kt`
   - Complete redesign of `ServiceDetailModal`
   - Added bilingual text helper function
   - Updated imports

3. `app/src/main/java/com/dreamdiver/rotterdam/util/Strings.kt`
   - Added service detail specific strings

## Benefits

1. **Improved UX**: Modern, clean design matching iOS standards
2. **Better Content Organization**: Tabbed content for easier navigation
3. **Full Bilingual Support**: Seamless English/Dutch switching
4. **Enhanced Accessibility**: Better visual hierarchy and readability
5. **Flexible Content**: API-driven tabs support any number of sections
6. **Professional Appearance**: Consistent with reference design
7. **Mobile-First**: Optimized for phone screens
8. **Easy Maintenance**: Clean code structure and reusable components

## Future Enhancements

Potential improvements:
1. Add opening hours section (when available in API)
2. Add rating/review display
3. Add image gallery support
4. Add sharing functionality
5. Add favorite/bookmark feature
6. Add directions integration
7. Add contact form integration
8. Add social media links
9. Add related services section
10. Add service booking functionality

