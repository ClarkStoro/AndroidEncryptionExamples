package com.clarkstoro.androidencryptionexamples.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Dimensions(
    // dp
    val size4: Dp,
    val size16: Dp,
    val size20: Dp,
    val defaultMargin: Dp,
    val cornerRadiusModalBottomSheet: Dp,
    val largeTopMargin: Dp,
    val signupProfilePicture: Dp,

    // sp
    val defaultTitleFontSize: TextUnit = 24.sp
)

val defaultDimensions = Dimensions(
    // dp
    size4 = 4.dp,
    size16 = 16.dp,
    size20 = 20.dp,
    defaultMargin = 16.dp,
    cornerRadiusModalBottomSheet = 16.dp,
    largeTopMargin = 64.dp,
    signupProfilePicture = 120.dp
)

// Ratio is 4 for default and 3 for small.

val smallDimensions = Dimensions(
    // dp
    size4 = 3.dp,
    size16 = 12.dp,
    size20 = 15.dp,
    defaultMargin = 12.dp,
    cornerRadiusModalBottomSheet = 12.dp,
    largeTopMargin = 48.dp,
    signupProfilePicture = 90.dp
)