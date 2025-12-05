package com.dreamdiver.rotterdam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.model.Category
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.model.ServiceDetail
import com.dreamdiver.rotterdam.data.model.TrendingItem
import com.dreamdiver.rotterdam.ui.components.ImageSlider
import com.dreamdiver.rotterdam.ui.viewmodel.HomeUiState
import com.dreamdiver.rotterdam.ui.viewmodel.HomeViewModel
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isEnglish: Boolean = true,
    onNavigateToEmergency: () -> Unit = {},
    onNavigateToHospital: () -> Unit = {},
    onNavigateToEducational: () -> Unit = {},
    onNavigateToServiceList: (Int, String) -> Unit = { _, _ -> },
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var selectedTrendingItem by remember { mutableStateOf<TrendingItem?>(null) }
    var selectedSlider by remember { mutableStateOf<com.dreamdiver.rotterdam.data.model.Slider?>(null) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Scrollable content
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6C63FF))
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 0.dp, bottom = 0.dp)
                ) {
                    // Image Slider section
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            ImageSlider(
                                modifier = Modifier.fillMaxSize(),
                                isEnglish = isEnglish,
                                onSliderClick = { slider -> selectedSlider = slider }
                            )

//                            // Dark overlay
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .background(Color(0x88101846))
//                            )
                        }
                    }

                    // Frequently used section
                    item {
                        FeaturedServicesSection(
                            services = state.featuredServices,
                            isEnglish = isEnglish,
                            onServiceClick = { service -> selectedService = service }
                        )
                    }

                    // Trending section
                    item {
                        TrendingSection(
                            trendingItems = state.trendingItems,
                            isEnglish = isEnglish,
                            onTrendingClick = { item -> selectedTrendingItem = item }
                        )
                    }

                    // All Services section
                    item {
                        AllServicesSection(
                            categories = state.categories,
                            onNavigateToServiceList = onNavigateToServiceList,
                            isEnglish = isEnglish
                        )
                    }
                }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF2E3A59)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCategories() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }

        // Fixed Search Bar and Notification overlaid on top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search for anything", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color(0x14000000)
                    )
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Notification icon
            IconButton(
                onClick = { },
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    // Show service detail modal if a service is selected
    selectedService?.let { service ->
        ServiceDetailModal(
            serviceDetail = service.toServiceDetail(),
            onDismiss = { selectedService = null }
        )
    }

    // Show trending detail modal if a trending item is selected
    selectedTrendingItem?.let { item ->
        TrendingDetailModal(
            trendingItem = item,
            isEnglish = isEnglish,
            onDismiss = { selectedTrendingItem = null }
        )
    }

    // Show slider detail modal if a slider is selected
    selectedSlider?.let { slider ->
        SliderDetailModal(
            slider = slider,
            isEnglish = isEnglish,
            onDismiss = { selectedSlider = null }
        )
    }
}

// Extension function to convert Service to ServiceDetail
fun Service.toServiceDetail(): ServiceDetail {
    return ServiceDetail(
        id = this.id,
        categoryId = this.categoryId,
        category = this.category,
        name = this.name,
        nameEn = this.nameEn,
        phone = this.phone,
        address = this.address,
        addressEn = this.addressEn,
        latitude = this.latitude,
        longitude = this.longitude,
        googleMapsUrl = this.googleMapsUrl,
        description = this.description,
        descriptionEn = this.descriptionEn,
        image = this.image?.takeIf { it.isNotEmpty() } ?: "https://via.placeholder.com/400x200?text=No+Image",
        status = this.status,
        locale = this.locale,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

@Composable
fun FeaturedServicesSection(
    services: List<Service>,
    isEnglish: Boolean,
    onServiceClick: (Service) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x1A000000)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "— Frequently used —",
                fontSize = 12.sp,
                color = Color(0xFF7C8BA0),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services.take(4)) { service ->
                    FeaturedServiceCard(
                        service = service,
                        isEnglish = isEnglish,
                        onClick = { onServiceClick(service) }
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedServiceCard(
    service: Service,
    isEnglish: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(85.dp)
            .height(95.dp)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color(0x1A6C63FF)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFBFD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0EFFF)), // Light purple background
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = service.category.icon,
                    contentDescription = service.name,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isEnglish) (service.nameEn ?: service.name) else service.name,
                fontSize = 11.sp,
                color = Color(0xFF2E3A59),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TrendingSection(
    trendingItems: List<TrendingItem>,
    isEnglish: Boolean,
    onTrendingClick: (TrendingItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Trending on",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trendingItems) { item ->
                TrendingCard(
                    item = item,
                    isEnglish = isEnglish,
                    onClick = { onTrendingClick(item) }
                )
            }
        }
    }
}

@Composable
fun TrendingCard(
    item: TrendingItem,
    isEnglish: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4DD0E1))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.image,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isEnglish) (item.titleEn ?: item.title) else item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.createdAt.take(10),
                    fontSize = 12.sp,
                    color = Color.White
                )
            }

            // Twitter icon
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingDetailModal(
    trendingItem: TrendingItem,
    isEnglish: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trending Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }

            // Trending Image
            AsyncImage(
                model = trendingItem.image,
                contentDescription = if (isEnglish) (trendingItem.titleEn ?: trendingItem.title) else trendingItem.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = if (isEnglish) (trendingItem.titleEn ?: trendingItem.title) else trendingItem.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = "Published: ${trendingItem.createdAt.take(10)}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Section
            Text(
                text = "Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isEnglish) (trendingItem.summaryEn ?: trendingItem.summary) else trendingItem.summary,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Details Section
            Text(
                text = "Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isEnglish) (trendingItem.detailsEn ?: trendingItem.details) else trendingItem.details,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Open URL Button (if URL exists)
            trendingItem.url?.let { url ->
                if (url.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = "Open in Browser",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Read Full Article",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "Open in browser",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderDetailModal(
    slider: com.dreamdiver.rotterdam.data.model.Slider,
    isEnglish: Boolean,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Slider Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }

            // Slider Image
            AsyncImage(
                model = slider.image,
                contentDescription = if (isEnglish) (slider.titleEn ?: slider.title) else slider.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = if (isEnglish) (slider.titleEn ?: slider.title) else slider.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = "Created: ${slider.createdAt.take(10)}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Short Details Section
            Text(
                text = "Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isEnglish) (slider.shortDetailsEn ?: slider.shortDetails) else slider.shortDetails,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Full Details Section
            Text(
                text = "Full Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isEnglish) (slider.detailsEn ?: slider.details) else slider.details,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AllServicesSection(
    categories: List<Category>,
    onNavigateToServiceList: (Int, String) -> Unit,
    isEnglish: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "All Services",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(400.dp)
        ) {
            items(categories) { category ->
                ServiceCategoryItem(
                    category = category,
                    onClick = { onNavigateToServiceList(category.id, category.name) },
                    isEnglish = isEnglish
                )
            }
        }
    }
}

@Composable
fun ServiceCategoryItem(
    category: Category,
    onClick: () -> Unit,
    isEnglish: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE8F4FF)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isEnglish) (category.nameEn ?: category.name) else category.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

