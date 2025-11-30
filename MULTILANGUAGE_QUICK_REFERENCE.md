# Quick Reference - Multi-Language Support

## 🚀 Quick Start

### Accessing User Language

```kotlin
// In ViewModel or Repository
val preferencesManager = PreferencesManager(context)
val language = preferencesManager.language.first() // "en" or "nl"
val isEnglish = preferencesManager.isEnglish.first() // true or false
```

### Changing Language

```kotlin
// Save language preference
lifecycleScope.launch {
    preferencesManager.setLanguage("nl") // or "en"
    // or
    preferencesManager.setLanguage(false) // false = Dutch, true = English
}
```

---

## 📝 Using Translations in UI

### Method 1: Strings Utility (Recommended for dynamic content)

```kotlin
import com.dreamdiver.rotterdam.util.Strings

@Composable
fun MyScreen(isEnglish: Boolean) {
    Text(text = Strings.home(isEnglish))           // "Home" or "Thuis"
    Text(text = Strings.profile(isEnglish))        // "Profile" or "Profiel"
    Text(text = Strings.changeLanguage(isEnglish)) // "Change Language" or "Taal Wijzigen"
}
```

### Method 2: Android String Resources (Best for static content)

```kotlin
import androidx.compose.ui.res.stringResource
import com.dreamdiver.rotterdam.R

@Composable
fun MyScreen() {
    Text(text = stringResource(R.string.nav_home))     // Auto-translates
    Text(text = stringResource(R.string.login))        // Based on device
    Text(text = stringResource(R.string.edit_profile)) // language settings
}
```

---

## 🌐 API Integration

### Automatic Language Header

All API calls automatically include the Accept-Language header:

```kotlin
// No code needed! RetrofitInstance handles it automatically
val categories = apiService.getCategories()
// Request includes: Accept-Language: nl (or en)
```

### Using Translated API Data

```kotlin
// API returns translated data based on Accept-Language header
data class Category(
    val name: String,      // "Nooddiensten" (if Dutch selected)
    val name_en: String?   // "Emergency Services" (fallback)
)

// Display in UI
Text(text = category.name) // Shows translated name automatically
```

---

## ➕ Adding New Translations

### Step 1: Add to Strings.kt

```kotlin
// File: app/src/main/java/com/dreamdiver/rotterdam/util/Strings.kt
object Strings {
    // Add your new string
    fun myNewString(isEnglish: Boolean) = 
        if (isEnglish) "English Text" else "Nederlandse Tekst"
}
```

### Step 2: Add to String Resources

**English** - `app/src/main/res/values/strings.xml`:
```xml
<string name="my_new_string">English Text</string>
```

**Dutch** - `app/src/main/res/values-nl/strings.xml`:
```xml
<string name="my_new_string">Nederlandse Tekst</string>
```

### Step 3: Use in UI

```kotlin
// Method 1: Strings utility
Text(text = Strings.myNewString(isEnglish))

// Method 2: String resource
Text(text = stringResource(R.string.my_new_string))
```

---

## 📋 Available Translations

### Navigation
- `home()` - Home / Thuis
- `favorites()` - Favorites / Favorieten
- `profile()` - Profile / Profiel
- `more()` - More / Meer

### Language Settings
- `languageSettings()` - Language Settings / Taalinstellingen
- `changeLanguage()` - Change Language / Taal Wijzigen
- `englishDutch()` - English / Dutch or Engels / Nederlands

### Information
- `information()` - Information / Informatie
- `notice()` - Notice / Kennisgeving
- `aboutUs()` - About Us / Over Ons

### Legal
- `legal()` - Legal / Juridisch
- `privacyPolicy()` - Privacy Policy / Privacybeleid
- `termsConditions()` - Terms & Conditions / Algemene Voorwaarden

### Categories
- `hospital()` - Hospital / Ziekenhuis
- `doctor()` - Doctor / Dokter
- `ambulance()` - Ambulance / Ambulance
- `fireService()` - Fire Service / Brandweer
- `educational()` - Educational / Onderwijs
- ...and 15+ more

**See `Strings.kt` for complete list of 60+ translations**

---

