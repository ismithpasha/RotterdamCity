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
fun TermsConditionsScreen(
    isEnglish: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Terms & Conditions" else "শর্তাবলী",
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

            TermsSection(
                title = if (isEnglish) "1. Acceptance of Terms" else "১. শর্তাবলীর গ্রহণযোগ্যতা",
                content = if (isEnglish)
                    "By accessing and using the Rotterdam City app, you accept and agree to be bound by the terms and provisions of this agreement. If you do not agree to these Terms and Conditions, please do not use this app."
                else
                    "রটারডাম সিটি অ্যাপ অ্যাক্সেস এবং ব্যবহার করে, আপনি এই চুক্তির শর্তাবলী এবং বিধানগুলি মেনে চলতে সম্মত হন। আপনি যদি এই শর্তাবলীতে সম্মত না হন, অনুগ্রহ করে এই অ্যাপটি ব্যবহার করবেন না।"
            )

            TermsSection(
                title = if (isEnglish) "2. Use of Service" else "২. সেবা ব্যবহার",
                content = if (isEnglish)
                    "You agree to use the Rotterdam City app only for lawful purposes and in accordance with these Terms. You agree not to:\n\n" +
                            "• Use the app in any way that violates any applicable law or regulation\n" +
                            "• Engage in any conduct that restricts or inhibits anyone's use of the app\n" +
                            "• Use the app to transmit any harmful or malicious code\n" +
                            "• Attempt to gain unauthorized access to any portion of the app\n" +
                            "• Use automated systems to access the app without permission"
                else
                    "আপনি শুধুমাত্র আইনসম্মত উদ্দেশ্যে এবং এই শর্তাবলী অনুযায়ী রটারডাম সিটি অ্যাপ ব্যবহার করতে সম্মত হন। আপনি সম্মত হন না:\n\n" +
                            "• অ্যাপটি এমনভাবে ব্যবহার করবেন না যা কোনো প্রযোজ্য আইন বা নিয়ম লঙ্ঘন করে\n" +
                            "• এমন কোনো আচরণে জড়িত হবেন না যা কারো অ্যাপ ব্যবহারে বাধা দেয়\n" +
                            "• ক্ষতিকারক বা দূষিত কোড প্রেরণ করতে অ্যাপ ব্যবহার করবেন না\n" +
                            "• অ্যাপের কোনো অংশে অননুমোদিত প্রবেশাধিকার পাওয়ার চেষ্টা করবেন না\n" +
                            "• অনুমতি ছাড়া অ্যাপ অ্যাক্সেস করতে স্বয়ংক্রিয় সিস্টেম ব্যবহার করবেন না"
            )

            TermsSection(
                title = if (isEnglish) "3. Information Accuracy" else "৩. তথ্য নির্ভুলতা",
                content = if (isEnglish)
                    "We strive to provide accurate and up-to-date information through the Rotterdam City app. However, we do not guarantee the accuracy, completeness, or reliability of any information provided. Users should verify critical information independently before relying on it."
                else
                    "আমরা রটারডাম সিটি অ্যাপের মাধ্যমে সঠিক এবং আপ-টু-ডেট তথ্য প্রদান করার চেষ্টা করি। তবে, আমরা প্রদত্ত কোনো তথ্যের সঠিকতা, সম্পূর্ণতা বা নির্ভরযোগ্যতার গ্যারান্টি দিই না। ব্যবহারকারীদের এটির উপর নির্ভর করার আগে গুরুত্বপূর্ণ তথ্য স্বাধীনভাবে যাচাই করা উচিত।"
            )

            TermsSection(
                title = if (isEnglish) "4. Third-Party Services" else "৪. তৃতীয় পক্ষ সেবা",
                content = if (isEnglish)
                    "The app may contain links to third-party websites or services that are not owned or controlled by Rotterdam City. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party websites or services."
                else
                    "অ্যাপে তৃতীয় পক্ষের ওয়েবসাইট বা সেবার লিঙ্ক থাকতে পারে যা রটারডাম সিটির মালিকানা বা নিয়ন্ত্রণে নেই। আমাদের কোনো তৃতীয় পক্ষের ওয়েবসাইট বা সেবার বিষয়বস্তু, গোপনীয়তা নীতি বা অনুশীলনের উপর কোনো নিয়ন্ত্রণ নেই এবং কোনো দায়িত্ব গ্রহণ করি না।"
            )

            TermsSection(
                title = if (isEnglish) "5. Disclaimer of Warranties" else "৫. ওয়ারেন্টির দাবিত্যাগ",
                content = if (isEnglish)
                    "The Rotterdam City app is provided on an \"AS IS\" and \"AS AVAILABLE\" basis without any warranties of any kind. We do not warrant that:\n\n" +
                            "• The app will function without interruption or be error-free\n" +
                            "• Defects will be corrected\n" +
                            "• The app is free of viruses or harmful components\n" +
                            "• Information will be accurate, reliable, or correct"
                else
                    "রটারডাম সিটি অ্যাপ কোনো ধরনের ওয়ারেন্টি ছাড়াই \"যেমন আছে\" এবং \"যেমন উপলব্ধ\" ভিত্তিতে প্রদান করা হয়। আমরা ওয়ারেন্ট করি না যে:\n\n" +
                            "• অ্যাপটি বাধা ছাড়াই কাজ করবে বা ত্রুটি-মুক্ত হবে\n" +
                            "• ত্রুটিগুলি সংশোধন করা হবে\n" +
                            "• অ্যাপটি ভাইরাস বা ক্ষতিকারক উপাদান মুক্ত\n" +
                            "• তথ্য সঠিক, নির্ভরযোগ্য বা সঠিক হবে"
            )

            TermsSection(
                title = if (isEnglish) "6. Limitation of Liability" else "৬. দায় সীমাবদ্ধতা",
                content = if (isEnglish)
                    "To the maximum extent permitted by law, Rotterdam City shall not be liable for any indirect, incidental, special, consequential, or punitive damages resulting from your use or inability to use the app."
                else
                    "আইন দ্বারা অনুমোদিত সর্বোচ্চ সীমা পর্যন্ত, রটারডাম সিটি আপনার অ্যাপ ব্যবহার বা ব্যবহার করতে অক্ষমতার ফলে উদ্ভূত কোনো পরোক্ষ, আনুষঙ্গিক, বিশেষ, ফলস্বরূপ বা শাস্তিমূলক ক্ষতির জন্য দায়বদ্ধ হবে না।"
            )

            TermsSection(
                title = if (isEnglish) "7. Changes to Terms" else "৭. শর্তাবলী পরিবর্তন",
                content = if (isEnglish)
                    "We reserve the right to modify these Terms and Conditions at any time. Changes will be effective immediately upon posting. Your continued use of the app after changes constitutes acceptance of the modified terms."
                else
                    "আমরা যে কোনো সময় এই শর্তাবলী পরিবর্তন করার অধিকার সংরক্ষণ করি। পরিবর্তনগুলি পোস্ট করার সাথে সাথে কার্যকর হবে। পরিবর্তনের পরে আপনার অ্যাপের অব্যাহত ব্যবহার পরিবর্তিত শর্তাবলীর গ্রহণযোগ্যতা গঠন করে।"
            )

            TermsSection(
                title = if (isEnglish) "8. Contact Information" else "৮. যোগাযোগ তথ্য",
                content = if (isEnglish)
                    "For questions about these Terms and Conditions, please contact us at:\n\nEmail: legal@rotterdamcity.com\nPhone: +31-10-XXX-XXXX\nAddress: Rotterdam, Netherlands"
                else
                    "এই শর্তাবলী সম্পর্কে প্রশ্নের জন্য, আমাদের সাথে যোগাযোগ করুন:\n\nইমেইল: legal@rotterdamcity.com\nফোন: +৩১-১০-XXX-XXXX\nঠিকানা: রটারডাম, নেদারল্যান্ডস"
            )
        }
    }
}

@Composable
private fun TermsSection(
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

