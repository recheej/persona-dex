package com.persona5dex.dagger.fusionService

import android.content.Context
import androidx.annotation.RawRes
import java.io.InputStream

abstract class PersonaFileService<T>(private val context: Context) {
    @RawRes
    protected abstract fun getFileRes(): Int

    suspend fun parseFile(): T = parseFile(context.resources.openRawResource(getFileRes()))

    protected abstract suspend fun parseFile(fileInputStream: InputStream): T
}
