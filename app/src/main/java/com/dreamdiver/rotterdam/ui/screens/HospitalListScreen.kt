package com.dreamdiver.rotterdam.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Hospital(
    val id: Int,
    val name: String,
    val nameBn: String,
    val address: String,
    val addressBn: String,
    val contact: String,
    val email: String,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val typeBn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalListScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = if (isEnglish) {
        listOf("Government", "Private & Specialized")
    } else {
        listOf("সরকারি", "বেসরকারি ও বিশেষায়িত")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Hospitals - Rotterdam" else "হাসপাতাল - রটারডাম",
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
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Content based on selected tab
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Get hospitals based on selected tab
                val hospitals = if (selectedTabIndex == 0) {
                    getGovernmentHospitals()
                } else {
                    getPrivateAndSpecializedHospitals()
                }

                // Header Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (isEnglish)
                            "🏥 ${hospitals.size} ${if (selectedTabIndex == 0) "Government" else "Private & Specialized"} Hospitals"
                        else
                            "🏥 ${hospitals.size}টি ${if (selectedTabIndex == 0) "সরকারি" else "বেসরকারি ও বিশেষায়িত"} হাসপাতাল",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Group hospitals by type
                val groupedHospitals = hospitals.groupBy { it.type }

                groupedHospitals.forEach { (type, hospitalList) ->
                    // Type Header
                    Text(
                        text = if (isEnglish) type else hospitalList.first().typeBn,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Hospitals in this category
                    hospitalList.forEach { hospital ->
                        HospitalCard(
                            hospital = hospital,
                            isEnglish = isEnglish,
                            onCallClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${hospital.contact}")
                                }
                                context.startActivity(intent)
                            },
                            onEmailClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${hospital.email}")
                                }
                                context.startActivity(intent)
                            },
                            onMapClick = {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("geo:${hospital.latitude},${hospital.longitude}?q=${hospital.latitude},${hospital.longitude}(${hospital.name})")
                                }
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Footer Note
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = if (isEnglish)
                            "ℹ️ Tap phone icon to call, email icon to send email, or location icon to view on map."
                        else
                            "ℹ️ ফোন আইকনে ট্যাপ করে কল করুন, ইমেইল আইকনে ইমেইল পাঠান, বা লোকেশন আইকনে ম্যাপে দেখুন।",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun HospitalCard(
    hospital: Hospital,
    isEnglish: Boolean,
    onCallClick: () -> Unit,
    onEmailClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Hospital Name
            Text(
                text = if (isEnglish) hospital.name else hospital.nameBn,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Address",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEnglish) hospital.address else hospital.addressBn,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contact Number
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Phone",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = hospital.contact,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = hospital.email,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Call Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onCallClick),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "Call" else "কল",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Email Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onEmailClick),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "Email" else "ইমেইল",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Map Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onMapClick),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Map",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "Map" else "ম্যাপ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getGovernmentHospitals(): List<Hospital> {
    return listOf(
        Hospital(
            id = 1,
            name = "Cumilla Medical College Hospital",
            nameBn = "কুমিল্লা মেডিকেল কলেজ হাসপাতাল",
            address = "Medical College Road, Cumilla Sadar, Cumilla",
            addressBn = "মেডিকেল কলেজ রোড, কুমিল্লা সদর, কুমিল্লা",
            contact = "081-76511",
            email = "cmch@cumilla.gov.bd",
            latitude = 23.4607,
            longitude = 91.1809,
            type = "Government Hospital",
            typeBn = "সরকারি হাসপাতাল"
        ),
        Hospital(
            id = 2,
            name = "Cumilla General Hospital",
            nameBn = "কুমিল্লা জেনারেল হাসপাতাল",
            address = "Dhaka-Chittagong Highway, Cumilla Sadar",
            addressBn = "ঢাকা-চট্টগ্রাম মহাসড়ক, কুমিল্লা সদর",
            contact = "081-65088",
            email = "cumilla.general@health.gov.bd",
            latitude = 23.4585,
            longitude = 91.1832,
            type = "Government Hospital",
            typeBn = "সরকারি হাসপাতাল"
        ),
        Hospital(
            id = 3,
            name = "Cumilla Sadar Hospital",
            nameBn = "কুমিল্লা সদর হাসপাতাল",
            address = "Kandirpar, Cumilla Sadar",
            addressBn = "কান্দিরপাড়, কুমিল্লা সদর",
            contact = "081-67890",
            email = "sadar.hospital@cumilla.gov.bd",
            latitude = 23.4623,
            longitude = 91.1795,
            type = "Government Hospital",
            typeBn = "সরকারি হাসপাতাল"
        )
    )
}

@Composable
private fun getPrivateAndSpecializedHospitals(): List<Hospital> {
    return listOf(
        // Private Hospitals
        Hospital(
            id = 4,
            name = "Cumilla Diabetic Hospital",
            nameBn = "কুমিল্লা ডায়াবেটিক হাসপাতাল",
            address = "Chowmuhani, Cumilla Sadar",
            addressBn = "চৌমুহনী, কুমিল্লা সদর",
            contact = "081-79101",
            email = "info@cumilladiabetic.org",
            latitude = 23.4645,
            longitude = 91.1756,
            type = "Private Hospital",
            typeBn = "বেসরকারি হাসপাতাল"
        ),
        Hospital(
            id = 5,
            name = "Medinova Hospital",
            nameBn = "মেডিনোভা হাসপাতাল",
            address = "Jhawtala Road, Cumilla",
            addressBn = "ঝাউতলা রোড, কুমিল্লা",
            contact = "081-62345",
            email = "info@medinovacumilla.com",
            latitude = 23.4612,
            longitude = 91.1823,
            type = "Private Hospital",
            typeBn = "বেসরকারি হাসপাতাল"
        ),
        Hospital(
            id = 6,
            name = "Popular Hospital Cumilla",
            nameBn = "পপুলার হাসপাতাল কুমিল্লা",
            address = "Laksham Road, Cumilla",
            addressBn = "লাকসাম রোড, কুমিল্লা",
            contact = "081-74567",
            email = "cumilla@popularhospital.com",
            latitude = 23.4598,
            longitude = 91.1845,
            type = "Private Hospital",
            typeBn = "বেসরকারি হাসপাতাল"
        ),
        Hospital(
            id = 7,
            name = "Ibn Sina Hospital Cumilla",
            nameBn = "ইবনে সিনা হাসপাতাল কুমিল্লা",
            address = "Kandirpar, Cumilla",
            addressBn = "কান্দিরপাড়, কুমিল্লা",
            contact = "081-78901",
            email = "cumilla@ibnsinahospital.com",
            latitude = 23.4630,
            longitude = 91.1778,
            type = "Private Hospital",
            typeBn = "বেসরকারি হাসপাতাল"
        ),
        Hospital(
            id = 8,
            name = "Delta Hospital & Diagnostic Center",
            nameBn = "ডেল্টা হাসপাতাল ও ডায়াগনস্টিক সেন্টার",
            address = "Tomsom Bridge Road, Cumilla",
            addressBn = "টমসম ব্রিজ রোড, কুমিল্লা",
            contact = "081-65432",
            email = "info@deltahospitalcumilla.com",
            latitude = 23.4605,
            longitude = 91.1867,
            type = "Private Hospital",
            typeBn = "বেসরকারি হাসপাতাল"
        ),

        // Specialized Hospitals
        Hospital(
            id = 9,
            name = "Cumilla Eye Hospital",
            nameBn = "কুমিল্লা চক্ষু হাসপাতাল",
            address = "Ranir Bazar, Cumilla",
            addressBn = "রানির বাজার, কুমিল্লা",
            contact = "081-63456",
            email = "info@cumillaeyehospital.org",
            latitude = 23.4618,
            longitude = 91.1801,
            type = "Specialized Hospital",
            typeBn = "বিশেষায়িত হাসপাতাল"
        ),
        Hospital(
            id = 10,
            name = "Cumilla Mother & Child Hospital",
            nameBn = "কুমিল্লা মাতৃ ও শিশু হাসপাতাল",
            address = "Kandirpar, Cumilla",
            addressBn = "কান্দিরপাড়, কুমিল্লা",
            contact = "081-67234",
            email = "info@cumillamch.org",
            latitude = 23.4635,
            longitude = 91.1788,
            type = "Specialized Hospital",
            typeBn = "বিশেষায়িত হাসপাতাল"
        ),
        Hospital(
            id = 11,
            name = "Cumilla Chest Disease Hospital",
            nameBn = "কুমিল্লা বক্ষব্যাধি হাসপাতাল",
            address = "Tomsom Bridge, Cumilla",
            addressBn = "টমসম ব্রিজ, কুমিল্লা",
            contact = "081-69876",
            email = "chest.hospital@cumilla.gov.bd",
            latitude = 23.4590,
            longitude = 91.1890,
            type = "Specialized Hospital",
            typeBn = "বিশেষায়িত হাসপাতাল"
        ),
        Hospital(
            id = 12,
            name = "Cumilla Dental College & Hospital",
            nameBn = "কুমিল্লা ডেন্টাল কলেজ ও হাসপাতাল",
            address = "Medical College Road, Cumilla",
            addressBn = "মেডিকেল কলেজ রোড, কুমিল্লা",
            contact = "081-72345",
            email = "info@cumilladentalcollege.edu.bd",
            latitude = 23.4615,
            longitude = 91.1812,
            type = "Specialized Hospital",
            typeBn = "বিশেষায়িত হাসপাতাল"
        )
    )
}

