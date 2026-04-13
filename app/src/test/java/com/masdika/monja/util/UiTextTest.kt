package com.masdika.monja.util

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UiTextTest {

    @MockK
    private lateinit var mockContext: Context

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `asString with Context when DynamicString should return the raw value`() {
        // ARRANGE
        val rawValue = "Heart rate is abnormal"
        val uiText = UiText.DynamicString(rawValue)

        // ACT
        val result = uiText.asString(mockContext)

        // ASSERT
        assertEquals(rawValue, result, "DynamicString should return its value as-is")
    }

    @Test
    fun `asString with Context when StringResource without args should resolve correctly`() {
        // ARRANGE
        val resId = 123
        val expectedString = "Successful Connection"
        val uiText = UiText.StringResource(resId)

        every { mockContext.getString(resId, *anyVararg()) } returns expectedString

        // ACT
        val result = uiText.asString(mockContext)

        // ASSERT
        assertEquals(expectedString, result, "StringResource should resolve the resId using context.getString()")
    }

    @Test
    fun `asString with Context when StringResource with args should resolve with formatted value`() {
        // ARRANGE
        val resId = 456
        val macAddress = "AA:BB:CC:DD"
        val expectedString = "Device $macAddress is Online"
        val uiText = UiText.StringResource(resId, macAddress)

        every { mockContext.getString(resId, *anyVararg()) } returns expectedString

        // ACT
        val result = uiText.asString(mockContext)

        // ASSERT
        assertEquals(expectedString, result, "StringResource should resolve resId and format the arguments correctly")
    }
}