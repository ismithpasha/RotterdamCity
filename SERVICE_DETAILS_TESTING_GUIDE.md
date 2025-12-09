# Service Details Testing Guide

## Date: December 9, 2025

## Test Scenarios

### 1. Basic Display Tests

#### Test 1.1: Full Service Details
**API**: `GET /api/v1/services/8`
**Expected**: 
- Hero card displays service image
- Service name appears in hero and below
- Category icon and name displayed
- Multiple tabs visible (Overview, Contact, etc.)
- All content sections render properly
- All three action buttons visible

#### Test 1.2: Minimal Service Details
**Test with service that has**:
- Only name and category
- No image (should show category icon)
- No phone number
- No website
- No address
**Expected**:
- Hero card shows category icon
- Service name displays correctly
- Category shown
- Default tabs appear
- Only relevant buttons show
- No errors/crashes

#### Test 1.3: Service Without Descriptions Array
**Test with service having**:
- `descriptions: []` or null
**Expected**:
- Default tabs created (Overview, Contact)
- Overview tab shows service description
- Contact tab shows phone and address
- Modal functions normally

### 2. Language Switch Tests

#### Test 2.1: English to Dutch Switch
**Steps**:
1. View service in English
2. Switch language to Dutch
3. Reopen service details
**Expected**:
- All labels in Dutch
- Service name shows Dutch version
- Tab titles in Dutch
- Content in Dutch
- Button labels in Dutch

#### Test 2.2: Missing Language Fields
**Test with service having**:
- `name_en` but no `name_nl`
- Or vice versa
**Expected**:
- Falls back to base `name` field
- No blank/null text displayed
- Graceful degradation

#### Test 2.3: Mixed Language Content
**Test with service having**:
- Some fields only in English
- Some fields only in Dutch
- Some fields in both
**Expected**:
- Correct language field selected when available
- Appropriate fallback used when not
- Consistent display

### 3. Tab Interaction Tests

#### Test 3.1: Multiple Tabs
**Test with service having**:
- 3+ tabs in descriptions array
**Expected**:
- All tabs scrollable horizontally
- Active tab highlighted
- Indicator animates smoothly
- Content updates on tab change
- No tab cut off

#### Test 3.2: Tab Content Switching
**Steps**:
1. Open service details
2. Switch between tabs rapidly
3. Scroll content in each tab
**Expected**:
- Smooth transitions
- Content loads correctly
- Scroll position resets per tab
- No lag or freeze

#### Test 3.3: Long Tab Titles
**Test with tabs having**:
- Very long titles (20+ characters)
**Expected**:
- Titles wrap or truncate appropriately
- Tabs remain usable
- Layout doesn't break

### 4. Action Button Tests

#### Test 4.1: Call Button
**Steps**:
1. Tap Call button
**Expected**:
- Phone dialer opens
- Number pre-filled
- No crash if phone app unavailable

#### Test 4.2: Visit Website Button
**Steps**:
1. Tap Visit Website
**Expected**:
- Browser opens
- Correct URL loaded
- No crash if browser unavailable

#### Test 4.3: Open Map Button
**Scenario A - With Lat/Lng**:
**Expected**: Maps opens with exact coordinates

**Scenario B - With google_maps_url**:
**Expected**: Maps opens with URL

**Scenario C - With address only**:
**Expected**: Maps opens with address search

**Scenario D - No location data**:
**Expected**: Button not visible

### 5. Content Display Tests

#### Test 5.1: Long Description
**Test with service having**:
- Description over 500 characters
**Expected**:
- Full text displays
- Content scrollable
- No text cut off
- Proper line breaks

#### Test 5.2: Special Characters
**Test with content having**:
- Emojis
- Accented characters (é, ñ, ü, etc.)
- Quotes and apostrophes
**Expected**:
- All characters render correctly
- No encoding issues
- Layout intact

#### Test 5.3: HTML/Markdown in Content
**Test if API returns**:
- HTML tags
- Markdown formatting
**Expected**:
- Either properly rendered or stripped
- No raw tags visible

#### Test 5.4: Multiple Content Sections
**Test with all sections present**:
- Description
- Address
- Phone
- Website
**Expected**:
- Proper spacing between sections
- Clear visual hierarchy
- All sections readable

### 6. Image Loading Tests

#### Test 6.1: Valid Service Image
**Expected**:
- Image loads in hero card
- Image fits properly (crop/scale)
- No distortion

#### Test 6.2: Invalid Image URL
**Expected**:
- Placeholder/error image shows
- Or category icon as fallback
- No broken image icon

#### Test 6.3: Slow Network
**Test with throttled connection**:
**Expected**:
- Loading indicator (if implemented)
- Eventually loads or shows placeholder
- UI remains responsive

#### Test 6.4: No Image Data
**Test with**:
- `image: null`
- `image: ""`
**Expected**:
- Category icon displays instead
- No blank space
- No error

### 7. Modal Behavior Tests

