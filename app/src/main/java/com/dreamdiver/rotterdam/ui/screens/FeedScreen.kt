package com.dreamdiver.rotterdam.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.dreamdiver.rotterdam.data.model.Feed
import com.dreamdiver.rotterdam.ui.viewmodel.FeedState
import com.dreamdiver.rotterdam.ui.viewmodel.FeedViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class FeedType(val value: String, val displayNameEn: String, val displayNameNl: String) {
    NEWS("news", "News", "Nieuws"),
    ALERTS("alerts", "Alerts", "Waarschuwingen"),
    BUDDY("buddy", "Buddy", "Maatje"),
    EVENTS("events", "Events", "Evenementen")
}

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    isEnglish: Boolean = true,
    viewModel: FeedViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedFeed by remember { mutableStateOf<Feed?>(null) }
    val feedTypes = FeedType.values()
    val feedState by viewModel.feedState.collectAsState()

    // Load feeds when tab changes
    LaunchedEffect(selectedTab) {
        viewModel.loadFeedsByType(feedTypes[selectedTab].value)
    }

    // Show detail modal when feed is selected
    selectedFeed?.let { feed ->
        FeedDetailModal(
            feed = feed,
            isEnglish = isEnglish,
            onDismiss = { selectedFeed = null }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Column {
                Text(
                    text = if (isEnglish) "Feed" else "Feed",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFF0083CA)
                ) {
                    feedTypes.forEachIndexed { index, feedType ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = if (isEnglish) feedType.displayNameEn else feedType.displayNameNl,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }

        // Feed Content
        when (val state = feedState) {
            is FeedState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF0083CA))
                }
            }
            is FeedState.Success -> {
                if (state.feeds.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = if (isEnglish) "No feeds available" else "Geen feeds beschikbaar",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.feeds) { feed ->
                            FeedCard(
                                feed = feed,
                                isEnglish = isEnglish,
                                onClick = { selectedFeed = feed }
                            )
                        }
                    }
                }
            }
            is FeedState.Error -> {
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
                            fontSize = 16.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadFeedsByType(feedTypes[selectedTab].value) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0083CA))
                        ) {
                            Text(if (isEnglish) "Retry" else "Opnieuw proberen")
                        }
                    }
                }
            }
            is FeedState.Idle -> {
                // Initial state, do nothing
            }
        }
    }
}

@Composable
fun FeedCard(
    feed: Feed,
    isEnglish: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val title = if (isEnglish) (feed.titleEn ?: feed.title) else (feed.titleNl ?: feed.title)
    val content = if (isEnglish) (feed.contentEn ?: feed.content) else (feed.contentNl ?: feed.content)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Feed Type Badge
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = when (feed.type) {
                    "news" -> Color(0xFF2196F3)
                    "alerts" -> Color(0xFFFF5722)
                    "buddy" -> Color(0xFF4CAF50)
                    "events" -> Color(0xFF9C27B0)
                    else -> Color(0xFF757575)
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = feed.type.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Image (if available)
            feed.image?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Title
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Published Date
            feed.publishedAt?.let { publishedAt ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = formatDate(publishedAt, isEnglish),
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
            }

            // URL Link (if available)
            feed.url?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = Color(0xFF0083CA),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isEnglish) "Read more" else "Lees meer",
                        fontSize = 14.sp,
                        color = Color(0xFF0083CA),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

fun formatDate(dateString: String, isEnglish: Boolean): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val outputFormat = if (isEnglish) {
            SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        } else {
            SimpleDateFormat("dd MMM yyyy", Locale("nl"))
        }
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedDetailModal(
    feed: Feed,
    isEnglish: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val title = if (isEnglish) (feed.titleEn ?: feed.title) else (feed.titleNl ?: feed.title)
    val content = if (isEnglish) (feed.contentEn ?: feed.content) else (feed.contentNl ?: feed.content)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            // Header with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Feed Type Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (feed.type) {
                        "news" -> Color(0xFF2196F3)
                        "alerts" -> Color(0xFFFF5722)
                        "buddy" -> Color(0xFF4CAF50)
                        "events" -> Color(0xFF9C27B0)
                        else -> Color(0xFF757575)
                    }
                ) {
                    Text(
                        text = feed.type.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
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
                    .padding(horizontal = 16.dp)
            ) {
                // Image (if available)
                feed.image?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Title
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E),
                    lineHeight = 32.sp
                )

                // Published Date
                feed.publishedAt?.let { publishedAt ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDate(publishedAt, isEnglish),
                        fontSize = 14.sp,
                        color = Color(0xFF999999)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(16.dp))

                // Full Content
                Text(
                    text = content,
                    fontSize = 16.sp,
                    color = Color(0xFF333333),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Bottom button (if URL available)
            feed.url?.let { url ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0083CA),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isEnglish) "Visit Website" else "Bezoek website",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

