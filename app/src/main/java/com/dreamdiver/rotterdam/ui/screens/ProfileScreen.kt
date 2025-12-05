package com.dreamdiver.rotterdam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.PreferencesManager
import com.dreamdiver.rotterdam.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    preferencesManager: PreferencesManager,
    authViewModel: AuthViewModel? = null,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    isEnglish: Boolean = true
) {
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userCity by remember { mutableStateOf("") }
    var userAvatar by remember { mutableStateOf<String?>(null) }
    var userAddress by remember { mutableStateOf("") }
    var userState by remember { mutableStateOf("") }
    var userZipCode by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }
    var userSkillCategory by remember { mutableStateOf("") }
    var userHourlyRate by remember { mutableStateOf("") }
    var userExperienceYears by remember { mutableStateOf<Int?>(null) }
    var userSkills by remember { mutableStateOf<List<String>>(emptyList()) }
    var userCertifications by remember { mutableStateOf<List<String>>(emptyList()) }
    var userAverageRating by remember { mutableStateOf("") }
    var userTotalRatings by remember { mutableStateOf<Int?>(null) }
    var authToken by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }

    // Function to load user data
    fun loadUserData() {
        scope.launch {
            userName = preferencesManager.userName.first() ?: ""
            userEmail = preferencesManager.userEmail.first() ?: ""
            userType = preferencesManager.userType.first() ?: ""
            userPhone = preferencesManager.userPhone.first() ?: ""
            userCity = preferencesManager.userCity.first() ?: ""
            userAvatar = preferencesManager.userAvatar.first()
            userAddress = preferencesManager.userAddress.first() ?: ""
            userState = preferencesManager.userState.first() ?: ""
            userZipCode = preferencesManager.userZipCode.first() ?: ""
            userBio = preferencesManager.userBio.first() ?: ""
            userSkillCategory = preferencesManager.userSkillCategory.first() ?: ""
            userHourlyRate = preferencesManager.userHourlyRate.first() ?: ""
            userExperienceYears = preferencesManager.userExperienceYears.first()
            val skillsString = preferencesManager.userSkills.first() ?: ""
            userSkills = if (skillsString.isNotBlank()) skillsString.split(",") else emptyList()
            val certificationsString = preferencesManager.userCertifications.first() ?: ""
            userCertifications = if (certificationsString.isNotBlank()) certificationsString.split(",") else emptyList()
            userAverageRating = preferencesManager.userAverageRating.first() ?: ""
            userTotalRatings = preferencesManager.userTotalRatings.first()
            authToken = preferencesManager.authToken.first() ?: ""
            isLoggedIn = authToken.isNotBlank()
        }
    }

    // Load initial data and call profile API
    LaunchedEffect(Unit) {
        loadUserData()
        // Automatically fetch profile from server after loading local data
        if (authToken.isNotBlank() && authViewModel != null) {
            authViewModel.getProfile(authToken)
        }
    }

    // Observe auth state for automatic profile fetch
    authViewModel?.let { viewModel ->
        val authState by viewModel.authState.collectAsState()

        LaunchedEffect(authState) {
            when (authState) {
                is com.dreamdiver.rotterdam.ui.viewmodel.AuthState.Success -> {
                    loadUserData() // Reload user data after successful refresh
                    viewModel.resetAuthState()
                }
                else -> {
                    // Do nothing for other states
                }
            }
        }
    }

    // Show login/register options if not logged in, otherwise show profile
    if (!isLoggedIn) {
        LoginRegisterView(
            modifier = modifier,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToRegister = onNavigateToRegister,
            isEnglish = isEnglish
        )
    } else {
        // Existing profile screen content
        LoggedInProfileView(
            modifier = modifier,
            userName = userName,
            userEmail = userEmail,
            userType = userType,
            userPhone = userPhone,
            userCity = userCity,
            userAvatar = userAvatar,
            userAddress = userAddress,
            userState = userState,
            userZipCode = userZipCode,
            userBio = userBio,
            userSkillCategory = userSkillCategory,
            userHourlyRate = userHourlyRate,
            userExperienceYears = userExperienceYears,
            userSkills = userSkills,
            userCertifications = userCertifications,
            userAverageRating = userAverageRating,
            userTotalRatings = userTotalRatings,
            onLogout = onLogout,
            onEditProfile = onEditProfile,
            isEnglish = isEnglish
        )
    }
}