## 🎨 Common Patterns

### Language Toggle Switch

```kotlin
Switch(
    checked = isEnglish,
    onCheckedChange = { newValue ->
        lifecycleScope.launch {
            preferencesManager.setLanguage(newValue)
        }
    }
)
```

### Language-Aware Screen

```kotlin
@Composable
fun MyScreen(
    isEnglish: Boolean,
    onLanguageChange: (Boolean) -> Unit
) {
    Column {
        Text(text = Strings.home(isEnglish))
        Button(onClick = { onLanguageChange(!isEnglish) }) {
            Text(Strings.changeLanguage(isEnglish))
        }
    }
}
```

### Passing Language State

```kotlin
// In MainActivity
val isEnglish = preferencesManager.isEnglish.collectAsState(initial = true)

MyScreen(
    isEnglish = isEnglish.value,
    onLanguageChange = { newValue ->
        lifecycleScope.launch {
            preferencesManager.setLanguage(newValue)
        }
    }
)
```

---

## 🔍 Debugging

### Check Current Language

```kotlin
// In ViewModel
viewModelScope.launch {
    val currentLang = preferencesManager.language.first()
    Log.d("Language", "Current language: $currentLang")
}
```

### Verify API Header

Check Logcat for OkHttp logs:
```
D/OkHttp: Accept-Language: nl
```

### Test Language Switch

```kotlin
// Force English
preferencesManager.setLanguage("en")

// Force Dutch
preferencesManager.setLanguage("nl")
```

---

## ⚠️ Common Mistakes

### ❌ Don't hardcode strings

```kotlin
// BAD
Text(text = if (isEnglish) "Login" else "Inloggen")

// GOOD
Text(text = Strings.login(isEnglish))
```

### ❌ Don't forget isEnglish parameter

```kotlin
// BAD
Text(text = Strings.home()) // Compilation error!

// GOOD
Text(text = Strings.home(isEnglish))
```

### ❌ Don't use wrong language code

```kotlin
// BAD
preferencesManager.setLanguage("nl_NL") // Wrong!

// GOOD
preferencesManager.setLanguage("nl") // Correct
```

---

## 🌍 Supported Languages

| Code | Language | Status |
|------|----------|--------|
| `en` | English | ✅ Complete |
| `nl` | Dutch | ✅ Complete |

---

## 📱 Testing Checklist

- [ ] Toggle language in More screen
- [ ] Check UI updates immediately
- [ ] Navigate between screens
- [ ] Verify API calls include header
- [ ] Restart app - language persists
- [ ] Check all string translations
- [ ] Test on different screen sizes
- [ ] Verify fallback to English works

---

## 🆘 Troubleshooting

### UI not updating after language change?
Make sure `isEnglish` is collected as State:
```kotlin
val isEnglish = preferencesManager.isEnglish.collectAsState(initial = true)
```

### API still returning English?
1. Check Accept-Language header in network logs
2. Verify RetrofitInstance.init() called in MainActivity
3. Confirm API supports multi-language

### Strings showing "null" or empty?
Use fallback pattern:
```kotlin
val displayName = category.name ?: category.name_en ?: "Unknown"
```

---

## 📚 Related Files

- `PreferencesManager.kt` - Language storage
- `RetrofitInstance.kt` - API header interceptor
- `Strings.kt` - Translation utility
- `values/strings.xml` - English resources
- `values-nl/strings.xml` - Dutch resources
- `MoreScreen.kt` - Language toggle UI

---

## 🎯 Best Practices

1. **Use Strings utility** for dynamic content
2. **Use string resources** for static content
3. **Always pass isEnglish** parameter to Strings methods
4. **Provide fallback** for API translations (use name_en)
5. **Test both languages** before release
6. **Keep translations updated** when adding features

---

**Quick Tip**: To add a new screen with language support:

```kotlin
@Composable
fun NewScreen(isEnglish: Boolean) {
    Text(text = Strings.home(isEnglish))
    Text(text = stringResource(R.string.app_name))
    // Mix both approaches as needed
}
```

---

**Last Updated**: November 30, 2025  
**Languages**: en (English), nl (Dutch)

