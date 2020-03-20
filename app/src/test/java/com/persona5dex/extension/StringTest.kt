package com.persona5dex.extension

import com.persona5dex.extensions.normalize
import org.junit.Assert
import org.junit.Test

class StringTest {
    @Test
    fun `normalizeName() removes whitespace`() {
        val normalizedName = "test this".normalize()
        Assert.assertEquals("testthis", normalizedName)
    }

    @Test
    fun `normalizeName() removes "_"`() {
        val normalizedName = "test_this".normalize()
        Assert.assertEquals("testthis", normalizedName)
    }

    @Test
    fun `normalizeName() removes "'"`() {
        val normalizedName = "test'this".normalize()
        Assert.assertEquals("testthis", normalizedName)
    }

    @Test
    fun `normalizeName() everything`() {
        val normalizedName = "test'thi_s  _ '".normalize()
        Assert.assertEquals("testthis", normalizedName)
    }
}