@Composable
fun LoginRegisterView(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    isEnglish: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isEnglish) "Welcome to Rotterdam City" else "Welkom in Rotterdam",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isEnglish)
                "Sign in to access your profile and personalized services"
            else
                "Meld u aan om toegang te krijgen tot uw profiel en gepersonaliseerde diensten",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Login Button
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isEnglish) "Sign In" else "Aanmelden",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        OutlinedButton(
            onClick = onNavigateToRegister,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isEnglish) "Create Account" else "Account Aanmaken",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isEnglish)
                "Browse services without logging in"
            else
                "Blader door diensten zonder in te loggen",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoggedInProfileView(
    modifier: Modifier = Modifier,
    userName: String,
    userEmail: String,
    userType: String,
    userPhone: String,
    userCity: String,
    userAvatar: String?,
    userAddress: String,
    userState: String,
    userZipCode: String,
    userBio: String,
    userSkillCategory: String,
    userHourlyRate: String,
    userExperienceYears: Int?,
    userSkills: List<String>,
    userCertifications: List<String>,
    userAverageRating: String,
    userTotalRatings: Int?,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    isEnglish: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    if (!userAvatar.isNullOrBlank()) {
                        AsyncImage(
                            model = userAvatar,
                            contentDescription = "User Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userType.replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Profile Information Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (isEnglish) "Profile Information" else "Profielinformatie",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Email
            ProfileInfoItem(
                icon = Icons.Default.Email,
                label = if (isEnglish) "Email" else "E-mail",
                value = userEmail
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Phone
            if (userPhone.isNotBlank()) {
                ProfileInfoItem(
                    icon = Icons.Default.Phone,
                    label = if (isEnglish) "Phone" else "Telefoon",
                    value = userPhone
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Address
            if (userAddress.isNotBlank()) {
                ProfileInfoItem(
                    icon = Icons.Default.Home,
                    label = if (isEnglish) "Address" else "Adres",
                    value = userAddress
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // City
            if (userCity.isNotBlank()) {
                ProfileInfoItem(
                    icon = Icons.Default.LocationOn,
                    label = if (isEnglish) "City" else "Stad",
                    value = userCity
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // State
            if (userState.isNotBlank()) {
                ProfileInfoItem(
                    icon = Icons.Default.LocationCity,
                    label = if (isEnglish) "State" else "Staat",
                    value = userState
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Zip Code
            if (userZipCode.isNotBlank()) {
                ProfileInfoItem(
                    icon = Icons.Default.Numbers,
                    label = if (isEnglish) "Zip Code" else "Postcode",
                    value = userZipCode
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Account Type
            ProfileInfoItem(
                icon = Icons.Default.AccountBox,
                label = if (isEnglish) "Account Type" else "Accounttype",
                value = userType.replaceFirstChar { it.uppercase() }
            )

            // Worker-specific fields
            if (userType.lowercase() == "worker") {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isEnglish) "Professional Information" else "Professionele Informatie",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Skill Category
                if (userSkillCategory.isNotBlank()) {
                    ProfileInfoItem(
                        icon = Icons.Default.Work,
                        label = if (isEnglish) "Skill Category" else "Vaardigheid Categorie",
                        value = userSkillCategory.replaceFirstChar { it.uppercase() }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Hourly Rate
                if (userHourlyRate.isNotBlank()) {
                    ProfileInfoItem(
                        icon = Icons.Default.AttachMoney,
                        label = if (isEnglish) "Hourly Rate" else "Uurtarief",
                        value = "€${userHourlyRate}"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Experience Years
                userExperienceYears?.let { years ->
                    ProfileInfoItem(
                        icon = Icons.Default.Timeline,
                        label = if (isEnglish) "Experience" else "Ervaring",
                        value = if (isEnglish) "$years years" else "$years jaar"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Rating
                if (userAverageRating.isNotBlank()) {
                    val ratingText = if (userTotalRatings != null && userTotalRatings!! > 0) {
                        "$userAverageRating ⭐ ($userTotalRatings ${if (isEnglish) "ratings" else "beoordelingen"})"
                    } else {
                        if (isEnglish) "No ratings yet" else "Nog geen beoordelingen"
                    }
                    ProfileInfoItem(
                        icon = Icons.Default.Star,
                        label = if (isEnglish) "Rating" else "Beoordeling",
                        value = ratingText
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Bio
                if (userBio.isNotBlank()) {
                    ProfileInfoItem(
                        icon = Icons.Default.Description,
                        label = if (isEnglish) "Bio" else "Bio",
                        value = userBio
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Skills
                if (userSkills.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEnglish) "Skills" else "Vaardigheden",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                userSkills.forEach { skill ->
                                    Text(
                                        text = "• $skill",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Certifications
                if (userCertifications.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.CardMembership,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEnglish) "Certifications" else "Certificaten",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                userCertifications.forEach { cert ->
                                    Text(
                                        text = "• $cert",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Edit Profile Button
            OutlinedButton(
                onClick = onEditProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEnglish) "Edit Profile" else "Profiel Bewerken",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEnglish) "Logout" else "Uitloggen",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Favorites Screen\nComing Soon",
            fontSize = 20.sp
        )
    }
}

