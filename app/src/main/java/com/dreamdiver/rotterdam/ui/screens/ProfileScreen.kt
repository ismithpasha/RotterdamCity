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
    isEnglish: Boolean = true
) {
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userCity by remember { mutableStateOf("") }
    var userAvatar by remember { mutableStateOf<String?>(null) }
    var authToken by remember { mutableStateOf("") }

    // Function to load user data
    fun loadUserData() {
        scope.launch {
            userName = preferencesManager.userName.first() ?: ""
            userEmail = preferencesManager.userEmail.first() ?: ""
            userType = preferencesManager.userType.first() ?: ""
            userPhone = preferencesManager.userPhone.first() ?: ""
            userCity = preferencesManager.userCity.first() ?: ""
            userAvatar = preferencesManager.userAvatar.first()
            authToken = preferencesManager.authToken.first() ?: ""
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

            // City
            if (userCity.isNotBlank()) {
                ProfileInfoItem(
                    icon = Icons.Default.LocationOn,
                    label = if (isEnglish) "City" else "Stad",
                    value = userCity
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Account Type
            ProfileInfoItem(
                icon = Icons.Default.AccountBox,
                label = if (isEnglish) "Account Type" else "Accounttype",
                value = userType.replaceFirstChar { it.uppercase() }
            )

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

