package com.stefata.sofiasupermarketsapi.flows

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import com.stefata.sofiasupermarketsapi.brochure.FantasticoBrochureDownloader
import com.stefata.sofiasupermarketsapi.getProduct
import com.stefata.sofiasupermarketsapi.interfaces.PdfProductsExtractor
import com.stefata.sofiasupermarketsapi.model.ProductStore
import com.stefata.sofiasupermarketsapi.model.Supermarket
import com.stefata.sofiasupermarketsapi.repository.ProductStoreRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
internal class FantasticoFlowTest {

    @MockK
    lateinit var fantasticoBrochureDownloader: FantasticoBrochureDownloader

    @MockK
    lateinit var pdfProductsExtractor: PdfProductsExtractor

    @MockK
    lateinit var productStoreRepository: ProductStoreRepository

    @InjectMockKs
    lateinit var underTest: FantasticoFlow

    @Test
    fun `runs flow for fantastico`(@TempDir tempDir: Path) {

        val randomFile = tempDir.resolve(UUID.randomUUID().toString())
        randomFile.toFile().createNewFile()

        val validUntil = LocalDate.of(2021, 5, 6)

        val foo = getProduct("foo").copy(validUntil = validUntil)
        val bar = getProduct("bar").copy(validUntil = validUntil)

        every { fantasticoBrochureDownloader.download() } returns Pair(randomFile, validUntil)
        every { pdfProductsExtractor.extract(any()) } returns listOf(foo, bar)
        every { productStoreRepository.saveIfProductsNotEmpty(any()) } returnsArgument 0

        underTest.runSafely()

        verify { pdfProductsExtractor.extract(any()) }

        val expectedToSave = ProductStore(supermarket = "Fantastico", products = listOf(foo, bar))
        verify {
            productStoreRepository.saveIfProductsNotEmpty(match {
                it.supermarket == expectedToSave.supermarket &&
                        it.products == expectedToSave.products
            })
        }

        assertThat(Files.exists(randomFile)).isFalse()
    }

    @Test
    fun `gets correct supermarket name`() {
        assertThat(underTest.getSupermarket()).isEqualTo(Supermarket.FANTASTICO)
    }
}