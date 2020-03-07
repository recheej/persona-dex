@file:JvmName("ThemeUtil")

package com.persona5dex

import android.content.Context
import android.content.res.Resources.Theme
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate


@ColorInt
fun Context.getThemeAttributeColor(@AttrRes themeColor: Int): Int {
    val typedValue = TypedValue()
    val theme: Theme = theme
    theme.resolveAttribute(themeColor, typedValue, true)
    return typedValue.data
}

private fun getNightMode(nightModeString: String): Int =
        when (nightModeString.toInt()) {
            -1 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> error("cannot get night mode for string $nightModeString")
        }

fun setNightMode(nightModeString: String) {
    AppCompatDelegate.setDefaultNightMode(getNightMode(nightModeString))
}
