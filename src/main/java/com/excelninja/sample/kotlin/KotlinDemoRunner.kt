package com.excelNinja.sample

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class KotlinTestRunner(
    private val productService: ProductService,
    private val studentService: StudentService
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(KotlinTestRunner::class.java)

    override fun run(vararg args: String) {
        logger.info("Kotlin ExcelNinja Test Started!")
        logger.info("=================================")

        try {
            runProductDemo()
            runStudentDemo()

            logger.info("=================================")
            logger.info("All Kotlin ExcelNinja tests completed successfully!")

        } catch (e: Exception) {
            logger.error("Error during Kotlin test execution: {}", e.message, e)
        }
    }

    private fun runProductDemo() {
        logger.info("\n=== Product Demo (Kotlin) ===")

        val products = listOf(
            Product(
                id = 1L,
                name = "MacBook Pro M3",
                category = "Laptop",
                price = 2499.00,
                stockQuantity = 15,
                isActive = true,
                createdAt = "2024-01-15T10:30:00"
            ),
            Product(
                id = 2L,
                name = "iPhone 15 Pro",
                category = "Smartphone",
                price = 1199.00,
                stockQuantity = 5,
                isActive = true,
                createdAt = "2024-02-20T14:00:00"
            ),
            Product(
                id = 3L,
                name = "AirPods Pro",
                category = "Audio",
                price = 249.00,
                stockQuantity = 25,
                isActive = true,
                createdAt = "2024-03-10T09:45:00"
            ),
            Product(
                id = 4L,
                name = "Magic Mouse",
                category = "Accessory",
                price = 79.00,
                stockQuantity = 8,
                isActive = false,
                createdAt = "2024-01-05T16:20:00"
            ),
            Product(
                id = 5L,
                name = "iPad Air",
                category = "Tablet",
                price = 599.00,
                stockQuantity = 12,
                isActive = true,
                createdAt = "2024-02-28T11:15:00"
            )
        )

        // Save products to Excel
        productService.saveProductsToExcel(products, "output/products.xlsx")

        logger.info("Sample products saved:")
        products.forEach { product ->
            logger.info(
                "  - {} ({}, Stock: {}, Value: {})",
                product.getDisplayName(),
                product.price,
                product.stockQuantity,
                product.getTotalValue()
            )
        }

        val readProducts = productService.readProductsFromExcel("output/products.xlsx")

        val lowStockProducts = productService.getLowStockProducts(readProducts)
        if (lowStockProducts.isNotEmpty()) {
            logger.info("Low stock products:")
            lowStockProducts.forEach { product ->
                logger.info("  - {} (Stock: {})", product.name, product.stockQuantity)
            }
            productService.saveProductsToExcel(lowStockProducts, "output/low_stock_products.xlsx")
        }

        val stats = productService.getProductStatistics(readProducts)
        logger.info("Product Statistics:")
        logger.info("  - Total Products: {}", stats["totalProducts"])
        logger.info("  - Total Value: {}", stats["totalValue"])
        logger.info("  - Average Price: {}", stats["averagePrice"])
        logger.info("  - Low Stock Count: {}", stats["lowStockCount"])
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

        studentService.saveStudentsToExcel(students, "output/students.xlsx")

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

        val readStudents = studentService.readStudentsFromExcel("output/students.xlsx")

        val honorStudents = studentService.getHonorStudents(readStudents)
        logger.info("Honor Students (GPA >= 3.5):")
        honorStudents.forEach { student ->
            logger.info("  - {} (GPA: {})", student.name, student.gpa)
        }
        studentService.saveStudentsToExcel(honorStudents, "output/honor_students.xlsx")

        val scholarshipStudents = studentService.getScholarshipStudents(readStudents)
        logger.info("Scholarship Students:")
        scholarshipStudents.forEach { student ->
            logger.info("  - {} ({})", student.name, student.major)
        }
        studentService.saveStudentsToExcel(scholarshipStudents, "output/scholarship_students.xlsx")

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
}