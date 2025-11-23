package com.dreamdiver.rotterdam 

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.lifecycleScope
import com.dreamdiver.rotterdam.data.PreferencesManager
import com.dreamdiver.rotterdam.ui.screens.AboutUsScreen
import com.dreamdiver.rotterdam.ui.screens.EducationalScreen
import com.dreamdiver.rotterdam.ui.screens.EmergencyServiceScreen
import com.dreamdiver.rotterdam.ui.screens.FavoritesScreen
import com.dreamdiver.rotterdam.ui.screens.HomeScreen
import com.dreamdiver.rotterdam.ui.screens.HospitalListScreen
import com.dreamdiver.rotterdam.ui.screens.MoreScreen
import com.dreamdiver.rotterdam.ui.screens.NoticeScreen
import com.dreamdiver.rotterdam.ui.screens.PrivacyPolicyScreen
import com.dreamdiver.rotterdam.ui.screens.ProfileScreen
import com.dreamdiver.rotterdam.ui.screens.TermsConditionsScreen
import com.dreamdiver.rotterdam.ui.theme.RotterdamCityTheme
import com.dreamdiver.rotterdam.util.Strings
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(applicationContext)
        enableEdgeToEdge()
        setContent {
            RotterdamCityTheme {
                val isEnglish = preferencesManager.isEnglish.collectAsState(initial = true)
                CumillaCityApp(
                    isEnglish = isEnglish.value,
                    onLanguageChange = { newIsEnglish ->
                        lifecycleScope.launch {
                            preferencesManager.setLanguage(newIsEnglish)
                        }
                    }
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun CumillaCityApp(
    isEnglish: Boolean = true,
    onLanguageChange: (Boolean) -> Unit = {}
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var currentDetailScreen by rememberSaveable { mutableStateOf<DetailScreen?>(null) }

    if (currentDetailScreen != null) {
        // Show detail screen without bottom navigation
        when (currentDetailScreen) {
            DetailScreen.NOTICE -> NoticeScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            DetailScreen.ABOUT_US -> AboutUsScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            DetailScreen.PRIVACY_POLICY -> PrivacyPolicyScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            DetailScreen.TERMS_CONDITIONS -> TermsConditionsScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            DetailScreen.EMERGENCY_SERVICE -> EmergencyServiceScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            DetailScreen.HOSPITAL_LIST -> HospitalListScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            DetailScreen.EDUCATIONAL -> EducationalScreen(
                isEnglish = isEnglish,
                onBackClick = { currentDetailScreen = null }
            )
            null -> {} // Should never happen due to if condition above
        }
    } else {
        // Show main screens with bottom navigation
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = it.getLabel(isEnglish)
                            )
                        },
                        label = { Text(it.getLabel(isEnglish)) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        ) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                when (currentDestination) {
                    AppDestinations.HOME -> HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        isEnglish = isEnglish,
                        onNavigateToEmergency = { currentDetailScreen = DetailScreen.EMERGENCY_SERVICE },
                        onNavigateToHospital = { currentDetailScreen = DetailScreen.HOSPITAL_LIST },
                        onNavigateToEducational = { currentDetailScreen = DetailScreen.EDUCATIONAL }
                    )
                    AppDestinations.FAVORITES -> FavoritesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                    AppDestinations.PROFILE -> ProfileScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                    AppDestinations.MORE -> MoreScreen(
                        modifier = Modifier.padding(innerPadding),
                        isEnglish = isEnglish,
                        onLanguageChange = onLanguageChange,
                        onNavigateToNotice = { currentDetailScreen = DetailScreen.NOTICE },
                        onNavigateToAbout = { currentDetailScreen = DetailScreen.ABOUT_US },
                        onNavigateToPrivacy = { currentDetailScreen = DetailScreen.PRIVACY_POLICY },
                        onNavigateToTerms = { currentDetailScreen = DetailScreen.TERMS_CONDITIONS }
                    )
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
    MORE("More", Icons.Default.MoreHoriz);

    fun getLabel(isEnglish: Boolean): String {
        return when (this) {
            HOME -> Strings.home(isEnglish)
            FAVORITES -> Strings.favorites(isEnglish)
            PROFILE -> Strings.profile(isEnglish)
            MORE -> Strings.more(isEnglish)
        }
    }
}

enum class DetailScreen {
    NOTICE,
    ABOUT_US,
    PRIVACY_POLICY,
    TERMS_CONDITIONS,
    EMERGENCY_SERVICE,
    HOSPITAL_LIST,
    EDUCATIONAL
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RotterdamCityTheme {
        Greeting("Android")
    }
}

