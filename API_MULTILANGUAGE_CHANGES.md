# API Multi-Language Updates - Complete Changes

## 🌐 Overview

All API endpoints now support **English (en)** and **Dutch (nl)** languages automatically based on the `Accept-Language` header.

---

## 📋 API Endpoints with Multi-Language Support

### ✅ **UPDATED ENDPOINTS** (Now Return Translations)

| Endpoint | Method | Changes | New Response Fields |
|----------|--------|---------|-------------------|
| `/api/v1/categories` | GET | Returns translated category names | `name`, `name_en`, `locale` |
| `/api/v1/categories/{id}` | GET | Returns translated category | `name`, `name_en`, `locale` |
| `/api/v1/categories/{id}/services` | GET | Returns translated services | `name`, `name_en`, `locale` |
| `/api/v1/subcategories` | GET | Returns translated subcategories | `name`, `name_en`, `locale` |
| `/api/v1/subcategories/skills` | GET | Returns translated worker skills | `name`, `name_en`, `locale` |
| `/api/v1/subcategories/category/{id}` | GET | Returns translated subcategories | `name`, `name_en`, `locale` |
| `/api/v1/subcategories/{id}` | GET | Returns translated subcategory | `name`, `name_en`, `locale` |
| `/api/v1/subcategories/{id}/services` | GET | Returns translated services | Services + translations |
| `/api/v1/services` | GET | Returns translated services | `name`, `description`, `address` + `_en` versions |
| `/api/v1/services/{id}` | GET | Returns translated service | `name`, `description`, `address` + `_en` versions |
| `/api/v1/services/category/{id}` | GET | Returns translated services | Full translation support |
| `/api/v1/services/subcategory/{id}` | GET | Returns translated services | Full translation support |
| `/api/v1/services/nearby/location` | GET | Returns translated nearby services | Full translation support |
| `/api/v1/sliders` | GET | Returns translated sliders | `title`, `short_details`, `details` + `_en` versions |
| `/api/v1/sliders/{id}` | GET | Returns translated slider | `title`, `short_details`, `details` + `_en` versions |
| `/api/v1/workers` | GET | Returns workers with translated skills | Subcategory names translated |

---

## 🔄 Request Header

### How to Specify Language

**Method 1: HTTP Header (Recommended)**
```http
Accept-Language: nl
```

**Method 2: Query Parameter**
```http
GET /api/v1/categories?lang=nl
```

**Default**: If no language specified, defaults to **English (en)**

---

## 📦 Response Format Changes

### **Before** (Old Response)
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Emergency Services",
      "icon_url": "...",
      "status": "active"
    }
  ]
}
```

### **After** (New Response with Translations)
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Nooddiensten",
      "name_en": "Emergency Services",
      "icon_url": "...",
      "status": "active",
      "locale": "nl"
    }
  ],
  "locale": "nl"
}
```

---

## 🎯 Detailed API Changes

### 1. **Categories API**

#### `GET /api/v1/categories`

**Request:**
```bash
curl -H "Accept-Language: nl" https://api.example.com/api/v1/categories
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Nooddiensten",
      "name_en": "Emergency Services",
      "icon": "emergency.png",
      "icon_url": "https://example.com/storage/icons/emergency.png",
      "status": "active",
      "tag": null,
      "services_count": 5
    },
    {
      "id": 11,
      "name": "Werknemersvaardigheden",
      "name_en": "Worker Skills",
      "icon": "skills.png",
      "icon_url": "https://example.com/storage/icons/skills.png",
      "status": "active",
      "tag": "skill",
      "services_count": 0
    }
  ],
  "locale": "nl"
}
```

**New Fields:**
- `name`: Translated name (Dutch if requested)
- `name_en`: Original English name (always included)
- `locale`: Current language code

---

### 2. **SubCategories API**

#### `GET /api/v1/subcategories/skills`

**Request:**
```bash
curl -H "Accept-Language: nl" https://api.example.com/api/v1/subcategories/skills
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 16,
      "category_id": 11,
      "name": "Elektricien",
      "name_en": "Electrician",
      "icon": "electrician.png",
      "icon_url": "https://example.com/storage/subcategories/electrician.png",
      "status": "active",
      "category": {
        "id": 11,
        "name": "Werknemersvaardigheden",
        "name_en": "Worker Skills"
      }
    },
    {
      "id": 17,
      "name": "Loodgieter",
      "name_en": "Plumber",
      "category_id": 11,
      "status": "active"
    }
  ],
  "locale": "nl"
}
```

