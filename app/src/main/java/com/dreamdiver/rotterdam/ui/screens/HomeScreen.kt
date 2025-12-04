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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.model.Category
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.model.TrendingItem
import com.dreamdiver.rotterdam.ui.components.ImageSlider
import com.dreamdiver.rotterdam.ui.viewmodel.HomeUiState
import com.dreamdiver.rotterdam.ui.viewmodel.HomeViewModel

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

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header Section with Image Slider Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            // Image Slider as background
            ImageSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                isEnglish = isEnglish
            )

            // Dark overlay (50% transparency - more see-through)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x801A1A2E)) // 50% transparent dark overlay
            )

            // Header content on top
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top bar with search and notification
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Search bar (smaller, on left)
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
                            .clip(RoundedCornerShape(8.dp)),
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

                    // Notification icon on right
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Dynamic content based on state
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    // Frequently used section
                    item {
                        FeaturedServicesSection(
                            services = state.featuredServices,
                            isEnglish = isEnglish
                        )
                    }

                    // Trending section
                    item {
                        TrendingSection(
                            trendingItems = state.trendingItems,
                            isEnglish = isEnglish
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
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCategories() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedServicesSection(
    services: List<Service>,
    isEnglish: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "— Frequently used —",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(services.take(4)) { service ->
                FeaturedServiceCard(service = service, isEnglish = isEnglish)
            }
        }
    }
}

@Composable
fun FeaturedServiceCard(service: Service, isEnglish: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8F4FF)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = service.category.icon,
                contentDescription = service.name,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isEnglish) (service.nameEn ?: service.name) else service.name,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun TrendingSection(
    trendingItems: List<TrendingItem>,
    isEnglish: Boolean
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
                TrendingCard(item = item, isEnglish = isEnglish)
            }
        }
    }
}

@Composable
fun TrendingCard(item: TrendingItem, isEnglish: Boolean) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(120.dp)
            .clickable { },
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
            Text(
                text = "More",
                fontSize = 14.sp,
                color = Color(0xFF4DD0E1),
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8F4FF)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isEnglish) (category.nameEn ?: category.name) else category.name,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 13.sp
        )
    }
}

