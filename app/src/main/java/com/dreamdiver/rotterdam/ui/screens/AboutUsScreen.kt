package com.dreamdiver.rotterdam.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "About Us" else "আমাদের সম্পর্কে",
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
            // App Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isEnglish) "Rotterdam City" else "রটারডাম সিটি",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isEnglish)
                            "Your one-stop solution for all city services"
                        else
                            "আপনার শহরের সকল সেবার জন্য এক-স্টপ সমাধান",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isEnglish) "About" else "সম্পর্কে",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isEnglish)
                            "Rotterdam City app is designed to provide easy access to all essential services in Rotterdam. From emergency services like hospitals, doctors, and ambulances to daily needs like job services, diagnostics, and educational institutions - everything is now at your fingertips.\n\n" +
                                    "Our mission is to digitize city services and make life easier for every citizen of Rotterdam. Whether you need to find a blood donor urgently, check bus timings, or explore tourist spots, our app has got you covered.\n\n" +
                                    "We continuously update our database to ensure you get the most accurate and up-to-date information."
                        else
                            "রটারডাম সিটি অ্যাপটি রটারডামের সকল প্রয়োজনীয় সেবায় সহজ প্রবেশাধিকার প্রদানের জন্য ডিজাইন করা হয়েছে। হাসপাতাল, ডাক্তার এবং অ্যাম্বুলেন্সের মতো জরুরি সেবা থেকে শুরু করে চাকরির সেবা, ডায়াগনস্টিক এবং শিক্ষা প্রতিষ্ঠানের মতো দৈনন্দিন চাহিদা - সবকিছুই এখন আপনার হাতের মুঠোয়।\n\n" +
                                    "আমাদের লক্ষ্য হল শহরের সেবাসমূহ ডিজিটালাইজ করা এবং রটারডামের প্রতিটি নাগরিকের জীবনকে সহজ করা। আপনার জরুরিভাবে রক্তদাতা খুঁজে পেতে, বাসের সময় চেক করতে বা পর্যটন স্থান অন্বেষণ করতে হোক না কেন, আমাদের অ্যাপ আপনার জন্য রয়েছে।\n\n" +
                                    "আমরা ক্রমাগত আমাদের ডাটাবেস আপডেট করি যাতে আপনি সবচেয়ে সঠিক এবং আপ-টু-ডেট তথ্য পান।",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Features Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isEnglish) "Key Features" else "প্রধান বৈশিষ্ট্য",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val features = if (isEnglish) listOf(
                        "✓ Emergency Services (Hospital, Doctor, Ambulance)",
                        "✓ Blood Donor Database",
                        "✓ Job Service Information",
                        "✓ Educational Institutions",
                        "✓ Transport Schedule (Bus & Train)",
                        "✓ Tourist Spots Information",
                        "✓ Daily Offers & Deals",
                        "✓ Important Contact Numbers",
                        "✓ Bilingual Support (English & Bangla)",
                        "✓ Regular Updates"
                    ) else listOf(
                        "✓ জরুরি সেবা (হাসপাতাল, ডাক্তার, অ্যাম্বুলেন্স)",
                        "✓ রক্তদাতা ডাটাবেস",
                        "✓ চাকরি সেবা তথ্য",
                        "✓ শিক্ষা প্রতিষ্ঠান",
                        "✓ পরিবহন সময়সূচী (বাস এবং ট্রেন)",
                        "✓ পর্যটন স্থানের তথ্য",
                        "✓ দৈনিক অফার এবং ডিল",
                        "✓ গুরুত্বপূর্ণ যোগাযোগ নম্বর",
                        "✓ দ্বিভাষিক সমর্থন (ইংরেজি এবং বাংলা)",
                        "✓ নিয়মিত আপডেট"
                    )

                    features.forEach { feature ->
                        Text(
                            text = feature,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isEnglish) "Contact Us" else "যোগাযোগ করুন",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isEnglish)
                            "Email: info@cumillacity.com\nPhone: +880-1XXX-XXXXXX\nAddress: Cumilla, Bangladesh"
                        else
                            "ইমেইল: info@cumillacity.com\nফোন: +৮৮০-১XXX-XXXXXX\nঠিকানা: কুমিল্লা, বাংলাদেশ",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Version Info
            Text(
                text = if (isEnglish) "Version 1.0.0" else "সংস্করণ ১.০.০",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

