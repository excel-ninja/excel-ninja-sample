package com.excelNinja.sample

import com.excelninja.domain.annotation.ExcelReadColumn
import com.excelninja.domain.annotation.ExcelWriteColumn

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
    var price: Double = 0.0,

    @ExcelReadColumn(headerName = "Stock Quantity")
    @ExcelWriteColumn(headerName = "Stock Quantity", order = 5)
    var stockQuantity: Int = 0,

    @ExcelReadColumn(headerName = "Is Active")
    @ExcelWriteColumn(headerName = "Is Active", order = 6)
    var isActive: Boolean = true,

    @ExcelReadColumn(headerName = "Created At")
    @ExcelWriteColumn(headerName = "Created At", order = 7)
    var createdAt: String = "",
) {

    fun isLowStock(): Boolean = stockQuantity < 10

    fun getTotalValue(): Double = price.toBigDecimal().multiply(stockQuantity.toBigDecimal()).toDouble()

    fun getDisplayName(): String = "[$category] $name"
}