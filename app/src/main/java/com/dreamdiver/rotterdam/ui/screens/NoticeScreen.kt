package com.dreamdiver.rotterdam.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Notice(
    val id: Int,
    val title: String,
    val titleBn: String,
    val description: String,
    val descriptionBn: String,
    val date: String,
    val dateBn: String,
    val isImportant: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val notices = getNotices()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Important Notices" else "গুরুত্বপূর্ণ নোটিশ",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isEnglish) "Back" else "পেছনে"
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (notices.isEmpty()) {
                // Empty state
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isEnglish) "No Notices Available" else "কোন নোটিশ নেই",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish)
                                "Check back later for important updates"
                            else
                                "গুরুত্বপূর্ণ আপডেটের জন্য পরে আবার দেখুন",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                notices.forEach { notice ->
                    NoticeCard(
                        notice = notice,
                        isEnglish = isEnglish
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun NoticeCard(
    notice: Notice,
    isEnglish: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notice.isImportant)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with date and important badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (notice.isImportant) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Important",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isEnglish) notice.date else notice.dateBn,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                )
                if (notice.isImportant) {
                    Text(
                        text = if (isEnglish) "IMPORTANT" else "গুরুত্বপূর্ণ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = if (isEnglish) notice.title else notice.titleBn,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = if (isEnglish) notice.description else notice.descriptionBn,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun getNotices(): List<Notice> {
    return listOf(
        Notice(
            id = 1,
            title = "App Maintenance Notice",
            titleBn = "অ্যাপ রক্ষণাবেক্ষণ নোটিশ",
            description = "The app will be under maintenance on November 5, 2025, from 2:00 AM to 4:00 AM. Some services may be temporarily unavailable during this time.",
            descriptionBn = "অ্যাপটি ৫ নভেম্বর, ২০২৫ তারিখে রাত ২:০০ থেকে ৪:০০ পর্যন্ত রক্ষণাবেক্ষণের অধীনে থাকবে। এই সময়ে কিছু সেবা সাময়িকভাবে অনুপলব্ধ থাকতে পারে।",
            date = "November 2, 2025",
            dateBn = "নভেম্বর ২, ২০২৫",
            isImportant = true
        ),
        Notice(
            id = 2,
            title = "New Features Added",
            titleBn = "নতুন বৈশিষ্ট্য যোগ করা হয়েছে",
            description = "We've added new features including bilingual support, image slider on home page, and improved navigation. Update the app to access these features.",
            descriptionBn = "আমরা দ্বিভাষিক সমর্থন, হোম পেজে ইমেজ স্লাইডার এবং উন্নত নেভিগেশন সহ নতুন বৈশিষ্ট্য যোগ করেছি। এই বৈশিষ্ট্যগুলি অ্যাক্সেস করতে অ্যাপ আপডেট করুন।",
            date = "November 1, 2025",
            dateBn = "নভেম্বর ১, ২০২৫",
            isImportant = false
        ),
        Notice(
            id = 3,
            title = "Emergency Numbers Update",
            titleBn = "জরুরি নম্বর আপডেট",
            description = "Emergency contact numbers have been updated for all hospitals and ambulance services. Please verify the numbers before use.",
            descriptionBn = "সকল হাসপাতাল এবং অ্যাম্বুলেন্স সেবার জন্য জরুরি যোগাযোগ নম্বর আপডেট করা হয়েছে। ব্যবহার করার আগে নম্বরগুলি যাচাই করুন।",
            date = "October 28, 2025",
            dateBn = "অক্টোবর ২৮, ২০২৫",
            isImportant = true
        ),
        Notice(
            id = 4,
            title = "Holiday Notice - Victory Day",
            titleBn = "ছুটির নোটিশ - বিজয় দিবস",
            description = "Most government offices will be closed on December 16, 2025, for Victory Day. Emergency services will remain operational.",
            descriptionBn = "বিজয় দিবসের জন্য ১৬ ডিসেম্বর, ২০২৫ তারিখে বেশিরভাগ সরকারি অফিস বন্ধ থাকবে। জরুরি সেবা চালু থাকবে।",
            date = "October 25, 2025",
            dateBn = "অক্টোবর ২৫, ২০২৫",
            isImportant = false
        ),
        Notice(
            id = 5,
            title = "Data Verification Request",
            titleBn = "ডেটা যাচাইকরণ অনুরোধ",
            description = "We are verifying all listed services. If you find any incorrect information, please report it through the app's feedback option.",
            descriptionBn = "আমরা সমস্ত তালিকাভুক্ত সেবা যাচাই করছি। আপনি যদি কোনো ভুল তথ্য খুঁজে পান, অ্যাপের ফিডব্যাক অপশনের মাধ্যমে রিপোর্ট করুন।",
            date = "October 20, 2025",
            dateBn = "অক্টোবর ২০, ২০২৫",
            isImportant = false
        )
    )
}

