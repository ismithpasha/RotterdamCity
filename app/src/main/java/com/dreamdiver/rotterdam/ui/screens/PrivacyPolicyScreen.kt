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
fun PrivacyPolicyScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Privacy Policy" else "গোপনীয়তা নীতি",
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
            Text(
                text = if (isEnglish) "Last Updated: November 2, 2025" else "সর্বশেষ আপডেট: নভেম্বর ২, ২০২৫",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PolicySection(
                title = if (isEnglish) "1. Information We Collect" else "১. আমরা যে তথ্য সংগ্রহ করি",
                content = if (isEnglish)
                    "Rotterdam City app collects the following types of information:\n\n" +
                            "• Device Information: We may collect device-specific information such as your device model, operating system version, and unique device identifiers.\n\n" +
                            "• Location Data: With your permission, we may collect your location data to provide location-based services such as finding nearby hospitals, diagnostic centers, or transport information.\n\n" +
                            "• Usage Data: We collect information about how you interact with the app, including the services you access and the time spent on different sections.\n\n" +
                            "• Contact Information: If you choose to share feedback or contact us, we may collect your email address or phone number."
                else
                    "রটারডাম সিটি অ্যাপ নিম্নলিখিত ধরনের তথ্য সংগ্রহ করে:\n\n" +
                            "• ডিভাইস তথ্য: আমরা আপনার ডিভাইস মডেল, অপারেটিং সিস্টেম সংস্করণ এবং অনন্য ডিভাইস শনাক্তকারীর মতো ডিভাইস-নির্দিষ্ট তথ্য সংগ্রহ করতে পারি।\n\n" +
                            "• অবস্থান ডেটা: আপনার অনুমতি নিয়ে, আমরা কাছাকাছি হাসপাতাল, ডায়াগনস্টিক সেন্টার বা পরিবহন তথ্য খুঁজে পেতে অবস্থান-ভিত্তিক সেবা প্রদানের জন্য আপনার অবস্থান ডেটা সংগ্রহ করতে পারি।\n\n" +
                            "• ব্যবহার ডেটা: আপনি অ্যাপের সাথে কীভাবে ইন্টারঅ্যাক্ট করেন, আপনি কোন সেবা অ্যাক্সেস করেন এবং বিভিন্ন বিভাগে কত সময় ব্যয় করেন সে সম্পর্কে তথ্য সংগ্রহ করি।\n\n" +
                            "• যোগাযোগ তথ্য: আপনি যদি প্রতিক্রিয়া শেয়ার করতে বা আমাদের সাথে যোগাযোগ করতে চান, আমরা আপনার ইমেইল ঠিকানা বা ফোন নম্বর সংগ্রহ করতে পারি।"
            )

            PolicySection(
                title = if (isEnglish) "2. How We Use Your Information" else "২. আমরা কীভাবে আপনার তথ্য ব্যবহার করি",
                content = if (isEnglish)
                    "We use the collected information to:\n\n" +
                            "• Provide and improve our services\n" +
                            "• Personalize your experience\n" +
                            "• Send you important notifications and updates\n" +
                            "• Respond to your inquiries and support requests\n" +
                            "• Analyze app usage to improve functionality\n" +
                            "• Ensure the security and integrity of our services"
                else
                    "আমরা সংগৃহীত তথ্য ব্যবহার করি:\n\n" +
                            "• আমাদের সেবা প্রদান এবং উন্নত করতে\n" +
                            "• আপনার অভিজ্ঞতা ব্যক্তিগতকৃত করতে\n" +
                            "• আপনাকে গুরুত্বপূর্ণ বিজ্ঞপ্তি এবং আপডেট পাঠাতে\n" +
                            "• আপনার অনুসন্ধান এবং সহায়তা অনুরোধের উত্তর দিতে\n" +
                            "• কার্যকারিতা উন্নত করতে অ্যাপ ব্যবহার বিশ্লেষণ করতে\n" +
                            "• আমাদের সেবার নিরাপত্তা এবং অখণ্ডতা নিশ্চিত করতে"
            )

            PolicySection(
                title = if (isEnglish) "3. Data Sharing and Disclosure" else "৩. ডেটা শেয়ারিং এবং প্রকাশ",
                content = if (isEnglish)
                    "We do not sell, trade, or rent your personal information to third parties. We may share your information only in the following circumstances:\n\n" +
                            "• With your explicit consent\n" +
                            "• To comply with legal obligations\n" +
                            "• To protect our rights and safety\n" +
                            "• With service providers who assist in app operations (under strict confidentiality agreements)"
                else
                    "আমরা তৃতীয় পক্ষের কাছে আপনার ব্যক্তিগত তথ্য বিক্রি, বাণিজ্য বা ভাড়া দিই না। আমরা শুধুমাত্র নিম্নলিখিত পরিস্থিতিতে আপনার তথ্য শেয়ার করতে পারি:\n\n" +
                            "• আপনার স্পষ্ট সম্মতি নিয়ে\n" +
                            "• আইনি বাধ্যবাধকতা মেনে চলতে\n" +
                            "• আমাদের অধিকার এবং নিরাপত্তা রক্ষা করতে\n" +
                            "• সেবা প্রদানকারীদের সাথে যারা অ্যাপ পরিচালনায় সহায়তা করে (কঠোর গোপনীয়তা চুক্তির অধীনে)"
            )

            PolicySection(
                title = if (isEnglish) "4. Data Security" else "৪. ডেটা নিরাপত্তা",
                content = if (isEnglish)
                    "We implement appropriate technical and organizational measures to protect your personal information against unauthorized access, alteration, disclosure, or destruction. However, no method of transmission over the internet is 100% secure."
                else
                    "আমরা অননুমোদিত প্রবেশ, পরিবর্তন, প্রকাশ বা ধ্বংসের বিরুদ্ধে আপনার ব্যক্তিগত তথ্য রক্ষা করতে উপযুক্ত প্রযুক্তিগত এবং সাংগঠনিক ব্যবস্থা বাস্তবায়ন করি। তবে, ইন্টারনেটের মাধ্যমে প্রেরণের কোনো পদ্ধতি ১০০% নিরাপদ নয়।"
            )

            PolicySection(
                title = if (isEnglish) "5. Your Rights" else "৫. আপনার অধিকার",
                content = if (isEnglish)
                    "You have the right to:\n\n" +
                            "• Access your personal information\n" +
                            "• Request correction of inaccurate data\n" +
                            "• Request deletion of your data\n" +
                            "• Opt-out of data collection (with limited app functionality)\n" +
                            "• Withdraw consent at any time"
                else
                    "আপনার অধিকার আছে:\n\n" +
                            "• আপনার ব্যক্তিগত তথ্য অ্যাক্সেস করার\n" +
                            "• ভুল ডেটা সংশোধনের অনুরোধ করার\n" +
                            "• আপনার ডেটা মুছে ফেলার অনুরোধ করার\n" +
                            "• ডেটা সংগ্রহ থেকে অপ্ট-আউট করার (সীমিত অ্যাপ কার্যকারিতা সহ)\n" +
                            "• যে কোনো সময় সম্মতি প্রত্যাহার করার"
            )

            PolicySection(
                title = if (isEnglish) "6. Contact Us" else "৬. আমাদের সাথে যোগাযোগ করুন",
                content = if (isEnglish)
                    "If you have any questions about this Privacy Policy, please contact us at:\n\nEmail: privacy@cumillacity.com\nPhone: +880-1XXX-XXXXXX"
                else
                    "এই গোপনীয়তা নীতি সম্পর্কে আপনার কোনো প্রশ্ন থাকলে, আমাদের সাথে যোগাযোগ করুন:\n\nইমেইল: privacy@cumillacity.com\nফোন: +৮৮০-১XXX-XXXXXX"
            )
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

