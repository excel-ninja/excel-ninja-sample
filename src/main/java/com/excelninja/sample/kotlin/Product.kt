package com.excelNinja.sample

import com.excelninja.domain.annotation.ExcelReadColumn
import com.excelninja.domain.annotation.ExcelWriteColumn
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    @ExcelReadColumn(headerName = "Product ID")
    @ExcelWriteColumn(headerName = "Product ID", order = 1)
    var id: Long? = null,

    @ExcelReadColumn(headerName = "Product Name")
    @ExcelWriteColumn(headerName = "Product Name", order = 2)
    var name: String = "",

    @ExcelReadColumn(headerName = "Category")
    @ExcelWriteColumn(headerName = "Category", order = 3)
    var category: String = "",

    @ExcelReadColumn(headerName = "Price")
    @ExcelWriteColumn(headerName = "Price", order = 4)
    var price: BigDecimal = BigDecimal.ZERO,

    @ExcelReadColumn(headerName = "Stock Quantity")
    @ExcelWriteColumn(headerName = "Stock Quantity", order = 5)
    var stockQuantity: Int = 0,

    @ExcelReadColumn(headerName = "Is Active")
    @ExcelWriteColumn(headerName = "Is Active", order = 6)
    var isActive: Boolean = true,

    @ExcelReadColumn(headerName = "Created At")
    @ExcelWriteColumn(headerName = "Created At", order = 7)
    var createdAt: LocalDateTime? = null,
) {

    fun isLowStock(): Boolean = stockQuantity < 10

    fun getTotalValue(): BigDecimal = price.multiply(stockQuantity.toBigDecimal())

    fun getDisplayName(): String = "[$category] $name"

    fun getFormattedPrice(): String = "₩${String.format("%,d", price.toLong())}"

    fun getPriceCategory(): String = when {
        price < BigDecimal(100) -> "Budget"
        price < BigDecimal(1000) -> "Mid-range"
        else -> "Premium"
    }
}