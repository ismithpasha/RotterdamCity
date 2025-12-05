package com.dreamdiver.rotterdam 

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.BackHandler
import kotlinx.parcelize.Parcelize
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dreamdiver.rotterdam.data.PreferencesManager
import com.dreamdiver.rotterdam.ui.screens.AboutUsScreen
import com.dreamdiver.rotterdam.ui.screens.EducationalScreen
import com.dreamdiver.rotterdam.ui.screens.EditProfileScreen
import com.dreamdiver.rotterdam.ui.screens.EmergencyServiceScreen
import com.dreamdiver.rotterdam.ui.screens.FavoritesScreen
import com.dreamdiver.rotterdam.ui.screens.HomeScreen
import com.dreamdiver.rotterdam.ui.screens.HospitalListScreen
import com.dreamdiver.rotterdam.ui.screens.LoginScreen
import com.dreamdiver.rotterdam.ui.screens.MoreScreen
import com.dreamdiver.rotterdam.ui.screens.NoticeScreen
import com.dreamdiver.rotterdam.ui.screens.PrivacyPolicyScreen
import com.dreamdiver.rotterdam.ui.screens.ProfileScreen
import com.dreamdiver.rotterdam.ui.screens.RegisterScreen
import com.dreamdiver.rotterdam.ui.screens.ServiceListScreen
import com.dreamdiver.rotterdam.ui.screens.SubCategoryListScreen
import com.dreamdiver.rotterdam.ui.screens.TermsConditionsScreen
import com.dreamdiver.rotterdam.ui.theme.RotterdamCityTheme
import com.dreamdiver.rotterdam.ui.viewmodel.AuthViewModel
import com.dreamdiver.rotterdam.ui.viewmodel.HomeViewModel
import com.dreamdiver.rotterdam.util.Strings
import com.dreamdiver.rotterdam.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize RetrofitInstance with context for language support
        RetrofitInstance.init(applicationContext)

        preferencesManager = PreferencesManager(applicationContext)
        authViewModel = AuthViewModel(preferencesManager = preferencesManager)
        enableEdgeToEdge()
        setContent {
            RotterdamCityTheme {
                val isEnglish = preferencesManager.isEnglish.collectAsState(initial = true)
                val isLoggedIn = authViewModel.isLoggedIn.collectAsState()
                CumillaCityApp(
                    isEnglish = isEnglish.value,
                    isLoggedIn = isLoggedIn.value,
                    authViewModel = authViewModel,
                    preferencesManager = preferencesManager,
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
    isLoggedIn: Boolean = false,
    authViewModel: AuthViewModel? = null,
    preferencesManager: PreferencesManager? = null,
    onLanguageChange: (Boolean) -> Unit = {}
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var currentDetailScreen by rememberSaveable { mutableStateOf<DetailScreen?>(null) }
    var serviceListState by rememberSaveable { mutableStateOf<ServiceListState?>(null) }
    var subcategoryListState by rememberSaveable { mutableStateOf<SubCategoryListState?>(null) }
    var showAuthScreen by rememberSaveable { mutableStateOf<AuthScreen?>(null) }
    var showExitDialog by rememberSaveable { mutableStateOf(false) }

    // Get activity context for exit functionality
    val activity = LocalContext.current as? ComponentActivity

    // Handle back button press
    BackHandler(enabled = true) {
        when {
            // If showing detail screen, go back to main navigation
            currentDetailScreen != null -> {
                currentDetailScreen = null
                serviceListState = null
                subcategoryListState = null
            }
            // If not on home screen, go to home
            currentDestination != AppDestinations.HOME -> {
                currentDestination = AppDestinations.HOME
            }
            // If on home screen, show exit confirmation
            else -> {
                showExitDialog = true
            }
        }
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(if (isEnglish) "Exit App" else "অ্যাপ থেকে বের হন") },
            text = { Text(if (isEnglish) "Are you sure you want to exit?" else "আপনি কি নিশ্চিত যে আপনি প্রস্থান করতে চান?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        activity?.finish()
                    }
                ) {
                    Text(if (isEnglish) "Exit" else "প্রস্থান")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(if (isEnglish) "Cancel" else "বাতিল")
                }
            }
        )
    }

    // Create a shared HomeViewModel to manage home screen state
    val homeViewModel: HomeViewModel = viewModel()

    // Reload home data whenever returning to HOME destination
    LaunchedEffect(currentDestination) {
        if (currentDestination == AppDestinations.HOME) {
            homeViewModel.loadCategories()
        }
    }

    // Show auth screens if not logged in
    if (!isLoggedIn && showAuthScreen != null && authViewModel != null) {
        when (showAuthScreen) {
            AuthScreen.LOGIN -> LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { showAuthScreen = null },
                onNavigateToRegister = { showAuthScreen = AuthScreen.REGISTER },
                onBackToHome = { showAuthScreen = null },
                isEnglish = isEnglish
            )
            AuthScreen.REGISTER -> RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { showAuthScreen = null },
                onNavigateToLogin = { showAuthScreen = AuthScreen.LOGIN },
                onBackToHome = { showAuthScreen = null },
                isEnglish = isEnglish
            )
            null -> {} // Should never happen due to outer if condition
        }
        return
    }

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
            DetailScreen.SUBCATEGORY_LIST -> {
                subcategoryListState?.let { state ->
                    SubCategoryListScreen(
                        categoryId = state.categoryId,
                        categoryName = state.categoryName,
                        isEnglish = isEnglish,
                        onBackClick = {
                            currentDetailScreen = null
                            subcategoryListState = null
                        },
                        onSubCategoryClick = { subcategoryId, subcategoryName ->
                            serviceListState = ServiceListState(
                                subcategoryId = subcategoryId,
                                categoryName = subcategoryName
                            )
                            currentDetailScreen = DetailScreen.SERVICE_LIST
                        }
                    )
                }
            }
            DetailScreen.SERVICE_LIST -> {
                serviceListState?.let { state ->
                    ServiceListScreen(
                        categoryId = state.categoryId,
                        subcategoryId = state.subcategoryId,
                        categoryName = state.categoryName,
                        isEnglish = isEnglish,
                        onBackClick = {
                            currentDetailScreen = null
                            serviceListState = null
                        }
                    )
                }
            }
            DetailScreen.EDIT_PROFILE -> {
                if (preferencesManager != null && authViewModel != null) {
                    EditProfileScreen(
                        viewModel = authViewModel,
                        preferencesManager = preferencesManager,
                        onBackClick = { currentDetailScreen = null },
                        isEnglish = isEnglish
                    )
                }
            }
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
                        onNavigateToEducational = { currentDetailScreen = DetailScreen.EDUCATIONAL },
                        onNavigateToServiceList = { categoryId, categoryName ->
                            subcategoryListState = SubCategoryListState(categoryId, categoryName)
                            currentDetailScreen = DetailScreen.SUBCATEGORY_LIST
                        },
                        viewModel = homeViewModel
                    )
                    AppDestinations.FAVORITES -> FavoritesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                    AppDestinations.PROFILE -> {
                        if (preferencesManager != null && authViewModel != null) {
                            ProfileScreen(
                                modifier = Modifier.padding(innerPadding),
                                preferencesManager = preferencesManager,
                                authViewModel = authViewModel,
                                onLogout = { authViewModel.logout() },
                                onEditProfile = { currentDetailScreen = DetailScreen.EDIT_PROFILE },
                                onNavigateToLogin = { showAuthScreen = AuthScreen.LOGIN },
                                onNavigateToRegister = { showAuthScreen = AuthScreen.REGISTER },
                                isEnglish = isEnglish
                            )
                        }
                    }
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
    EDUCATIONAL,
    SUBCATEGORY_LIST,
    SERVICE_LIST,
    EDIT_PROFILE
}

enum class AuthScreen {
    LOGIN,
    REGISTER
}

// State holder for service list navigation
@Parcelize
data class ServiceListState(
    val categoryId: Int? = null,
    val subcategoryId: Int? = null,
    val categoryName: String
) : Parcelable

// State holder for subcategory list navigation
@Parcelize
data class SubCategoryListState(
    val categoryId: Int,
    val categoryName: String
) : Parcelable

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

