package com.dreamdiver.rotterdam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.model.Service
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    isEnglish: Boolean = true,
    allServices: List<Service> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }
    var filteredServices by remember { mutableStateOf<List<Service>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Service?>(null) }

    // Filter services based on search query
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            isSearching = true
            delay(300) // Debounce search
            filteredServices = allServices.filter { service ->
                val name = if (isEnglish) (service.nameEn ?: service.name) else service.name
                val description = if (isEnglish) (service.descriptionEn ?: service.description) else service.description

                name?.contains(searchQuery, ignoreCase = true) == true ||
                description?.contains(searchQuery, ignoreCase = true) == true
            }
            isSearching = false
        } else {
            filteredServices = emptyList()
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header with back button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = if (isEnglish) "Back" else "Terug",
                                tint = Color(0xFF2E3A59)
                            )
                        }
                        Text(
                            text = if (isEnglish) "Search Services" else "Zoek Diensten",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E3A59),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search TextField
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                if (isEnglish) "Search by service name..." else "Zoek op dienstnaam...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF0083CA)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isSearching -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF0083CA))
                    }
                }
                searchQuery.isEmpty() -> {
                    // Empty search state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color(0xFFE0E0E0)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isEnglish) "Search for services" else "Zoek naar diensten",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish)
                                "Enter keywords to find services"
                            else
                                "Voer trefwoorden in om diensten te vinden",
                            fontSize = 14.sp,
                            color = Color(0xFFCCCCCC),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                filteredServices.isEmpty() -> {
                    // No results state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isEnglish) "No services found" else "Geen diensten gevonden",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish)
                                "Try different keywords"
                            else
                                "Probeer andere trefwoorden",
                            fontSize = 14.sp,
                            color = Color(0xFFCCCCCC),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    // Results list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = if (isEnglish)
                                    "${filteredServices.size} service(s) found"
                                else
                                    "${filteredServices.size} dienst(en) gevonden",
                                fontSize = 14.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(filteredServices) { service ->
                            SearchResultCard(
                                service = service,
                                isEnglish = isEnglish,
                                searchQuery = searchQuery,
                                onClick = { selectedService = service }
                            )
                        }
                    }
                }
            }
        }
    }

    // Show service detail modal if selected
    selectedService?.let { service ->
        val serviceDetail = com.dreamdiver.rotterdam.data.model.ServiceDetail(
            id = service.id,
            categoryId = service.categoryId,
            category = service.category,
            name = service.name,
            nameEn = service.nameEn,
            phone = service.phone,
            address = service.address,
            addressEn = service.addressEn,
            latitude = service.latitude,
            longitude = service.longitude,
            googleMapsUrl = service.googleMapsUrl,
            description = service.description,
            descriptionEn = service.descriptionEn,
            image = service.image?.takeIf { it.isNotEmpty() } ?: "https://via.placeholder.com/400x200?text=No+Image",
            status = service.status,
            locale = service.locale,
            createdAt = service.createdAt,
            updatedAt = service.updatedAt
        )

        ServiceDetailModal(
            serviceDetail = serviceDetail,
            onDismiss = { selectedService = null }
        )
    }
}

@Composable
fun SearchResultCard(
    service: Service,
    isEnglish: Boolean,
    searchQuery: String,
    onClick: () -> Unit
) {
    val displayName = if (isEnglish) (service.nameEn ?: service.name) else service.name
    val displayDescription = if (isEnglish) (service.descriptionEn ?: service.description) else service.description

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Service icon/image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0EFFF)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = service.category?.icon ?: service.image,
                    contentDescription = displayName,
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Service details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = displayName ?: if (isEnglish) "Unnamed Service" else "Naamloze Dienst",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E3A59),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!displayDescription.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = displayDescription,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }

                // Category badge
                service.category?.let { category ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE8F4FF)
                    ) {
                        Text(
                            text = if (isEnglish) (category.nameEn ?: category.name) else category.name,
                            fontSize = 11.sp,
                            color = Color(0xFF0083CA),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


