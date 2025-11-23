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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class EmergencyContact(
    val id: Int,
    val name: String,
    val nameBn: String,
    val number: String,
    val category: String,
    val categoryBn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyServiceScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = if (isEnglish) {
        listOf("Rotterdam City", "General Helplines")
    } else {
        listOf("রটারডাম সিটি", "সাধারণ হেল্পলাইন")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Emergency Services" else "জরুরি সেবা",
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
                    containerColor = MaterialTheme.colorScheme.error,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
                contentColor = MaterialTheme.colorScheme.error
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
                // Warning Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = if (isEnglish)
                            "⚠️ Emergency Hotlines - ${if (selectedTabIndex == 0) "Cumilla" else "Bangladesh"}"
                        else
                            "⚠️ জরুরি হটলাইন - ${if (selectedTabIndex == 0) "কুমিল্লা" else "বাংলাদেশ"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Get contacts based on selected tab
                val emergencyContacts = if (selectedTabIndex == 0) {
                    getCumillaCityContacts()
                } else {
                    getGeneralHelplineContacts()
                }

                // Group contacts by category
                val groupedContacts = emergencyContacts.groupBy { it.category }

                groupedContacts.forEach { (category, contacts) ->
                    // Category Header
                    Text(
                        text = if (isEnglish) category else contacts.first().categoryBn,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Contacts in this category
                    contacts.forEach { contact ->
                        EmergencyContactCard(
                            contact = contact,
                            isEnglish = isEnglish,
                            onCallClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${contact.number}")
                                }
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Footer Note
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Text(
                        text = if (isEnglish)
                            "ℹ️ Tap on any contact to call directly. Keep these numbers handy for emergencies."
                        else
                            "ℹ️ কল করতে যেকোনো যোগাযোগে ট্যাপ করুন। জরুরি পরিস্থিতির জন্য এই নম্বরগুলি হাতের কাছে রাখুন।",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmergencyContactCard(
    contact: EmergencyContact,
    isEnglish: Boolean,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCallClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Contact Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEnglish) contact.name else contact.nameBn,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = contact.number,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Call Button Icon
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun getCumillaCityContacts(): List<EmergencyContact> {
    return listOf(
        // Cumilla Police
        EmergencyContact(1, "Cumilla Kotwali Police", "কুমিল্লা কোতোয়ালী থানা", "081-76624", "Police - Cumilla", "পুলিশ - কুমিল্লা"),
        EmergencyContact(2, "Cumilla Sadar Dakkhin Police", "কুমিল্লা সদর দক্ষিণ থানা", "081-76204", "Police - Cumilla", "পুলিশ - কুমিল্লা"),
        EmergencyContact(3, "Cumilla Model Police", "কুমিল্লা মডেল থানা", "081-79841", "Police - Cumilla", "পুলিশ - কুমিল্লা"),
        EmergencyContact(4, "Cumilla Traffic Police", "কুমিল্লা ট্রাফিক পুলিশ", "081-61090", "Police - Cumilla", "পুলিশ - কুমিল্লা"),

        // Fire Service
        EmergencyContact(5, "Cumilla Fire Service", "কুমিল্লা ফায়ার সার্ভিস", "081-76332", "Fire Service", "ফায়ার সার্ভিস"),

        // Medical Emergency - Cumilla
        EmergencyContact(6, "Cumilla Medical College", "কুমিল্লা মেডিকেল কলেজ", "081-76511", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(7, "CMC Emergency", "সিএমসি জরুরি", "081-76512", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(8, "Cumilla General Hospital", "কুমিল্লা জেনারেল হাসপাতাল", "081-65088", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(9, "Cumilla Diabetic Hospital", "কুমিল্লা ডায়াবেটিক হাসপাতাল", "081-79101", "Medical Emergency", "চিকিৎসা জরুরি"),

        // Ambulance Services - Cumilla
        EmergencyContact(10, "Cumilla Ambulance Service", "কুমিল্লা অ্যাম্বুলেন্স", "081-61010", "Ambulance", "অ্যাম্বুলেন্স"),
        EmergencyContact(11, "Red Crescent Cumilla", "রেড ক্রিসেন্ট কুমিল্লা", "081-76888", "Ambulance", "অ্যাম্বুলেন্স"),

        // Utilities - Cumilla
        EmergencyContact(12, "Cumilla DESA", "কুমিল্লা ডেসা", "081-76441", "Utilities", "ইউটিলিটিস"),
        EmergencyContact(13, "Cumilla Gas Office", "কুমিল্লা গ্যাস অফিস", "081-65544", "Utilities", "ইউটিলিটিস"),
        EmergencyContact(14, "Cumilla WASA", "কুমিল্লা ওয়াসা", "081-76773", "Utilities", "ইউটিলিটিস"),

        // Other Services - Cumilla
        EmergencyContact(15, "Cumilla Civil Surgeon", "কুমিল্লা সিভিল সার্জন", "081-76204", "Health Services", "স্বাস্থ্য সেবা"),
        EmergencyContact(16, "Cumilla DC Office", "কুমিল্লা ডিসি অফিস", "081-76091", "Administration", "প্রশাসন")
    )
}

@Composable
private fun getGeneralHelplineContacts(): List<EmergencyContact> {
    return listOf(
        // National Emergency Services
        EmergencyContact(1, "National Emergency", "জাতীয় জরুরি সেবা", "999", "National Emergency", "জাতীয় জরুরি সেবা"),

        // Police Services
        EmergencyContact(2, "Police Emergency", "পুলিশ জরুরি", "999", "Police", "পুলিশ"),
        EmergencyContact(3, "Police Control Room", "পুলিশ কন্ট্রোল রুম", "01320-014052", "Police", "পুলিশ"),
        EmergencyContact(4, "Traffic Police Dhaka", "ট্রাফিক পুলিশ ঢাকা", "9559998", "Police", "পুলিশ"),
        EmergencyContact(5, "RAB Control Room", "র‍্যাব কন্ট্রোল রুম", "01777-778899", "Police", "পুলিশ"),

        // Fire Service
        EmergencyContact(6, "Fire Service", "ফায়ার সার্ভিস", "102", "Fire Service", "ফায়ার সার্ভিস"),
        EmergencyContact(7, "Fire Service Dhaka", "ফায়ার সার্ভিস ঢাকা", "9555555", "Fire Service", "ফায়ার সার্ভিস"),

        // Medical Emergency
        EmergencyContact(8, "Ambulance Service", "অ্যাম্বুলেন্স সেবা", "199", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(9, "Dhaka Medical College", "ঢাকা মেডিকেল কলেজ", "01844-243203", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(10, "DMCH Emergency", "ডিএমসিএইচ জরুরি", "9668690-5", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(11, "National Heart Foundation", "জাতীয় হৃদরোগ ইনস্টিটিউট", "9898787", "Medical Emergency", "চিকিৎসা জরুরি"),
        EmergencyContact(12, "Poison Control Center", "বিষক্রিয়া নিয়ন্ত্রণ কেন্দ্র", "01777-778800", "Medical Emergency", "চিকিৎসা জরুরি"),

        // Women & Child Safety
        EmergencyContact(13, "Women & Children Helpline", "নারী ও শিশু হেল্পলাইন", "109", "Women & Child", "নারী ও শিশু"),
        EmergencyContact(14, "National Women's Helpline", "জাতীয় মহিলা হেল্পলাইন", "10921", "Women & Child", "নারী ও শিশু"),
        EmergencyContact(15, "Child Helpline", "শিশু হেল্পলাইন", "1098", "Women & Child", "নারী ও শিশু"),

        // Other Emergency Services
        EmergencyContact(16, "Earthquake Helpline", "ভূমিকম্প হেল্পলাইন", "02-8870505", "Disaster Management", "দুর্যোগ ব্যবস্থাপনা"),
        EmergencyContact(17, "Coast Guard", "কোস্ট গার্ড", "01769-669300", "Coast Guard", "কোস্ট গার্ড"),
        EmergencyContact(18, "Border Guard Bangladesh", "বর্ডার গার্ড বাংলাদেশ", "01769-664411", "Border Security", "সীমান্ত নিরাপত্তা"),

        // Utilities Emergency
        EmergencyContact(19, "Gas Emergency (Dhaka)", "গ্যাস জরুরি (ঢাকা)", "16496", "Utilities", "ইউটিলিটিস"),
        EmergencyContact(20, "DESA Emergency", "ডেসা জরুরি", "9559955", "Utilities", "ইউটিলিটিস"),
        EmergencyContact(21, "WASA Emergency", "ওয়াসা জরুরি", "9896622", "Utilities", "ইউটিলিটিস"),

        // Anti-Corruption & Legal Aid
        EmergencyContact(22, "Anti-Corruption Hotline", "দুর্নীতি বিরোধী হটলাইন", "106", "Anti-Corruption", "দুর্নীতি বিরোধী"),
        EmergencyContact(23, "Legal Aid Helpline", "আইনি সহায়তা হেল্পলাইন", "16430", "Legal Aid", "আইনি সহায়তা"),

        // Health Services
        EmergencyContact(24, "Health Helpline", "স্বাস্থ্য হেল্পলাইন", "16263", "Health Services", "স্বাস্থ্য সেবা"),
        EmergencyContact(25, "Shasthyo Batayon", "স্বাস্থ্য বাতায়ন", "16263", "Health Services", "স্বাস্থ্য সেবা"),

        // Transport
        EmergencyContact(26, "Railway Helpline", "রেলওয়ে হেল্পলাইন", "01709-986801", "Transport", "পরিবহন"),
        EmergencyContact(27, "Airport Emergency", "বিমানবন্দর জরুরি", "01844-488840", "Transport", "পরিবহন")
    )
}

