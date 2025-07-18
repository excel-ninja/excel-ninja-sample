package com.excelninja.sample.kotlin

import com.excelNinja.sample.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("상품 모델 테스트")
class ProductTest {

    @Test
    @DisplayName("재고가 10개 미만이면 재고 부족으로 판단한다")
    fun isLowStock() {
        val lowStockProduct = Product(stockQuantity = 5)
        val normalStockProduct = Product(stockQuantity = 15)

        assertThat(lowStockProduct.isLowStock()).isTrue()
        assertThat(normalStockProduct.isLowStock()).isFalse()
    }

    @Test
    @DisplayName("총 가치를 계산할 수 있다")
    fun getTotalValue() {
        val product = Product(price = 100.0, stockQuantity = 5)

        assertThat(product.getTotalValue()).isEqualTo(500.0)
    }

    @Test
    @DisplayName("상품 표시명을 생성할 수 있다")
    fun getDisplayName() {
        val product = Product(name = "MacBook Pro", category = "Laptop")

        assertThat(product.getDisplayName()).isEqualTo("[Laptop] MacBook Pro")
    }
}