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

data class EducationalInstitution(
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
fun EducationalScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = if (isEnglish) {
        listOf("School", "College", "Madrasa")
    } else {
        listOf("স্কুল", "কলেজ", "মাদ্রাসা")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Educational Institutions" else "শিক্ষা প্রতিষ্ঠান",
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
                // Get institutions based on selected tab
                val institutions = when (selectedTabIndex) {
                    0 -> getSchools()
                    1 -> getColleges()
                    else -> getMadrasas()
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
                            "📚 ${institutions.size} ${tabs[selectedTabIndex]}s in Cumilla"
                        else
                            "📚 কুমিল্লায় ${institutions.size}টি ${tabs[selectedTabIndex]}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Group institutions by type
                val groupedInstitutions = institutions.groupBy { it.type }

                groupedInstitutions.forEach { (type, institutionList) ->
                    // Type Header
                    Text(
                        text = if (isEnglish) type else institutionList.first().typeBn,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Institutions in this category
                    institutionList.forEach { institution ->
                        InstitutionCard(
                            institution = institution,
                            isEnglish = isEnglish,
                            onCallClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${institution.contact}")
                                }
                                context.startActivity(intent)
                            },
                            onEmailClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${institution.email}")
                                }
                                context.startActivity(intent)
                            },
                            onMapClick = {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("geo:${institution.latitude},${institution.longitude}?q=${institution.latitude},${institution.longitude}(${institution.name})")
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
private fun InstitutionCard(
    institution: EducationalInstitution,
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
            // Institution Name
            Text(
                text = if (isEnglish) institution.name else institution.nameBn,
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
                    text = if (isEnglish) institution.address else institution.addressBn,
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
                    text = institution.contact,
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
                    text = institution.email,
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
private fun getSchools(): List<EducationalInstitution> {
    return listOf(
        EducationalInstitution(
            id = 1,
            name = "Cumilla Zilla School",
            nameBn = "কুমিল্লা জিলা স্কুল",
            address = "Kandirpar, Cumilla Sadar",
            addressBn = "কান্দিরপাড়, কুমিল্লা সদর",
            contact = "081-66701",
            email = "cumillazillaschool@edu.bd",
            latitude = 23.4623,
            longitude = 91.1795,
            type = "Government School",
            typeBn = "সরকারি স্কুল"
        ),
        EducationalInstitution(
            id = 2,
            name = "Cumilla Cantonment Public School",
            nameBn = "কুমিল্লা ক্যান্টনমেন্ট পাবলিক স্কুল",
            address = "Cumilla Cantonment",
            addressBn = "কুমিল্লা সেনানিবাস",
            contact = "081-78901",
            email = "ccps@cantonment.edu.bd",
            latitude = 23.4512,
            longitude = 91.1923,
            type = "Cantonment School",
            typeBn = "সেনানিবাস স্কুল"
        ),
        EducationalInstitution(
            id = 3,
            name = "Cumilla Victoria Government High School",
            nameBn = "কুমিল্লা ভিক্টোরিয়া সরকারি উচ্চ বিদ্যালয়",
            address = "Victoria Road, Cumilla",
            addressBn = "ভিক্টোরিয়া রোড, কুমিল্লা",
            contact = "081-67234",
            email = "victoria@cumilla.edu.bd",
            latitude = 23.4598,
            longitude = 91.1812,
            type = "Government School",
            typeBn = "সরকারি স্কুল"
        ),
        EducationalInstitution(
            id = 4,
            name = "Cumilla Modern High School",
            nameBn = "কুমিল্লা মডার্ন হাই স্কুল",
            address = "Ranir Bazar, Cumilla",
            addressBn = "রানির বাজার, কুমিল্লা",
            contact = "081-68456",
            email = "modern@cumilla.edu.bd",
            latitude = 23.4618,
            longitude = 91.1801,
            type = "Private School",
            typeBn = "বেসরকারি স্কুল"
        ),
        EducationalInstitution(
            id = 5,
            name = "Cumilla Residential Model School",
            nameBn = "কুমিল্লা রেসিডেন্সিয়াল মডেল স্কুল",
            address = "Medical College Road, Cumilla",
            addressBn = "মেডিকেল কলেজ রোড, কুমিল্লা",
            contact = "081-69567",
            email = "residential@cumilla.edu.bd",
            latitude = 23.4607,
            longitude = 91.1809,
            type = "Private School",
            typeBn = "বেসরকারি স্কুল"
        )
    )
}

@Composable
private fun getColleges(): List<EducationalInstitution> {
    return listOf(
        EducationalInstitution(
            id = 6,
            name = "Cumilla Victoria Government College",
            nameBn = "কুমিল্লা ভিক্টোরিয়া সরকারি কলেজ",
            address = "Victoria Road, Cumilla",
            addressBn = "ভিক্টোরিয়া রোড, কুমিল্লা",
            contact = "081-66234",
            email = "vgc@cumilla.edu.bd",
            latitude = 23.4590,
            longitude = 91.1825,
            type = "Government College",
            typeBn = "সরকারি কলেজ"
        ),
        EducationalInstitution(
            id = 7,
            name = "Cumilla Govt. Women's College",
            nameBn = "কুমিল্লা সরকারি মহিলা কলেজ",
            address = "Tomsom Bridge Road, Cumilla",
            addressBn = "টমসম ব্রিজ রোড, কুমিল্লা",
            contact = "081-67345",
            email = "womens@cumilla.gov.bd",
            latitude = 23.4605,
            longitude = 91.1867,
            type = "Government College",
            typeBn = "সরকারি কলেজ"
        ),
        EducationalInstitution(
            id = 8,
            name = "Cumilla Commerce College",
            nameBn = "কুমিল্লা কমার্স কলেজ",
            address = "Kandirpar, Cumilla",
            addressBn = "কান্দিরপাড়, কুমিল্লা",
            contact = "081-68456",
            email = "commerce@cumilla.edu.bd",
            latitude = 23.4628,
            longitude = 91.1790,
            type = "Government College",
            typeBn = "সরকারি কলেজ"
        ),
        EducationalInstitution(
            id = 9,
            name = "Cumilla Degree College",
            nameBn = "কুমিল্লা ডিগ্রি কলেজ",
            address = "Jhawtala Road, Cumilla",
            addressBn = "ঝাউতলা রোড, কুমিল্লা",
            contact = "081-69567",
            email = "degree@cumilla.edu.bd",
            latitude = 23.4612,
            longitude = 91.1823,
            type = "Private College",
            typeBn = "বেসরকারি কলেজ"
        ),
        EducationalInstitution(
            id = 10,
            name = "Cumilla Cadet College",
            nameBn = "কুমিল্লা ক্যাডেট কলেজ",
            address = "Kotbari, Cumilla",
            addressBn = "কোটবাড়ি, কুমিল্লা",
            contact = "081-70123",
            email = "cadet@cumilla.edu.bd",
            latitude = 23.4456,
            longitude = 91.2012,
            type = "Cadet College",
            typeBn = "ক্যাডেট কলেজ"
        )
    )
}

@Composable
private fun getMadrasas(): List<EducationalInstitution> {
    return listOf(
        EducationalInstitution(
            id = 11,
            name = "Cumilla Islamia Dakhil Madrasa",
            nameBn = "কুমিল্লা ইসলামিয়া দাখিল মাদ্রাসা",
            address = "Chowmuhani, Cumilla",
            addressBn = "চৌমুহনী, কুমিল্লা",
            contact = "081-71234",
            email = "islamia@cumilla.edu.bd",
            latitude = 23.4645,
            longitude = 91.1756,
            type = "Dakhil Madrasa",
            typeBn = "দাখিল মাদ্রাসা"
        ),
        EducationalInstitution(
            id = 12,
            name = "Cumilla Government Aliya Madrasa",
            nameBn = "কুমিল্লা সরকারি আলিয়া মাদ্রাসা",
            address = "Kandirpar, Cumilla",
            addressBn = "কান্দিরপাড়, কুমিল্লা",
            contact = "081-72345",
            email = "aliya@cumilla.gov.bd",
            latitude = 23.4635,
            longitude = 91.1788,
            type = "Aliya Madrasa",
            typeBn = "আলিয়া মাদ্রাসা"
        ),
        EducationalInstitution(
            id = 13,
            name = "Cumilla Fazil Madrasa",
            nameBn = "কুমিল্লা ফাজিল মাদ্রাসা",
            address = "Ranir Bazar, Cumilla",
            addressBn = "রানির বাজার, কুমিল্লা",
            contact = "081-73456",
            email = "fazil@cumilla.edu.bd",
            latitude = 23.4620,
            longitude = 91.1805,
            type = "Fazil Madrasa",
            typeBn = "ফাজিল মাদ্রাসা"
        ),
        EducationalInstitution(
            id = 14,
            name = "Cumilla Kamil Madrasa",
            nameBn = "কুমিল্লা কামিল মাদ্রাসা",
            address = "Laksham Road, Cumilla",
            addressBn = "লাকসাম রোড, কুমিল্লা",
            contact = "081-74567",
            email = "kamil@cumilla.edu.bd",
            latitude = 23.4598,
            longitude = 91.1845,
            type = "Kamil Madrasa",
            typeBn = "কামিল মাদ্রাসা"
        ),
        EducationalInstitution(
            id = 15,
            name = "Cumilla Hafezi Madrasa",
            nameBn = "কুমিল্লা হাফেজি মাদ্রাসা",
            address = "Tomsom Bridge, Cumilla",
            addressBn = "টমসম ব্রিজ, কুমিল্লা",
            contact = "081-75678",
            email = "hafezi@cumilla.edu.bd",
            latitude = 23.4590,
            longitude = 91.1890,
            type = "Hafezi Madrasa",
            typeBn = "হাফেজি মাদ্রাসা"
        )
    )
}

