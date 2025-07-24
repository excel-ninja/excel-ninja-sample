package com.excelNinja.sample

import com.excelninja.application.facade.NinjaExcel
import com.excelninja.domain.model.ExcelWorkbook
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class ProductService {

    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    fun saveProductsToExcel(products: List<Product>, fileName: String) {
        try {
            val document = ExcelWorkbook.builder().sheet("Product Inventory", products).build()
            NinjaExcel.write(document, fileName)
            logger.info("Successfully saved {} products to {}", products.size, fileName)
        } catch (e: Exception) {
            logger.error("Failed to save products Excel file: {}", e.message)
            throw RuntimeException("Failed to save products Excel file", e)
        }
    }

    fun readProductsFromExcel(fileName: String): List<Product> {
        return try {
            val products = NinjaExcel.read(fileName, Product::class.java)
            logger.info("Successfully read {} products from {}", products.size, fileName)
            products
        } catch (e: Exception) {
            logger.error("Failed to read products Excel file: {}", e.message)
            throw RuntimeException("Failed to read products Excel file", e)
        }
    }

    fun getLowStockProducts(products: List<Product>): List<Product> {
        return products.filter { it.isLowStock() }
    }

    fun getProductsByPriceRange(
        products: List<Product>,
        minPrice: BigDecimal,
        maxPrice: BigDecimal
    ): List<Product> {
        return products.filter { it.price >= minPrice && it.price <= maxPrice }
    }

    fun getPremiumProducts(products: List<Product>): List<Product> {
        return products.filter { it.getPriceCategory() == "Premium" }
    }

    fun getProductStatistics(products: List<Product>): Map<String, Any> {
        if (products.isEmpty()) {
            return mapOf(
                "totalProducts" to 0,
                "totalValue" to BigDecimal.ZERO,
                "averagePrice" to BigDecimal.ZERO,
                "totalStock" to 0,
                "activeProducts" to 0,
                "lowStockCount" to 0,
                "premiumProductCount" to 0
            )
        }

        val totalValue = products.sumOf { it.getTotalValue() }
        val averagePrice = products.map { it.price }
            .fold(BigDecimal.ZERO) { acc, price -> acc.add(price) }
            .divide(BigDecimal(products.size), 2, RoundingMode.HALF_UP)

        val totalStock = products.sumOf { it.stockQuantity }
        val activeProducts = products.count { it.isActive }
        val lowStockCount = products.count { it.isLowStock() }
        val premiumProductCount = products.count { it.getPriceCategory() == "Premium" }

        return mapOf(
            "totalProducts" to products.size,
            "totalValue" to totalValue,
            "averagePrice" to averagePrice,
            "totalStock" to totalStock,
            "activeProducts" to activeProducts,
            "lowStockCount" to lowStockCount,
            "premiumProductCount" to premiumProductCount
        )
    }

    fun getCategoryStatistics(products: List<Product>): Map<String, Map<String, Any>> {
        return products.groupBy { it.category }.mapValues { (_, categoryProducts) ->
            val totalValue = categoryProducts.sumOf { it.getTotalValue() }
            val averagePrice = if (categoryProducts.isNotEmpty()) {
                categoryProducts.map { it.price }
                    .fold(BigDecimal.ZERO) { acc, price -> acc.add(price) }
                    .divide(BigDecimal(categoryProducts.size), 2, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO
            }

            mapOf(
                "count" to categoryProducts.size,
                "totalValue" to totalValue,
                "averagePrice" to averagePrice,
                "totalStock" to categoryProducts.sumOf { it.stockQuantity },
                "lowStockCount" to categoryProducts.count { it.isLowStock() }
            )
        }
    }
}