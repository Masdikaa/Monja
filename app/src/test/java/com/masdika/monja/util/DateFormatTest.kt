package com.masdika.monja.util

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateFormatTest {
    @Test
    fun `dateFormat when Given Valid UTC String returns Formatted Jakarta Time String`() = runTest {
        // ARRANGE
        val validUtcDateString = "2026-04-10T00:00:00Z"
        val expectedFormattedDate = "10 Apr 2026, 07:00"

        // ACT
        val result = dateFormat(validUtcDateString)

        // ASSERT
        assertEquals(
            expectedFormattedDate,
            result,
            "The date string must be converted to Asia/Jakarta timezone and formatted correctly"
        )
    }

    @Test
    fun `dateFormat when Given Valid UTC String Crossing Midnight returns Next Day Jakarta Time String`() =
        runTest {
            val lateUtcDateString = "2026-04-10T20:00:00Z"
            val expectedFormattedDate = "11 Apr 2026, 03:00"

            val result = dateFormat(lateUtcDateString)

            assertEquals(
                expectedFormattedDate,
                result,
                "The timezone conversion must correctly roll over to the next day when crossing midnight"
            )
        }

}