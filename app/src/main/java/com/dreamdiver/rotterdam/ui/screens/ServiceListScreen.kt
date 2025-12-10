package com.dreamdiver.rotterdam.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.R
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.ui.viewmodel.ServiceUiState
import com.dreamdiver.rotterdam.ui.viewmodel.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreen(
    categoryId: Int? = null,
    subcategoryId: Int? = null,
    categoryName: String,
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {},
    viewModel: ServiceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val detailState by viewModel.detailState.collectAsState()
    var selectedServiceId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(categoryId, subcategoryId) {
        when {
            subcategoryId != null -> viewModel.loadServicesBySubCategory(subcategoryId)
            categoryId != null -> viewModel.loadServices(categoryId, categoryName)
        }
    }

    // Load service detail when a service is selected
    LaunchedEffect(selectedServiceId) {
        selectedServiceId?.let { serviceId ->
            viewModel.loadServiceDetail(serviceId)
        }
    }

    // Show service detail modal if service detail is loaded
    if (detailState is com.dreamdiver.rotterdam.ui.viewmodel.ServiceDetailState.Success) {
        ServiceDetailModal(
            serviceDetail = (detailState as com.dreamdiver.rotterdam.ui.viewmodel.ServiceDetailState.Success).serviceDetail,
            isEnglish = isEnglish,
            onDismiss = {
                selectedServiceId = null
                viewModel.clearServiceDetail()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isEnglish) "Back" else "Terug"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is ServiceUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ServiceUiState.Success -> {
                if (state.services.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEnglish) "No services available" else "Geen diensten beschikbaar",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.services) { service ->
                            ServiceCard(
                                service = service,
                                onClick = { selectedServiceId = service.id }
                            )
                        }
                    }
                }
            }
            is ServiceUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            when {
                                subcategoryId != null -> viewModel.loadServicesBySubCategory(subcategoryId)
                                categoryId != null -> viewModel.loadServices(categoryId, categoryName)
                            }
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(
    service: Service,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Service Image
            AsyncImage(
                model = service.image?.takeIf { it.isNotEmpty() } ?: service.category?.icon,
                contentDescription = service.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Service Name and Contact
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Service Name
                Text(
                    text = service.name ?: "Unnamed Service",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Contact (Phone Number)
                if (service.phone.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Phone",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = service.phone,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailModal(
    serviceDetail: com.dreamdiver.rotterdam.data.model.ServiceDetail,
    isEnglish: Boolean = true,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Helper function to get localized text
    fun getLocalizedText(en: String?, nl: String?, fallback: String?): String {
        return if (isEnglish) {
            en?.takeIf { it.isNotBlank() } ?: fallback?.takeIf { it.isNotBlank() } ?: ""
        } else {
            nl?.takeIf { it.isNotBlank() } ?: fallback?.takeIf { it.isNotBlank() } ?: ""
        }
    }

    val serviceName = getLocalizedText(serviceDetail.nameEn, serviceDetail.nameNl, serviceDetail.name)
    val serviceAddress = getLocalizedText(serviceDetail.addressEn, serviceDetail.addressNl, serviceDetail.address)
    val serviceDescription = getLocalizedText(serviceDetail.descriptionEn, serviceDetail.descriptionNl, serviceDetail.description)

    // Prepare tabs from descriptions array
    val tabs = if (serviceDetail.descriptions.isNotEmpty()) {
        serviceDetail.descriptions.sortedBy { it.order }
    } else {
        // Default tabs if no descriptions array
        listOf(
            com.dreamdiver.rotterdam.data.model.ServiceDescription(
                id = 1,
                tabTitleEn = "Overview",
                tabTitleNl = "Overzicht",
                contentEn = serviceDescription,
                contentNl = serviceDescription,
                order = 0
            ),
            com.dreamdiver.rotterdam.data.model.ServiceDescription(
                id = 2,
                tabTitleEn = "Contact",
                tabTitleNl = "Contact",
                contentEn = "Phone: ${serviceDetail.phone}\n\nAddress: $serviceAddress",
                contentNl = "Telefoon: ${serviceDetail.phone}\n\nAdres: $serviceAddress",
                order = 1
            )
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp) // Space for fixed buttons
            ) {
                // Header with back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }

                // Scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

            // Hero Card with Service Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E5A4C)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Background image
                    val imageUrl = serviceDetail.image?.takeIf { it.isNotBlank() }
                        ?: serviceDetail.category?.icon

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = serviceName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_launcher_background)
                    )

                    // Dark overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )

                    // Service name centered
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = serviceName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Service Name and Category
            Text(
                text = serviceName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category with icon (only show if category exists)
            serviceDetail.category?.let { category ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (category.icon.isNotBlank()) {
                        AsyncImage(
                            model = category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            error = painterResource(id = R.drawable.ic_launcher_background)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = category.name,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tabs
            if (tabs.isNotEmpty()) {
                var selectedTabIndex by remember { mutableStateOf(0) }

                // Tab Row
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.White,
                    contentColor = Color(0xFF0083CA),
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = Color(0xFF0083CA)
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val tabTitle = getLocalizedText(tab.tabTitleEn, tab.tabTitleNl, tab.tabTitle)
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = tabTitle,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                Divider(color = Color(0xFFEEEEEE))

                // Tab Content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                    ) {
                        val currentTab = tabs[selectedTabIndex]
                        val tabContent = getLocalizedText(currentTab.contentEn, currentTab.contentNl, currentTab.content)

                        // Description Section
                        if (tabContent.isNotBlank()) {
                            Text(
                                text = if (isEnglish) "Description" else "Beschrijving",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1E1E)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = tabContent,
                                fontSize = 15.sp,
                                color = Color(0xFF1E1E1E),
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Address Section
                        if (serviceAddress.isNotBlank()) {
                            Text(
                                text = if (isEnglish) "Address" else "Adres",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1E1E)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = serviceAddress,
                                fontSize = 15.sp,
                                color = Color(0xFF1E1E1E)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Phone Section
                        if (serviceDetail.phone.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isEnglish) "Phone: " else "Telefoon: ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1E1E)
                                )
                                Text(
                                    text = serviceDetail.phone,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1E1E1E)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Website Section
                        if (!serviceDetail.url.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isEnglish) "Website: " else "Website: ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1E1E)
                                )
                                Text(
                                    text = serviceDetail.url,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0083CA),
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(serviceDetail.url))
                                        context.startActivity(intent)
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
        }

        // Fixed Action Buttons at Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top shadow divider
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp
                )

                // Action Buttons Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Call Button
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${serviceDetail.phone}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF0083CA),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            disabledContentColor = Color(0xFF999999)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                        enabled = serviceDetail.phone.isNotBlank(),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isEnglish) "Call" else "Bellen",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Visit Website Button
                    Button(
                        onClick = {
                            val url = serviceDetail.url ?: ""
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF0083CA),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            disabledContentColor = Color(0xFF999999)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                        enabled = !serviceDetail.url.isNullOrBlank(),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isEnglish) "Website" else "Website",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }

                    // Open Map Button
                    Button(
                        onClick = {
                            val hasLocation = (!serviceDetail.latitude.isNullOrBlank() && !serviceDetail.longitude.isNullOrBlank())
                                    || !serviceDetail.googleMapsUrl.isNullOrBlank()
                                    || serviceAddress.isNotBlank()

                            if (hasLocation) {
                                val mapUri = when {
                                    !serviceDetail.latitude.isNullOrBlank() && !serviceDetail.longitude.isNullOrBlank() -> {
                                        "geo:${serviceDetail.latitude},${serviceDetail.longitude}?q=${serviceDetail.latitude},${serviceDetail.longitude}($serviceName)"
                                    }
                                    !serviceDetail.googleMapsUrl.isNullOrBlank() -> {
                                        serviceDetail.googleMapsUrl
                                    }
                                    else -> {
                                        val query = Uri.encode(serviceAddress)
                                        "https://www.google.com/maps/search/?api=1&query=$query"
                                    }
                                }
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUri))
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF0083CA),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            disabledContentColor = Color(0xFF999999)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                        enabled = (!serviceDetail.latitude.isNullOrBlank() && !serviceDetail.longitude.isNullOrBlank())
                                || !serviceDetail.googleMapsUrl.isNullOrBlank()
                                || serviceAddress.isNotBlank(),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isEnglish) "Map" else "Kaart",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

