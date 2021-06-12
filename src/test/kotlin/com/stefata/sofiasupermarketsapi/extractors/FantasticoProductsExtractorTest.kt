package com.stefata.sofiasupermarketsapi.extractors

import assertk.assertThat
import assertk.assertions.isNotEmpty
import com.stefata.sofiasupermarketsapi.getPath
import com.stefata.sofiasupermarketsapi.readResource
import com.stefata.sofiasupermarketsapi.testObjectMapper
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode.NON_EXTENSIBLE
import java.nio.file.Paths

internal class FantasticoProductsExtractorTest {

    private val objectMapper = testObjectMapper()

    private val underTest = FantasticoProductsExtractor()

    @Test
    fun `test extracting products`() {

        val testPdf = getPath("/extractors/fantastico/fantastico_test.pdf")

        val products = underTest.extract(testPdf)

        val actualJson = objectMapper.writeValueAsString(products)
        val expectedJson = readResource("/extractors/fantastico/expected.json")

        JSONAssert.assertEquals(expectedJson, actualJson, NON_EXTENSIBLE)
    }

    @Test
    fun `test extracting products 2`() {

        val testPdf = getPath("/extractors/fantastico/fantastico_test_2.pdf")

        val products = underTest.extract(testPdf)

        val actualJson = objectMapper.writeValueAsString(products)
        val expectedJson = readResource("/extractors/fantastico/expected_2.json")

        JSONAssert.assertEquals(expectedJson, actualJson, NON_EXTENSIBLE)
    }

    @Test
    @Disabled("used for manual testing")
    fun `test fetching from real pdf`() {

        val pdf = Paths.get("fantastico_2.pdf")
        val products = underTest.extract(pdf)

        val json = objectMapper.writeValueAsString(products)

        println(json)

        assertThat(products).isNotEmpty()
    }
}