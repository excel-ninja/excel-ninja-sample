package com.excelNinja.sample

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.File
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class KotlinTestRunner(
    private val productService: ProductService,
    private val studentService: StudentService
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(KotlinTestRunner::class.java)
    private val createdFiles = mutableListOf<String>()

    override fun run(vararg args: String) {
        logger.info("Kotlin ExcelNinja Test Started!")
        logger.info("=================================")

        try {
            createOutputDirectory()

            runProductDemo()
            runStudentDemo()

            logger.info("=================================")
            logger.info("All Kotlin ExcelNinja tests completed successfully!")

        } catch (e: Exception) {
            logger.error("Error during Kotlin test execution: {}", e.message, e)
        } finally {
            cleanupFiles()
        }
    }

    private fun createOutputDirectory() {
        val outputDir = File("output")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
            logger.info("Created output directory: {}", outputDir.absolutePath)
        }
    }

    private fun saveFileAndTrack(filePath: String, saveAction: () -> Unit) {
        try {
            saveAction()
            createdFiles.add(filePath)
            logger.info("File created and tracked: {}", filePath)
        } catch (e: Exception) {
            logger.error("Failed to create file: {}", filePath, e)
        }
    }

    private fun runProductDemo() {
        logger.info("\n=== Product Demo (Kotlin) - BigDecimal & LocalDateTime ===")

        val products = listOf(
            Product(
                id = 1L,
                name = "MacBook Pro M3",
                category = "Laptop",
                price = BigDecimal("2499.99"),
                stockQuantity = 15,
                isActive = true,
                createdAt = LocalDateTime.of(2024, 1, 15, 10, 30, 0)
            ),
            Product(
                id = 2L,
                name = "iPhone 15 Pro",
                category = "Smartphone",
                price = BigDecimal("1199.00"),
                stockQuantity = 5,
                isActive = true,
                createdAt = LocalDateTime.of(2024, 2, 20, 14, 0, 0)
            ),
            Product(
                id = 3L,
                name = "AirPods Pro",
                category = "Audio",
                price = BigDecimal("249.99"),
                stockQuantity = 25,
                isActive = true,
                createdAt = LocalDateTime.of(2024, 3, 10, 9, 45, 0)
            ),
            Product(
                id = 4L,
                name = "Magic Mouse",
                category = "Accessory",
                price = BigDecimal("79.99"),
                stockQuantity = 8,
                isActive = false,
                createdAt = LocalDateTime.of(2024, 1, 5, 16, 20, 0)
            ),
            Product(
                id = 5L,
                name = "iPad Air",
                category = "Tablet",
                price = BigDecimal("599.00"),
                stockQuantity = 12,
                isActive = true,
                createdAt = LocalDateTime.of(2024, 2, 28, 11, 15, 0)
            )
        )

        val productsFile = "output/products.xlsx"
        saveFileAndTrack(productsFile) {
            productService.saveProductsToExcel(products, productsFile)
        }

        logger.info("Sample products saved:")
        products.forEach { product ->
            logger.info(
                "  - {} ({}, Stock: {}, Total Value: {})",
                product.getDisplayName(),
                product.getFormattedPrice(),
                product.stockQuantity,
                product.getTotalValue()
            )
        }

        val readProducts = productService.readProductsFromExcel(productsFile)

        val lowStockProducts = productService.getLowStockProducts(readProducts)
        if (lowStockProducts.isNotEmpty()) {
            logger.info("Low stock products:")
            lowStockProducts.forEach { product ->
                logger.info("  - {} (Stock: {})", product.name, product.stockQuantity)
            }

            val lowStockFile = "output/low_stock_products.xlsx"
            saveFileAndTrack(lowStockFile) {
                productService.saveProductsToExcel(lowStockProducts, lowStockFile)
            }
        }

        val premiumProducts = productService.getPremiumProducts(readProducts)
        logger.info("Premium products:")
        premiumProducts.forEach { product ->
            logger.info("  - {} ({})", product.name, product.getFormattedPrice())
        }

        val midRangeProducts = productService.getProductsByPriceRange(
            readProducts,
            BigDecimal("100"),
            BigDecimal("1000")
        )
        logger.info("Mid-range products (₩100 - ₩1,000): {}", midRangeProducts.size)

        val stats = productService.getProductStatistics(readProducts)
        logger.info("Product Statistics:")
        logger.info("  - Total Products: {}", stats["totalProducts"])
        logger.info("  - Total Inventory Value: {}", stats["totalValue"])
        logger.info("  - Average Price: {}", stats["averagePrice"])
        logger.info("  - Low Stock Count: {}", stats["lowStockCount"])
        logger.info("  - Premium Product Count: {}", stats["premiumProductCount"])

        val categoryStats = productService.getCategoryStatistics(readProducts)
        logger.info("Category Statistics:")
        categoryStats.forEach { (category, stats) ->
            logger.info(
                "  - {}: {} items, Total Value: {}, Avg Price: {}",
                category,
                stats["count"],
                stats["totalValue"],
                stats["averagePrice"]
            )
        }
    }

    private fun runStudentDemo() {
        logger.info("\n=== Student Demo (Kotlin) ===")

        val students = listOf(
            Student(
                studentId = "CS001",
                name = "Hyunsoo Jo",
                email = "hynsoo@university.edu",
                major = "Computer Science",
                grade = 3,
                gpa = 3.8,
                hasScholarship = true
            ),
            Student(
                studentId = "ME002",
                name = "Eunmi Lee",
                email = "eunmi@university.edu",
                major = "Mechanical Engineering",
                grade = 2,
                gpa = 3.2,
                hasScholarship = false
            ),
            Student(
                studentId = "CS003",
                name = "Changhee Lee",
                email = "changhee@university.edu",
                major = "Computer Science",
                grade = 4,
                gpa = 3.9,
                hasScholarship = true
            ),
            Student(
                studentId = "BIO004",
                name = "Wanjoo Kim",
                email = "wanjoo@university.edu",
                major = "Biology",
                grade = 1,
                gpa = 3.6,
                hasScholarship = false
            ),
            Student(
                studentId = "ME005",
                name = "Daejoon Kim",
                email = "daejoon@university.edu",
                major = "Mechanical Engineering",
                grade = 3,
                gpa = 3.4,
                hasScholarship = true
            )
        )

        val studentsFile = "output/students.xlsx"
        saveFileAndTrack(studentsFile) {
            studentService.saveStudentsToExcel(students, studentsFile)
        }

        logger.info("Sample students saved:")
        students.forEach { student ->
            logger.info(
                "  - {} ({}, {}, GPA: {})",
                student.name,
                student.major,
                student.getGradeLevel(),
                student.gpa
            )
        }

        val readStudents = studentService.readStudentsFromExcel(studentsFile)

        val honorStudents = studentService.getHonorStudents(readStudents)
        logger.info("Honor Students (GPA >= 3.5):")
        honorStudents.forEach { student ->
            logger.info("  - {} (GPA: {})", student.name, student.gpa)
        }

        val honorStudentsFile = "output/honor_students.xlsx"
        saveFileAndTrack(honorStudentsFile) {
            studentService.saveStudentsToExcel(honorStudents, honorStudentsFile)
        }

        val scholarshipStudents = studentService.getScholarshipStudents(readStudents)
        logger.info("Scholarship Students:")
        scholarshipStudents.forEach { student ->
            logger.info("  - {} ({})", student.name, student.major)
        }

        val scholarshipStudentsFile = "output/scholarship_students.xlsx"
        saveFileAndTrack(scholarshipStudentsFile) {
            studentService.saveStudentsToExcel(scholarshipStudents, scholarshipStudentsFile)
        }

        val majorStats = studentService.getMajorStatistics(readStudents)
        logger.info("Major Statistics:")
        majorStats.forEach { (major, stats) ->
            logger.info(
                "  - {}: {} students (Avg GPA: {}, Honor: {}, Scholarship: {})",
                major,
                stats["count"],
                String.format("%.2f", stats["averageGPA"]),
                stats["honorStudents"],
                stats["scholarshipStudents"]
            )
        }
    }

    private fun cleanupFiles() {
        logger.info("\n=== Cleaning up generated files ===")

        var deletedCount = 0
        var failedCount = 0

        createdFiles.forEach { filePath ->
            try {
                val file = File(filePath)
                if (file.exists() && file.delete()) {
                    logger.info("Deleted: {}", filePath)
                    deletedCount++
                } else {
                    logger.warn("File not found or failed to delete: {}", filePath)
                    failedCount++
                }
            } catch (e: Exception) {
                logger.error("Error deleting file {}: {}", filePath, e.message)
                failedCount++
            }
        }

        cleanupOutputDirectory()

        logger.info("Cleanup completed - Deleted: {}, Failed: {}", deletedCount, failedCount)
    }

    private fun cleanupOutputDirectory() {
        try {
            val outputDir = File("output")
            if (outputDir.exists() && outputDir.isDirectory) {
                val files = outputDir.listFiles()
                if (files == null || files.isEmpty()) {
                    if (outputDir.delete()) {
                        logger.info("Deleted empty output directory")
                    }
                } else {
                    logger.info("Output directory not empty, keeping it (remaining files: {})", files.size)
                }
            }
        } catch (e: Exception) {
            logger.error("Error cleaning up output directory: {}", e.message)
        }
    }
}