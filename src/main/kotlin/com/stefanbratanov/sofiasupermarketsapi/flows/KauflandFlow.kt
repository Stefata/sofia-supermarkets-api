package com.stefanbratanov.sofiasupermarketsapi.flows

import com.stefanbratanov.sofiasupermarketsapi.common.Log
import com.stefanbratanov.sofiasupermarketsapi.common.Log.Companion.log
import com.stefanbratanov.sofiasupermarketsapi.interfaces.SupermarketFlow
import com.stefanbratanov.sofiasupermarketsapi.interfaces.UrlProductsExtractor
import com.stefanbratanov.sofiasupermarketsapi.links.KauflandSublinksScraper
import com.stefanbratanov.sofiasupermarketsapi.model.ProductStore
import com.stefanbratanov.sofiasupermarketsapi.model.Supermarket
import com.stefanbratanov.sofiasupermarketsapi.repository.ProductStoreRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Log
@Component
class KauflandFlow(
  val kauflandSublinksScraper: KauflandSublinksScraper,
  @Qualifier("Kaufland") val urlProductsExtractor: UrlProductsExtractor,
  val productStoreRepository: ProductStoreRepository,
) : SupermarketFlow {

  override fun run() {
    val products =
      kauflandSublinksScraper.getSublinks().flatMap { urlProductsExtractor.extract(it) }

    log.info("Retrieved ${products.size} products")
    log.info("Saving ${getSupermarket().title} products")

    val toSave = ProductStore(supermarket = getSupermarket().title, products = products)
    productStoreRepository.saveIfProductsNotEmpty(toSave)
  }

  override fun getSupermarket(): Supermarket {
    return Supermarket.KAUFLAND
  }
}
