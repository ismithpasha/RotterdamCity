package com.dreamdiver.rotterdam.util

object Strings {
    // Home Screen
    fun home(isEnglish: Boolean) = if (isEnglish) "Home" else "হোম"
    fun favorites(isEnglish: Boolean) = if (isEnglish) "Favorites" else "পছন্দসমূহ"
    fun profile(isEnglish: Boolean) = if (isEnglish) "Profile" else "প্রোফাইল"
    fun more(isEnglish: Boolean) = if (isEnglish) "More" else "আরও"

    // More Screen
    fun moreOptions(isEnglish: Boolean) = if (isEnglish) "More Options" else "আরও অপশন"
    fun languageSettings(isEnglish: Boolean) = if (isEnglish) "Language Settings" else "ভাষা সেটিংস"
    fun changeLanguage(isEnglish: Boolean) = if (isEnglish) "Change Language" else "ভাষা পরিবর্তন করুন"
    fun englishBangla(isEnglish: Boolean) = if (isEnglish) "English / বাংলা" else "ইংরেজি / বাংলা"

    fun information(isEnglish: Boolean) = if (isEnglish) "Information" else "তথ্য"
    fun notice(isEnglish: Boolean) = if (isEnglish) "Notice" else "নোটিশ"
    fun viewImportantNotices(isEnglish: Boolean) = if (isEnglish) "View important notices" else "গুরুত্বপূর্ণ নোটিশ দেখুন"
    fun aboutUs(isEnglish: Boolean) = if (isEnglish) "About Us" else "আমাদের সম্পর্কে"
    fun learnAbout(isEnglish: Boolean) = if (isEnglish) "Learn about Rotterdam City" else "রটারডাম সিটি সম্পর্কে জানুন"

    fun legal(isEnglish: Boolean) = if (isEnglish) "Legal" else "আইনি"
    fun privacyPolicy(isEnglish: Boolean) = if (isEnglish) "Privacy Policy" else "গোপনীয়তা নীতি"
    fun howWeHandleData(isEnglish: Boolean) = if (isEnglish) "How we handle your data" else "আমরা আপনার ডেটা কীভাবে পরিচালনা করি"
    fun termsConditions(isEnglish: Boolean) = if (isEnglish) "Terms & Conditions" else "শর্তাবলী"
    fun termsOfService(isEnglish: Boolean) = if (isEnglish) "Terms of service" else "সেবার শর্তাবলী"

    fun other(isEnglish: Boolean) = if (isEnglish) "Other" else "অন্যান্য"
    fun rateUs(isEnglish: Boolean) = if (isEnglish) "Rate Us" else "রেট করুন"
    fun rateOnPlayStore(isEnglish: Boolean) = if (isEnglish) "Rate on Play Store" else "প্লে স্টোরে রেট করুন"
    fun shareApp(isEnglish: Boolean) = if (isEnglish) "Share App" else "অ্যাপ শেয়ার করুন"
    fun shareWithFriends(isEnglish: Boolean) = if (isEnglish) "Share with friends" else "বন্ধুদের সাথে শেয়ার করুন"

    fun version(isEnglish: Boolean) = if (isEnglish) "Version 1.0.0" else "সংস্করণ ১.০.০"

    // Menu Items
    fun dailyOffer(isEnglish: Boolean) = if (isEnglish) "Daily Offer" else "ডেইলি অফার"
    fun jobService(isEnglish: Boolean) = if (isEnglish) "Job Service" else "চাকরী সেবা"
    fun diagnostic(isEnglish: Boolean) = if (isEnglish) "Diagnostic" else "ডায়গনস্টিক"
    fun hospital(isEnglish: Boolean) = if (isEnglish) "Hospital" else "হাসপাতাল"
    fun doctor(isEnglish: Boolean) = if (isEnglish) "Doctor" else "ডাক্তার"
    fun ambulance(isEnglish: Boolean) = if (isEnglish) "Ambulance" else "এ্যাম্বুলেন্স"
    fun bloodDonor(isEnglish: Boolean) = if (isEnglish) "Blood Donor" else "রক্ত দাতা"
    fun thanaPolice(isEnglish: Boolean) = if (isEnglish) "Thana Police" else "থানা পুলিশ"
    fun lawyer(isEnglish: Boolean) = if (isEnglish) "Lawyer" else "আইনজীবী"
    fun journalist(isEnglish: Boolean) = if (isEnglish) "Journalist" else "সাংবাদিক"
    fun onlineService(isEnglish: Boolean) = if (isEnglish) "Online Service" else "অনলাইন সেবা"
    fun fireService(isEnglish: Boolean) = if (isEnglish) "Fire Service" else "ফায়ার সার্ভিস"
    fun electricityOffice(isEnglish: Boolean) = if (isEnglish) "Electricity Office" else "বিদ্যুৎ অফিস"
    fun todayNews(isEnglish: Boolean) = if (isEnglish) "Today's News" else "আজকের খবর"
    fun touristSpot(isEnglish: Boolean) = if (isEnglish) "Tourist Spot" else "দর্শনীয় স্থান"
    fun busTime(isEnglish: Boolean) = if (isEnglish) "Bus Time" else "বাসের সময়..."
    fun trainTime(isEnglish: Boolean) = if (isEnglish) "Train Time" else "ট্রেনের সময়..."
    fun carRental(isEnglish: Boolean) = if (isEnglish) "Car Rental" else "গাড়ি ভাড়া"
    fun educational(isEnglish: Boolean) = if (isEnglish) "Educational" else "শিক্ষাপ্রতিষ্ঠান"
    fun teacher(isEnglish: Boolean) = if (isEnglish) "Teacher" else "শিক্ষক"

    // Slider
    fun cumillaCity(isEnglish: Boolean) = if (isEnglish) "Rotterdam City" else "রটারডাম সিটি"
    fun allServicesInOnePlace(isEnglish: Boolean) = if (isEnglish) "All services in one place" else "আপনার শহরের সকল সেবা এক জায়গায়"
    fun emergencyServices(isEnglish: Boolean) = if (isEnglish) "Emergency Services" else "জরুরি সেবা"
    fun hospitalDoctorAmbulance(isEnglish: Boolean) = if (isEnglish) "Hospital, Doctor, Ambulance" else "হাসপাতাল, ডাক্তার, এ্যাম্বুলেন্স"
    fun dailyOffers(isEnglish: Boolean) = if (isEnglish) "Daily Offers" else "দৈনিক অফার"
    fun specialDiscounts(isEnglish: Boolean) = if (isEnglish) "Get special discounts" else "বিশেষ ছাড় ও অফার পান"
    fun transportService(isEnglish: Boolean) = if (isEnglish) "Transport Service" else "যোগাযোগ সেবা"
    fun busTrainSchedule(isEnglish: Boolean) = if (isEnglish) "Bus and Train Schedule" else "বাস ও ট্রেনের সময়সূচী"
}