**New Fields:**
- `name`: Translated subcategory name
- `name_en`: Original English name
- `category.name`: Translated category name
- `category.name_en`: Original category English name

---

### 3. **Services API**

#### `GET /api/v1/services`

**Request:**
```bash
curl -H "Accept-Language: nl" https://api.example.com/api/v1/services
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "category_id": 1,
      "category": {
        "id": 1,
        "name": "Nooddiensten",
        "name_en": "Emergency Services"
      },
      "name": "Rotterdam Brandweer",
      "name_en": "Rotterdam Fire Department",
      "phone": "+31 10 446 6666",
      "address": "Boompjes 200, Rotterdam",
      "address_en": "Boompjes 200, Rotterdam",
      "latitude": "51.9165",
      "longitude": "4.4821",
      "google_maps_url": "https://maps.google.com/...",
      "description": "24/7 brandweer nooddienst",
      "description_en": "24/7 fire emergency service",
      "image": "https://example.com/storage/images/fire_dept.jpg",
      "status": "active",
      "locale": "nl",
      "created_at": "2024-01-15T10:30:00.000000Z",
      "updated_at": "2024-01-15T10:30:00.000000Z"
    }
  ],
  "count": 1,
  "locale": "nl",
  "message": "Services retrieved successfully."
}
```

**New Fields:**
- `name`: Translated service name
- `name_en`: Original English name
- `description`: Translated description
- `description_en`: Original English description
- `address`: Translated address (if applicable)
- `address_en`: Original English address
- `locale`: Current language

---

### 4. **Sliders API**

#### `GET /api/v1/sliders`

**Request:**
```bash
curl -H "Accept-Language: nl" https://api.example.com/api/v1/sliders
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Welkom bij Rotterdam Services",
      "title_en": "Welcome to Rotterdam Services",
      "short_details": "Vind alle stadsdiensten op één plek",
      "short_details_en": "Find all city services in one place",
      "details": "Ontdek categorieën zoals nooddiensten, gezondheidszorg en meer",
      "details_en": "Discover categories like emergency services, healthcare, and more",
      "image": "https://example.com/storage/sliders/welcome.jpg",
      "order": 1,
      "status": "active",
      "locale": "nl",
      "created_at": "2024-01-01 10:00:00",
      "updated_at": "2024-01-01 10:00:00"
    }
  ],
  "count": 1,
  "locale": "nl",
  "message": "Sliders retrieved successfully."
}
```

**New Fields:**
- `title`: Translated title
- `title_en`: Original English title
- `short_details`: Translated short description
- `short_details_en`: Original English short description
- `details`: Translated full details
- `details_en`: Original English details
- `locale`: Current language

---

### 5. **Workers API**

#### `GET /api/v1/workers`

**Request:**
```bash
curl -H "Accept-Language: nl" https://api.example.com/api/v1/workers?subcategory_id=16
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 5,
      "name": "Jan de Vries",
      "email": "jan@example.com",
      "phone": "+31 6 1234 5678",
      "city": "Rotterdam",
      "profile": {
        "subcategory_id": 16,
        "subcategory": {
          "id": 16,
          "name": "Elektricien",
          "name_en": "Electrician",
          "category": {
            "id": 11,
            "name": "Werknemersvaardigheden",
            "name_en": "Worker Skills"
          }
        },
        "hourly_rate": 45.00,
        "experience_years": 5,
        "bio": "Gecertificeerd elektricien met 5 jaar ervaring",
        "is_verified": true
      },
      "average_rating": 4.5,
      "total_ratings": 12
    }
  ],
  "current_page": 1,
  "per_page": 15,
  "total": 1,
  "locale": "nl"
}
```

**Translation in:**
- `profile.subcategory.name`: Translated skill name
- `profile.subcategory.category.name`: Translated category name

---

## 🔌 Mobile App Integration

### React Native / Flutter Example

