package com.stefata.sofiasupermarketsapi.flows

import com.stefata.sofiasupermarketsapi.common.Log
import com.stefata.sofiasupermarketsapi.common.Log.Companion.log
import com.stefata.sofiasupermarketsapi.interfaces.SupermarketFlow
import com.stefata.sofiasupermarketsapi.interfaces.UrlProductsExtractor
import com.stefata.sofiasupermarketsapi.links.BillaSublinksScraper
import com.stefata.sofiasupermarketsapi.model.ProductStore
import com.stefata.sofiasupermarketsapi.model.Supermarket
import com.stefata.sofiasupermarketsapi.repository.ProductStoreRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Log
@Component
class BillaFlow(
    val billaSublinksScraper: BillaSublinksScraper,
    @Qualifier("Billa") val urlProductsExtractor: UrlProductsExtractor,
    val productStoreRepository: ProductStoreRepository
) : SupermarketFlow {

    override fun run() {

        val products = billaSublinksScraper.getSublinks().flatMap {
            urlProductsExtractor.extract(it)
        }

        log.info("Retrieved ${products.size} products")
        log.info("Saving ${getSupermarket().title} products")

        val toSave = ProductStore(supermarket = getSupermarket().title, products = products)
        productStoreRepository.saveIfProductsNotEmpty(toSave)
    }

    override fun getSupermarket(): Supermarket {
        return Supermarket.BILLA
    }
}