#### Test 7.1: Open/Close
**Steps**:
1. Tap service card
2. Modal appears
3. Tap back button
4. Modal closes
**Expected**:
- Smooth animation
- Returns to service list
- No state lost

#### Test 7.2: Scroll Behavior
**Steps**:
1. Open modal with long content
2. Scroll to bottom
3. Switch tabs
4. Return to first tab
**Expected**:
- Scroll resets on tab change
- Smooth scrolling
- No bounce issues

#### Test 7.3: Modal Height
**Expected**:
- Modal fills 95% of screen height
- Header visible
- Content scrollable
- Action buttons accessible

### 8. Edge Case Tests

#### Test 8.1: Empty Strings vs Null
**Test API returning**:
- `"address": ""`
- `"address": null`
**Expected**:
- Both handled gracefully
- Sections hidden appropriately
- No "null" text displayed

#### Test 8.2: Very Long Service Name
**Test with name**:
- 50+ characters
**Expected**:
- Name wraps in hero
- Name readable below hero
- Layout doesn't break

#### Test 8.3: Single Tab
**Test with service having**:
- Only one tab in descriptions
**Expected**:
- Single tab displays
- Tab still selectable
- Content shows correctly

#### Test 8.4: No Phone or Website
**Test with service lacking**:
- phone
- url
**Expected**:
- Only Map button shows
- Button takes available space
- Layout adjusts properly

### 9. Device-Specific Tests

#### Test 9.1: Different Screen Sizes
**Test on**:
- Small phone (< 5")
- Regular phone (5-6")
- Large phone (6.5"+)
**Expected**:
- Layout adapts
- All content accessible
- Buttons usable

#### Test 9.2: Different Orientations
**Steps**:
1. Open modal in portrait
2. Rotate to landscape
**Expected**:
- Modal adjusts or stays portrait
- Content remains visible
- No crash

#### Test 9.3: Different Android Versions
**Test on**:
- Android 9 (API 28)
- Android 10 (API 29)
- Android 11+ (API 30+)
**Expected**:
- Consistent appearance
- All features work
- No version-specific crashes

### 10. Performance Tests

#### Test 10.1: Multiple Opens
**Steps**:
1. Open service details
2. Close
3. Repeat 10 times quickly
**Expected**:
- No memory leaks
- Performance doesn't degrade
- No crashes

#### Test 10.2: Tab Switching Performance
**Steps**:
1. Open service with many tabs
2. Switch between tabs rapidly
**Expected**:
- Smooth transitions
- No lag
- Content loads quickly

#### Test 10.3: Large Content
**Test with**:
- Very long descriptions (1000+ words)
- Multiple long tabs
**Expected**:
- Renders without lag
- Scrolling smooth
- No ANR (App Not Responding)

## Manual Checklist

Before release, verify:

- [ ] All text properly localized (EN/NL)
- [ ] Images load correctly
- [ ] All buttons functional
- [ ] Tabs work properly
- [ ] Back button closes modal
- [ ] Layout looks good on different screens
- [ ] No crashes with missing data
- [ ] Phone dialer opens with correct number
- [ ] Browser opens correct URL
- [ ] Maps opens with correct location
- [ ] Hero card displays properly
- [ ] Category icon shows when no image
- [ ] Content sections properly spaced
- [ ] Action buttons properly styled
- [ ] Tab indicator animates smoothly
- [ ] Fallbacks work for missing language fields
- [ ] Special characters display correctly
- [ ] Long text doesn't break layout
- [ ] Modal closes without issues
- [ ] Performance is smooth

## Automated Test Recommendations

Consider adding:
1. Unit tests for `getLocalizedText()` helper
2. UI tests for modal open/close
3. UI tests for tab switching
4. Integration tests for button actions
5. Screenshot tests for different languages
6. Performance tests for large content

## Common Issues to Watch For

1. **Null pointer exceptions**: When service data is incomplete
2. **Layout overflow**: With very long content
3. **Image loading failures**: Network issues or bad URLs
4. **Language fallback issues**: Missing translations
5. **Tab indicator position**: With many tabs
6. **Button visibility**: When data is missing
7. **Deep link handling**: If user comes from notification
8. **State persistence**: When app goes to background
9. **Memory leaks**: From image loading or listeners
10. **Accessibility**: Screen reader support

## Test Data Requirements

Create test services with:
1. All fields populated (happy path)
2. Minimal fields only
3. No image
4. No location data
5. No phone/website
6. Very long names
7. Very long descriptions
8. Multiple tabs (5+)
9. Single tab
10. No tabs/descriptions array
11. Mixed language content
12. Only English content
13. Only Dutch content
14. Special characters in all fields
15. Invalid URLs
16. Invalid image URLs

## Reporting Issues

When reporting bugs, include:
- Device model and Android version
- Language setting (EN/NL)
- Service ID being viewed
- Steps to reproduce
- Expected vs actual behavior
- Screenshot/video if possible
- Logcat output if crash

