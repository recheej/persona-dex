package com.persona5dex

import java.util.*

fun String.normalizeName(): String =
        replace("\\s+".toRegex(), "")
                .replace("_".toRegex(), "")
                .replace("'".toRegex(), "").toLowerCase(Locale.US)

infix fun String.equalNormalized(other: String) =
        this.normalizeName() == other.normalizeName()