```javascript
// Set default language header for all API calls
import axios from 'axios';

const api = axios.create({
  baseURL: 'https://api.rotterdamcity.com/api/v1',
  headers: {
    'Accept-Language': 'nl', // or 'en'
    'Content-Type': 'application/json',
  },
});

// Fetch categories in Dutch
const response = await api.get('/categories');
console.log(response.data.locale); // "nl"
console.log(response.data.data[0].name); // "Nooddiensten"
console.log(response.data.data[0].name_en); // "Emergency Services"
```

### Swift (iOS) Example

```swift
var request = URLRequest(url: url)
request.setValue("nl", forHTTPHeaderField: "Accept-Language")

URLSession.shared.dataTask(with: request) { data, response, error in
    // Response will contain Dutch translations
}
```

### Kotlin (Android) Example

```kotlin
val client = OkHttpClient()
val request = Request.Builder()
    .url("https://api.rotterdamcity.com/api/v1/categories")
    .addHeader("Accept-Language", "nl")
    .build()

client.newCall(request).execute()
```

---

## ⚙️ Backend Implementation

### Models Updated
- ✅ `Category` - Added `translations()` relationship
- ✅ `SubCategory` - Added `translations()` relationship  
- ✅ `Service` - Added `translations()` relationship
- ✅ `Slider` - Added `translations()` relationship

### Controllers Updated
- ✅ `CategoryApiController` - Loads translations
- ✅ `SubCategoryApiController` - Loads translations
- ✅ `ServiceApiController` - All methods load translations
- ✅ `SliderApiController` - All methods load translations
- ✅ `UserProfileController` - Worker skills translated

### Resources Updated
- ✅ `CategoryResource` - Returns translated fields
- ✅ `ServiceResource` - Returns translated fields
- ✅ `SliderResource` - Returns translated fields

---

## 🧪 Testing

### Test English Response (Default)
```bash
curl https://api.example.com/api/v1/categories
```

### Test Dutch Response
```bash
curl -H "Accept-Language: nl" https://api.example.com/api/v1/categories
```

### Test with Query Parameter
```bash
curl "https://api.example.com/api/v1/categories?lang=nl"
```

### Expected Result
All responses should include:
- `locale` field indicating current language
- Translated `name` field
- Original `name_en` field for fallback
- Consistent format across all endpoints

---

## 📊 Translation Coverage

| Entity | Total | Dutch Translations | Completion |
|--------|-------|-------------------|------------|
| Categories | 12 | 12 | 100% ✅ |
| SubCategories | 43 | 43 | 100% ✅ |
| Services | Variable | 0 | 0% (Manual) |
| Sliders | Variable | 0 | 0% (Manual) |

**Note**: Services and Sliders need manual translation via admin panel.

---

## 🔄 Migration Guide for Mobile Apps

### Step 1: Update API Headers
Add `Accept-Language` header to all API requests:
```javascript
headers: {
  'Accept-Language': userLanguagePreference // 'en' or 'nl'
}
```

### Step 2: Update Response Parsing
Access translated fields:
```javascript
// Old way
const categoryName = response.data[0].name;

// New way (same code works, but now returns translated)
const categoryName = response.data[0].name; // Dutch if Accept-Language: nl
const englishName = response.data[0].name_en; // Always English
```

### Step 3: Handle Fallbacks
```javascript
const displayName = response.data[0].name || response.data[0].name_en || 'N/A';
```

### Step 4: Display Language Indicator
```javascript
const currentLocale = response.data.locale; // 'nl' or 'en'
```

---

## ✅ Backward Compatibility

**Good News**: All changes are **backward compatible**!

- Old apps will continue to work without changes
- `name` field still exists (now translated)
- New `name_en` field added (doesn't break old apps)
- Default language is English if no header sent

---

## 🆘 Troubleshooting

### Issue: Still getting English responses
**Solution**: Verify Accept-Language header is set correctly
```bash
curl -v -H "Accept-Language: nl" https://api.example.com/api/v1/categories
```

### Issue: Missing translations
**Solution**: Check admin panel at `/admin/translations` to add missing translations

### Issue: Null values in translated fields
**Solution**: Translation doesn't exist, will fallback to English. Add via admin panel.

---

**Last Updated**: November 30, 2025  
**API Version**: v1  
**Languages Supported**: English (en), Dutch (nl)
