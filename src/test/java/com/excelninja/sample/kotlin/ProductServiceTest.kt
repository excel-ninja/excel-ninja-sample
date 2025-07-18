package com.excelninja.sample.kotlin

import com.excelNinja.sample.Product
import com.excelNinja.sample.ProductService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.*
import java.io.File

@DisplayName("상품 서비스 테스트")
class ProductServiceTest {

    private lateinit var productService: ProductService
    private val testFileName = "test_products.xlsx"
    private val outputDir = "test_output"

    @BeforeEach
    fun setUp() {
        productService = ProductService()
        File(outputDir).mkdirs()
    }

    @AfterEach
    fun tearDown() {
        File("$outputDir/$testFileName").delete()
        File(outputDir).deleteRecursively()
    }

    @Test
    @DisplayName("상품 목록을 엑셀 파일로 저장할 수 있다")
    fun saveProductsToExcel() {
        val products = createTestProducts()
        val fileName = "$outputDir/$testFileName"

        productService.saveProductsToExcel(products, fileName)

        assertThat(File(fileName)).exists()
        assertThat(File(fileName).length()).isGreaterThan(0)
    }

    @Test
    @DisplayName("엑셀 파일에서 상품 목록을 읽을 수 있다")
    fun readProductsFromExcel() {
        val originalProducts = createTestProducts()
        val fileName = "$outputDir/$testFileName"

        productService.saveProductsToExcel(originalProducts, fileName)
        val readProducts = productService.readProductsFromExcel(fileName)

        assertThat(readProducts).hasSize(originalProducts.size)
        assertThat(readProducts[0].name).isEqualTo("MacBook Pro M3")
        assertThat(readProducts[0].price).isEqualTo(2499.0)
        assertThat(readProducts[0].stockQuantity).isEqualTo(15)
    }

    @Test
    @DisplayName("재고 부족 상품을 필터링할 수 있다")
    fun getLowStockProducts() {
        val products = createTestProducts()

        val lowStockProducts = productService.getLowStockProducts(products)

        assertThat(lowStockProducts).hasSize(2)
    }

    @Test
    @DisplayName("상품 통계를 계산할 수 있다")
    fun getProductStatistics() {
        val products = createTestProducts()

        val stats = productService.getProductStatistics(products)

        assertThat(stats["totalProducts"]).isEqualTo(5)
        assertThat(stats["totalValue"] as Double).isGreaterThan(0.0)
        assertThat(stats["averagePrice"] as Double).isGreaterThan(0.0)
        assertThat(stats["totalStock"]).isEqualTo(65)
        assertThat(stats["activeProducts"]).isEqualTo(4)
        assertThat(stats["lowStockCount"]).isEqualTo(2)
    }

    @Test
    @DisplayName("존재하지 않는 파일을 읽으려 하면 예외가 발생한다")
    fun readNonExistentFile() {
        assertThrows<RuntimeException> {
            productService.readProductsFromExcel("nonexistent.xlsx")
        }
    }

    @Test
    @DisplayName("빈 상품 목록의 통계를 계산할 수 있다")
    fun getEmptyProductStatistics() {
        val emptyProducts = emptyList<Product>()

        val stats = productService.getProductStatistics(emptyProducts)

        assertThat(stats["totalProducts"]).isEqualTo(0)
        assertThat(stats["totalValue"]).isEqualTo(0.0)
        assertThat(stats["averagePrice"]).isEqualTo(0.0)
        assertThat(stats["totalStock"]).isEqualTo(0)
        assertThat(stats["activeProducts"]).isEqualTo(0)
        assertThat(stats["lowStockCount"]).isEqualTo(0)
    }

    private fun createTestProducts(): List<Product> {
        return listOf(
            Product(
                id = 1L,
                name = "MacBook Pro M3",
                category = "Laptop",
                price = 2499.0,
                stockQuantity = 15,
                isActive = true,
                createdAt = "2024-01-15T10:30:00"
            ),
            Product(
                id = 2L,
                name = "iPhone 15 Pro",
                category = "Smartphone",
                price = 1199.0,
                stockQuantity = 5,
                isActive = true,
                createdAt = "2024-02-20T14:00:00"
            ),
            Product(
                id = 3L,
                name = "AirPods Pro",
                category = "Audio",
                price = 249.0,
                stockQuantity = 25,
                isActive = true,
                createdAt = "2024-03-10T09:45:00"
            ),
            Product(
                id = 4L,
                name = "Magic Mouse",
                category = "Accessory",
                price = 79.0,
                stockQuantity = 8,
                isActive = false,
                createdAt = "2024-01-05T16:20:00"
            ),
            Product(
                id = 5L,
                name = "iPad Air",
                category = "Tablet",
                price = 599.0,
                stockQuantity = 12,
                isActive = true,
                createdAt = "2024-02-28T11:15:00"
            )
        )
    }
}