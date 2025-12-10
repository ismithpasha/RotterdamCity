package com.dreamdiver.rotterdam.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.PreferencesManager
import com.dreamdiver.rotterdam.ui.viewmodel.AuthViewModel
import com.dreamdiver.rotterdam.util.Strings

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    isEnglish: Boolean = true,
    onLanguageChange: (Boolean) -> Unit = {},
    onNavigateToNotice: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    preferencesManager: PreferencesManager? = null,
    authViewModel: AuthViewModel? = null,
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val isLoggedIn by authViewModel?.isLoggedIn?.collectAsState() ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    // Get user data from preferences if logged in
    val userName by preferencesManager?.userName?.collectAsState(initial = "") ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    val userEmail by preferencesManager?.userEmail?.collectAsState(initial = "") ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    val userAvatar by preferencesManager?.userAvatar?.collectAsState(initial = null) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = Strings.moreOptions(isEnglish),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Profile Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Strings.profile(isEnglish),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (isLoggedIn && !userName.isNullOrEmpty()) {
                    // Show user profile
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = userAvatar,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userName ?: "",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userEmail ?: "",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    MoreOptionItem(
                        icon = Icons.Default.Edit,
                        title = if (isEnglish) "Edit Profile" else "Profiel bewerken",
                        subtitle = if (isEnglish) "Update your information" else "Werk uw informatie bij",
                        onClick = onEditProfile
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    MoreOptionItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = if (isEnglish) "Logout" else "Uitloggen",
                        subtitle = if (isEnglish) "Sign out from your account" else "Uitloggen van uw account",
                        onClick = onLogout
                    )
                } else {
                    // Show login/register buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateToLogin,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isEnglish) "Login" else "Inloggen")
                        }
                        Button(
                            onClick = onNavigateToRegister,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isEnglish) "Register" else "Registreren")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Strings.languageSettings(isEnglish),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                MoreOptionItem(
                    icon = Icons.Default.Language,
                    title = Strings.changeLanguage(isEnglish),
                    subtitle = Strings.englishDutch(isEnglish),
                    showSwitch = true,
                    switchChecked = isEnglish,
                    onSwitchChanged = { onLanguageChange(it) },
                    onClick = { onLanguageChange(!isEnglish) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Information Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Strings.information(isEnglish),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                MoreOptionItem(
                    icon = Icons.Default.Notifications,
                    title = Strings.notice(isEnglish),
                    subtitle = Strings.viewImportantNotices(isEnglish),
                    onClick = onNavigateToNotice
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                MoreOptionItem(
                    icon = Icons.Default.Info,
                    title = Strings.aboutUs(isEnglish),
                    subtitle = Strings.learnAbout(isEnglish),
                    onClick = onNavigateToAbout
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legal Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Strings.legal(isEnglish),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                MoreOptionItem(
                    icon = Icons.Default.PrivacyTip,
                    title = Strings.privacyPolicy(isEnglish),
                    subtitle = Strings.howWeHandleData(isEnglish),
                    onClick = onNavigateToPrivacy
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                MoreOptionItem(
                    icon = Icons.AutoMirrored.Filled.Article,
                    title = Strings.termsConditions(isEnglish),
                    subtitle = Strings.termsOfService(isEnglish),
                    onClick = onNavigateToTerms
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Other Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Strings.other(isEnglish),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                MoreOptionItem(
                    icon = Icons.Default.Star,
                    title = Strings.rateUs(isEnglish),
                    subtitle = Strings.rateOnPlayStore(isEnglish),
                    onClick = { /* Open Play Store */ }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                MoreOptionItem(
                    icon = Icons.Default.Share,
                    title = Strings.shareApp(isEnglish),
                    subtitle = Strings.shareWithFriends(isEnglish),
                    onClick = { /* Share app */ }
                )
            }
        }

        // App Version
        Text(
            text = Strings.version(isEnglish),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun MoreOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onSwitchChanged: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        if (showSwitch) {
            Switch(
                checked = switchChecked,
                onCheckedChange = onSwitchChanged
            )
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

