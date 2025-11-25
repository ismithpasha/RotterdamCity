package com.dreamdiver.rotterdam.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.PreferencesManager
import com.dreamdiver.rotterdam.ui.viewmodel.AuthState
import com.dreamdiver.rotterdam.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: AuthViewModel,
    preferencesManager: PreferencesManager,
    onBackClick: () -> Unit,
    isEnglish: Boolean = true
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }

    // Worker-specific fields
    var skillCategory by remember { mutableStateOf("") }
    var skillsText by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }
    var experienceYears by remember { mutableStateOf("") }
    var certificationsText by remember { mutableStateOf("") }

    // Availability fields
    var mondayAvail by remember { mutableStateOf("") }
    var tuesdayAvail by remember { mutableStateOf("") }
    var wednesdayAvail by remember { mutableStateOf("") }
    var thursdayAvail by remember { mutableStateOf("") }
    var fridayAvail by remember { mutableStateOf("") }
    var saturdayAvail by remember { mutableStateOf("") }
    var sundayAvail by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        avatarUri = uri
    }

    // Load user data
    LaunchedEffect(Unit) {
        scope.launch {
            name = preferencesManager.userName.first() ?: ""
            phone = preferencesManager.userPhone.first() ?: ""
            city = preferencesManager.userCity.first() ?: ""
            userType = preferencesManager.userType.first() ?: ""
            token = preferencesManager.authToken.first() ?: ""
        }
    }

    // Handle success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onBackClick()
            viewModel.resetAuthState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEnglish) "Edit Profile" else "Profiel Bewerken") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Avatar Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUri != null) {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Select Avatar",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isEnglish) "Tap to change photo" else "Tik om foto te wijzigen",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Basic Information
            Text(
                text = if (isEnglish) "Basic Information" else "Basisinformatie",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(if (isEnglish) "Full Name" else "Volledige Naam") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(if (isEnglish) "Phone" else "Telefoon") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(if (isEnglish) "Address" else "Adres") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text(if (isEnglish) "City" else "Stad") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text(if (isEnglish) "State" else "Provincie") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = zipCode,
                onValueChange = { zipCode = it },
                label = { Text(if (isEnglish) "Zip Code" else "Postcode") },
                leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text(if (isEnglish) "Latitude" else "Breedtegraad") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text(if (isEnglish) "Longitude" else "Lengtegraad") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }

            // Worker-specific fields
            if (userType == "worker") {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isEnglish) "Professional Information" else "Professionele Informatie",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = skillCategory,
                    onValueChange = { skillCategory = it },
                    label = { Text(if (isEnglish) "Skill Category" else "Vaardigheid Categorie") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = skillsText,
                    onValueChange = { skillsText = it },
                    label = { Text(if (isEnglish) "Skills (comma separated)" else "Vaardigheden (gescheiden door komma's)") },
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Wiring, Installation, Repair") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(if (isEnglish) "Bio" else "Bio") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hourlyRate,
                        onValueChange = { hourlyRate = it },
                        label = { Text("€ " + if (isEnglish) "Hourly Rate" else "Uurtarief") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    OutlinedTextField(
                        value = experienceYears,
                        onValueChange = { experienceYears = it },
                        label = { Text(if (isEnglish) "Experience (years)" else "Ervaring (jaren)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = certificationsText,
                    onValueChange = { certificationsText = it },
                    label = { Text(if (isEnglish) "Certifications (comma separated)" else "Certificaten (gescheiden door komma's)") },
                    leadingIcon = { Icon(Icons.Default.CardMembership, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Licensed Electrician, OSHA Certified") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isEnglish) "Availability" else "Beschikbaarheid",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                listOf(
                    "Monday" to mondayAvail to { v: String -> mondayAvail = v },
                    "Tuesday" to tuesdayAvail to { v: String -> tuesdayAvail = v },
                    "Wednesday" to wednesdayAvail to { v: String -> wednesdayAvail = v },
                    "Thursday" to thursdayAvail to { v: String -> thursdayAvail = v },
                    "Friday" to fridayAvail to { v: String -> fridayAvail = v },
                    "Saturday" to saturdayAvail to { v: String -> saturdayAvail = v },
                    "Sunday" to sundayAvail to { v: String -> sundayAvail = v }
                ).forEach { (dayPair, setter) ->
                    OutlinedTextField(
                        value = dayPair.second,
                        onValueChange = setter,
                        label = { Text(dayPair.first) },
                        placeholder = { Text("e.g., 9am-5pm") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error Message
            if (authState is AuthState.Error) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Update Button
            Button(
                onClick = {
                    if (name.isNotBlank() && token.isNotBlank()) {
                        val avatarFile = avatarUri?.let { uri ->
                            uriToFile(context, uri)
                        }

                        val skillsList = if (skillsText.isNotBlank()) {
                            skillsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        } else null

                        val certsList = if (certificationsText.isNotBlank()) {
                            certificationsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        } else null

                        val availabilityMap = mutableMapOf<String, String>()
                        if (mondayAvail.isNotBlank()) availabilityMap["monday"] = mondayAvail
                        if (tuesdayAvail.isNotBlank()) availabilityMap["tuesday"] = tuesdayAvail
                        if (wednesdayAvail.isNotBlank()) availabilityMap["wednesday"] = wednesdayAvail
                        if (thursdayAvail.isNotBlank()) availabilityMap["thursday"] = thursdayAvail
                        if (fridayAvail.isNotBlank()) availabilityMap["friday"] = fridayAvail
                        if (saturdayAvail.isNotBlank()) availabilityMap["saturday"] = saturdayAvail
                        if (sundayAvail.isNotBlank()) availabilityMap["sunday"] = sundayAvail

                        viewModel.updateProfile(
                            token = token,
                            name = name,
                            phone = phone.ifBlank { null },
                            address = address.ifBlank { null },
                            city = city.ifBlank { null },
                            state = state.ifBlank { null },
                            zipCode = zipCode.ifBlank { null },
                            latitude = latitude.toDoubleOrNull(),
                            longitude = longitude.toDoubleOrNull(),
                            avatarFile = avatarFile,
                            skillCategory = if (userType == "worker") skillCategory.ifBlank { null } else null,
                            skills = if (userType == "worker") skillsList else null,
                            bio = if (userType == "worker") bio.ifBlank { null } else null,
                            hourlyRate = if (userType == "worker") hourlyRate.toDoubleOrNull() else null,
                            experienceYears = if (userType == "worker") experienceYears.toIntOrNull() else null,
                            certifications = if (userType == "worker") certsList else null,
                            availability = if (userType == "worker" && availabilityMap.isNotEmpty()) availabilityMap else null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (isEnglish) "Update Profile" else "Profiel Bijwerken",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

