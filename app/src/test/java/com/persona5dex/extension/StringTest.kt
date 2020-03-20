package com.persona5dex.extension

import com.persona5dex.extensions.normalizeName
import org.junit.Assert
import org.junit.Test

class StringTest {
    @Test
    fun `normalizeName() removes whitespace`() {
        val normalizedName = "test this".normalizeName()
        Assert.assertEquals("testthis", normalizedName)
    }

    @Test
    fun `normalizeName() removes "_"`() {
        val normalizedName = "test_this".normalizeName()
        Assert.assertEquals("testthis", normalizedName)
    }

    @Test
    fun `normalizeName() removes "'"`() {
        val normalizedName = "test'this".normalizeName()
        Assert.assertEquals("testthis", normalizedName)
    }

    @Test
    fun `normalizeName() everything`() {
        val normalizedName = "test'thi_s  _ '".normalizeName()
        Assert.assertEquals("testthis", normalizedName)
    }
}