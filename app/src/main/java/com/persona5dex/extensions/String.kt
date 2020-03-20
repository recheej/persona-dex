@file:JvmName("StringUtils")

package com.persona5dex.extensions

import java.util.*

fun String.toLowerCaseInsensitive() = toLowerCase(Locale.ROOT)

fun String.normalize(): String =
        "[\\s_\']".toRegex().replace(this, "").toLowerCaseInsensitive()

infix fun String.equalNormalized(other: String) =
        this.normalize() == other.normalize()