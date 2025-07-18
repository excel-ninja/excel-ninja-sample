package com.excelNinja.sample

import com.excelninja.application.facade.NinjaExcel
import com.excelninja.domain.model.ExcelDocument
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductService {

    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    fun saveProductsToExcel(products: List<Product>, fileName: String) {
        try {
            val document = ExcelDocument.writer()
                .objects(products)
                .sheetName("Product Inventory")
                .columnWidth(1, 5000) // Product Name
                .columnWidth(2, 3000) // Category
                .create()

            NinjaExcel.write(document, fileName)
        } catch (e: Exception) {
            throw RuntimeException("Failed to save products Excel file", e)
        }
    }

    fun readProductsFromExcel(fileName: String): List<Product> {
        return try {
            val products = NinjaExcel.read(fileName, Product::class.java)
            products
        } catch (e: Exception) {
            throw RuntimeException("Failed to read products Excel file", e)
        }
    }

    fun getLowStockProducts(products: List<Product>): List<Product> {
        return products.filter { it.isLowStock() }
    }

    fun getProductStatistics(products: List<Product>): Map<String, Any> {
        val totalValue = products.sumOf { it.getTotalValue() }
        val averagePrice = if (products.isNotEmpty()) {
            products.sumOf { it.price } / products.size
        } else {
            0.0
        }
        val totalStock = products.sumOf { it.stockQuantity }
        val activeProducts = products.count { it.isActive }

        return mapOf(
            "totalProducts" to products.size,
            "totalValue" to totalValue,
            "averagePrice" to averagePrice,
            "totalStock" to totalStock,
            "activeProducts" to activeProducts,
            "lowStockCount" to products.count { it.isLowStock() }
        )
    }
}