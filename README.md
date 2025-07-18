# ExcelNinja Sample

Kotlin/Spring Boot sample showcasing ExcelNinja library for Excel file operations demo

## Overview

This project demonstrates how to use the ExcelNinja library with Spring Boot to handle Excel file operations seamlessly. It includes examples in both Java and Kotlin, covering common use cases like data export/import, filtering, and statistical analysis.

## Features

- **Excel Read/Write Operations**: Create and read Excel files with custom column mapping
- **Data Models**: Product and Student management with business logic
- **Filtering & Statistics**: Low stock alerts, honor roll filtering, department statistics
- **Dual Language Support**: Examples in both Java and Kotlin
- **Automatic Directory Creation**: Smart file handling with error management

## Tech Stack

- **Spring Boot 3.4.7**
- **Kotlin 1.9.25**
- **Java 21**
- **ExcelNinja 0.0.5**
- **Gradle**

## Quick Start

### Prerequisites

- Java 21 or higher
- Gradle (or use included Gradle wrapper)

### Installation

1. Clone the repository:

git clone <repository-url>
cd excel-ninja-sample

2. Run the application:

./gradlew bootRun

The application will automatically:
- Create sample data
- Generate Excel files in the `output/` directory
- Read data back from Excel files
- Display statistics and filtered results

## Project Structure

```
src/main/java/com/excelninja/sample/
└── java/                           # Java examples
    ├── Employee.java               # Employee data model
    ├── EmployeeExcelService.java   # Excel operations for employees
    └── SampleDemoRunner.java       # Java demo runner
└── kotlin/                         # Kotlin examples
    ├── Product.kt                  # Product data model
    ├── ProductService.kt           # Excel operations for products
    ├── Student.kt                  # Student data model
    ├── StudentService.kt           # Excel operations for students
    └── KotlinDemoRunner.kt         # Kotlin demo runner
```

## Usage Examples

### Excel Column Mapping

```kotlin
data class Product(
    @ExcelReadColumn(headerName = "Product ID")
    @ExcelWriteColumn(headerName = "Product ID", order = 1)
    var id: Long? = null,
    
    @ExcelReadColumn(headerName = "Product Name")
    @ExcelWriteColumn(headerName = "Product Name", order = 2)
    var name: String = ""
)
```

### Writing to Excel
```kotlin
fun saveProductsToExcel(products: List<Product>, fileName: String) {
    val document = ExcelDocument.writer()
    .objects(products)
    .sheetName("Product Inventory")
    .columnWidth(1, 5000)
    .create()
    
    NinjaExcel.write(document, fileName)
}
```

### Reading from Excel
```kotlin
fun readProductsFromExcel(fileName: String): List<Product> {
    return NinjaExcel.read(fileName, Product::class.java)
}
```

## Generated Files

After running the application, check the `output/` directory for:

- `products.xlsx` - Complete product inventory
- `students.xlsx` - Student records
- `employees.xlsx` - Employee data
- `low_stock_products.xlsx` - Filtered low stock items
- `honor_students.xlsx` - Students with GPA ≥ 3.5
- `scholarship_students.xlsx` - Students with scholarships

## Configuration

The application uses minimal configuration in `application.yml`:
```yaml
server:
port: 8080

spring:
application:
name: excel-ninja-sample

devtools:
restart:
enabled: true

logging:
level:
com.excelninja.sample: DEBUG
com.excelninja: DEBUG
```

## Sample Data

### Products
- MacBook Pro M3, iPhone 15 Pro, AirPods Pro, Magic Mouse, iPad Air

### Students
- Computer Science, Mechanical Engineering, Biology majors
- GPA tracking and scholarship status

### Employees
- Various departments: Card, UI/UX, BRM, Remittance
- Salary and hire date information

## Key Features Demonstrated

1. **Custom Column Headers**: Map object fields to specific Excel column names
2. **Column Ordering**: Control the order of columns in generated Excel files
3. **Data Filtering**: Filter data based on business rules (low stock, honor students, etc.)
4. **Statistics Calculation**: Generate summary statistics from Excel data
5. **Error Handling**: Robust error handling with detailed logging
6. **Performance Monitoring**: Execution time tracking for operations

## Building

# Build the project
./gradlew build

# Run tests
./gradlew test

# Generate JAR
./gradlew bootJar

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is a sample/demo application for educational purposes.

## Resources

- [ExcelNinja Library](https://github.com/excel-ninja/excelNinja)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kotlin Documentation](https://kotlinlang.org/docs/)