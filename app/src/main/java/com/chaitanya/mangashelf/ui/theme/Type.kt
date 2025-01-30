package com.chaitanya.mangashelf.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.chaitanya.mangashelf.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val DefaultTextStyle = TextStyle(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

@Composable
fun textStylePop12Lh16Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontWeight = FontWeight(500),
    )
}
@Composable
fun textStylePop12Lh16Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
        fontWeight = FontWeight(600),
    )
}
@Composable
fun textStylePop14Lh16Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontWeight = FontWeight(500)
    )
}
@Composable
fun textStylePop16Lh24Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontWeight = FontWeight(500)
    )
}

@Composable
fun textStylePop16Lh24Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.poppins_bold)),
        fontWeight = FontWeight(700)
    )
}


@Composable
fun textStylePop18Lh24Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.poppins_bold)),
        fontWeight = FontWeight(700)
    )
}
@Composable
fun textStylePop24Lh36Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 24.sp,
        lineHeight = 36.sp,
        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
        fontWeight = FontWeight(600)
    )
}

