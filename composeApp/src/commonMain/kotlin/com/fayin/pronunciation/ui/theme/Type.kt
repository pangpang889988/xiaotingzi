package com.fayin.pronunciation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val iOSLargeTitle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 41.sp, letterSpacing = 0.41.sp)
val iOSTitle1 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = 0.36.sp)
val iOSTitle2 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.35.sp)
val iOSHeadline = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = -0.41.sp)
val iOSBody = TextStyle(fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = -0.41.sp)
val iOSCallout = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 21.sp, letterSpacing = -0.32.sp)
val iOSSubhead = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = -0.24.sp)
val iOSFootnote = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = -0.08.sp)
val iOSCaption1 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.sp)
val iOSCaption2 = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 13.sp, letterSpacing = 0.07.sp)

val Typography = Typography(
    displayLarge = iOSLargeTitle, displayMedium = iOSTitle1, displaySmall = iOSTitle2,
    headlineLarge = iOSTitle2, headlineMedium = iOSHeadline, headlineSmall = iOSHeadline,
    titleLarge = iOSHeadline, titleMedium = iOSBody, titleSmall = iOSSubhead,
    bodyLarge = iOSBody, bodyMedium = iOSCallout, bodySmall = iOSFootnote,
    labelLarge = iOSHeadline, labelMedium = iOSSubhead, labelSmall = iOSCaption2
)
