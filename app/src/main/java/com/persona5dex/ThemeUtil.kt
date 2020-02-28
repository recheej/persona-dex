@file:JvmName("ThemeUtil")

package com.persona5dex

import android.content.Context
import android.content.res.Resources.Theme
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt


@ColorInt
fun Context.getThemeAttributeColor(@AttrRes themeColor: Int): Int {
    val typedValue = TypedValue()
    val theme: Theme = theme
    theme.resolveAttribute(themeColor, typedValue, true)
    return typedValue.data
}