package com.dreamdiver.rotterdam.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dreamdiver.rotterdam.data.model.SubCategory
import com.dreamdiver.rotterdam.ui.viewmodel.SubCategoryUiState
import com.dreamdiver.rotterdam.ui.viewmodel.SubCategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoryListScreen(
    categoryId: Int,
    categoryName: String,
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {},
    onSubCategoryClick: (Int, String) -> Unit = { _, _ -> },
    onNestedSubCategoryClick: (SubCategory) -> Unit = {},
    viewModel: SubCategoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.loadSubCategories(categoryId, categoryName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is SubCategoryUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SubCategoryUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.subCategories) { subCategory ->
                        SubCategoryCard(
                            subCategory = subCategory,
                            onClick = {
                                // Safe name for navigation
                                val safeName = subCategory.nameEn?.takeIf { it.isNotBlank() }
                                    ?: subCategory.name?.takeIf { it.isNotBlank() }
                                    ?: "Unnamed"

                                // If has children, navigate to nested subcategories
                                if (subCategory.children.isNotEmpty()) {
                                    onNestedSubCategoryClick(subCategory)
                                } else {
                                    // Otherwise, navigate to services
                                    onSubCategoryClick(subCategory.id, safeName)
                                }
                            }
                        )
                    }
                }
            }
            is SubCategoryUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
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
                        Button(onClick = { viewModel.loadSubCategories(categoryId, categoryName) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedSubCategoryListScreen(
    subCategory: SubCategory,
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {},
    onSubCategoryClick: (Int, String) -> Unit = { _, _ -> },
    onNestedSubCategoryClick: (SubCategory) -> Unit = {}
) {
    // Safe name extraction with fallbacks
    val displayName = subCategory.nameEn?.takeIf { it.isNotBlank() }
        ?: subCategory.name?.takeIf { it.isNotBlank() }
        ?: "Unnamed"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(displayName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (subCategory.children.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No subcategories available")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subCategory.children) { childSubCategory ->
                    SubCategoryCard(
                        subCategory = childSubCategory,
                        onClick = {
                            // Safe name for navigation
                            val childName = childSubCategory.nameEn?.takeIf { it.isNotBlank() }
                                ?: childSubCategory.name?.takeIf { it.isNotBlank() }
                                ?: "Unnamed"

                            // If has children, navigate to nested subcategories
                            if (childSubCategory.children.isNotEmpty()) {
                                onNestedSubCategoryClick(childSubCategory)
                            } else {
                                // Otherwise, navigate to services
                                onSubCategoryClick(childSubCategory.id, childName)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SubCategoryCard(
    subCategory: SubCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Safe name extraction with fallbacks
    val displayName = subCategory.nameEn?.takeIf { it.isNotBlank() }
        ?: subCategory.name?.takeIf { it.isNotBlank() }
        ?: "Unnamed"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Load icon from URL using Coil
            val iconUrl = subCategory.iconUrl ?: subCategory.icon?.let {
                "https://rotterdam.dreamdiver.nl/storage/$it"
            }

            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = displayName,
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Placeholder icon if no icon URL
                Box(
                    modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayName.take(2).uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

