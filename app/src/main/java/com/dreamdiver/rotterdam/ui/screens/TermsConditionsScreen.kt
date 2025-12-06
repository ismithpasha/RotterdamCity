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
                        text = if (isEnglish) "Terms & Conditions" else "Voorwaarden",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isEnglish) "Back" else "Terug"
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
                text = if (isEnglish) "Last Updated: November 2, 2025" else "Laatst bijgewerkt: 2 november 2025",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TermsSection(
                title = if (isEnglish) "1. Acceptance of Terms" else "1. Acceptatie van Voorwaarden",
                content = if (isEnglish)
                    "By accessing and using the Rotterdam City app, you accept and agree to be bound by the terms and provisions of this agreement. If you do not agree to these Terms and Conditions, please do not use this app."
                else
                    "Door de Rotterdam City-app te openen en te gebruiken, accepteert en stemt u in met de voorwaarden en bepalingen van deze overeenkomst. Als u niet akkoord gaat met deze Voorwaarden, gebruik deze app dan niet."
            )

            TermsSection(
                title = if (isEnglish) "2. Use of Service" else "2. Gebruik van de Service",
                content = if (isEnglish)
                    "You agree to use the Rotterdam City app only for lawful purposes and in accordance with these Terms. You agree not to:\n\n" +
                            "• Use the app in any way that violates any applicable law or regulation\n" +
                            "• Engage in any conduct that restricts or inhibits anyone's use of the app\n" +
                            "• Use the app to transmit any harmful or malicious code\n" +
                            "• Attempt to gain unauthorized access to any portion of the app\n" +
                            "• Use automated systems to access the app without permission"
                else
                    "U stemt ermee in de Rotterdam City-app alleen te gebruiken voor wettige doeleinden en in overeenstemming met deze Voorwaarden. U stemt ermee in om niet:\n\n" +
                            "• De app te gebruiken op een manier die een toepasselijke wet of regelgeving schendt\n" +
                            "• Zich bezig te houden met gedrag dat het gebruik van de app door anderen beperkt of belemmert\n" +
                            "• De app te gebruiken om schadelijke of kwaadaardige code te verzenden\n" +
                            "• Te proberen ongeautoriseerde toegang te verkrijgen tot een deel van de app\n" +
                            "• Geautomatiseerde systemen te gebruiken om zonder toestemming toegang te krijgen tot de app"
            )

            TermsSection(
                title = if (isEnglish) "3. Information Accuracy" else "3. Nauwkeurigheid van Informatie",
                content = if (isEnglish)
                    "We strive to provide accurate and up-to-date information through the Rotterdam City app. However, we do not guarantee the accuracy, completeness, or reliability of any information provided. Users should verify critical information independently before relying on it."
                else
                    "We streven ernaar nauwkeurige en actuele informatie te verstrekken via de Rotterdam City-app. We garanderen echter niet de nauwkeurigheid, volledigheid of betrouwbaarheid van de verstrekte informatie. Gebruikers moeten kritieke informatie zelfstandig verifiëren voordat ze erop vertrouwen."
            )

            TermsSection(
                title = if (isEnglish) "4. Third-Party Services" else "4. Diensten van Derden",
                content = if (isEnglish)
                    "The app may contain links to third-party websites or services that are not owned or controlled by Rotterdam City. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party websites or services."
                else
                    "De app kan links bevatten naar websites of diensten van derden die niet eigendom zijn van of worden beheerd door Rotterdam City. We hebben geen controle over en nemen geen verantwoordelijkheid voor de inhoud, het privacybeleid of de praktijken van websites of diensten van derden."
            )

            TermsSection(
                title = if (isEnglish) "5. Disclaimer of Warranties" else "5. Disclaimer van Garanties",
                content = if (isEnglish)
                    "The Rotterdam City app is provided on an \"AS IS\" and \"AS AVAILABLE\" basis without any warranties of any kind. We do not warrant that:\n\n" +
                            "• The app will function without interruption or be error-free\n" +
                            "• Defects will be corrected\n" +
                            "• The app is free of viruses or harmful components\n" +
                            "• Information will be accurate, reliable, or correct"
                else
                    "De Rotterdam City-app wordt geleverd op \"AS IS\" en \"AS AVAILABLE\" basis zonder enige garanties van welke aard dan ook. We garanderen niet dat:\n\n" +
                            "• De app zonder onderbreking zal functioneren of foutloos zal zijn\n" +
                            "• Defecten zullen worden gecorrigeerd\n" +
                            "• De app vrij is van virussen of schadelijke componenten\n" +
                            "• Informatie nauwkeurig, betrouwbaar of correct zal zijn"
            )

            TermsSection(
                title = if (isEnglish) "6. Limitation of Liability" else "6. Beperking van Aansprakelijkheid",
                content = if (isEnglish)
                    "To the maximum extent permitted by law, Rotterdam City shall not be liable for any indirect, incidental, special, consequential, or punitive damages resulting from your use or inability to use the app."
                else
                    "Voor zover maximaal toegestaan door de wet, is Rotterdam City niet aansprakelijk voor indirecte, incidentele, speciale, gevolgschade of punitieve schade als gevolg van uw gebruik of onvermogen om de app te gebruiken."
            )

            TermsSection(
                title = if (isEnglish) "7. Changes to Terms" else "7. Wijzigingen in Voorwaarden",
                content = if (isEnglish)
                    "We reserve the right to modify these Terms and Conditions at any time. Changes will be effective immediately upon posting. Your continued use of the app after changes constitutes acceptance of the modified terms."
                else
                    "We behouden ons het recht voor om deze Voorwaarden op elk moment te wijzigen. Wijzigingen worden onmiddellijk van kracht na publicatie. Uw voortgezet gebruik van de app na wijzigingen houdt acceptatie van de gewijzigde voorwaarden in."
            )

            TermsSection(
                title = if (isEnglish) "8. Contact Information" else "8. Contactgegevens",
                content = if (isEnglish)
                    "For questions about these Terms and Conditions, please contact us at:\n\nEmail: legal@rotterdamcity.com\nPhone: +31-10-XXX-XXXX\nAddress: Rotterdam, Netherlands"
                else
                    "Voor vragen over deze Voorwaarden kunt u contact met ons opnemen via:\n\nE-mail: legal@rotterdamcity.com\nTelefoon: +31-10-XXX-XXXX\nAdres: Rotterdam, Nederland"
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

