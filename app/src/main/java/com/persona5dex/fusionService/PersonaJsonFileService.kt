package com.persona5dex.fusionService

import android.content.Context
import android.util.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader

abstract class PersonaJsonFileService<T>(context: Context) : PersonaFileService<T>(context) {
    protected abstract suspend fun parseJson(jsonReader: JsonReader): T

    final override suspend fun parseFile(fileInputStream: InputStream): T =
            withContext(Dispatchers.IO) {
                JsonReader(InputStreamReader(fileInputStream, "UTF-8")).run {
                    use {
                        parseJson(it)
                    }
                }
            }
}