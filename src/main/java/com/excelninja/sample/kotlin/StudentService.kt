package com.excelNinja.sample

import com.excelninja.application.facade.NinjaExcel
import com.excelninja.domain.model.ExcelWorkbook
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService {

    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun saveStudentsToExcel(students: List<Student>, fileName: String) {
        try {
            val startTime = System.currentTimeMillis()

            // Create output directory if it doesn't exist - more explicit approach
            val file = java.io.File(fileName)
            val parentDir = file.parentFile
            if (parentDir != null && !parentDir.exists()) {
                val created = parentDir.mkdirs()
                if (created) {
                    logger.info("Created directory: {}", parentDir.absolutePath)
                } else {
                    logger.warn("Failed to create directory: {}", parentDir.absolutePath)
                }
            }

            val document = ExcelWorkbook.builder().sheet("Student Records",students).build()
            NinjaExcel.write(document, fileName)

            val duration = System.currentTimeMillis() - startTime
            logger.info(
                "Saved {} students to {} (Duration: {}ms)",
                students.size, fileName, duration
            )

        } catch (e: Exception) {
            logger.error("Error saving students: {}", e.message)
            // Log more details for debugging
            val file = java.io.File(fileName)
            logger.error("File path: {}", file.absolutePath)
            logger.error("Parent directory exists: {}", file.parentFile?.exists())
            throw RuntimeException("Failed to save students Excel file", e)
        }
    }

    fun readStudentsFromExcel(fileName: String): List<Student> {
        return try {
            val startTime = System.currentTimeMillis()

            val students = NinjaExcel.read(fileName, Student::class.java)

            val duration = System.currentTimeMillis() - startTime
            logger.info(
                "Read {} students from {} (Duration: {}ms)",
                students.size, fileName, duration
            )

            students
        } catch (e: Exception) {
            logger.error("Error reading students: {}", e.message)
            throw RuntimeException("Failed to read students Excel file", e)
        }
    }

    fun getStudentsByMajor(students: List<Student>, major: String): List<Student> {
        return students.filter { it.major == major }
    }

    fun getHonorStudents(students: List<Student>): List<Student> {
        return students.filter { it.isHonorStudent() }
    }

    fun getScholarshipStudents(students: List<Student>): List<Student> {
        return students.filter { it.hasScholarship }
    }

    fun getStudentsByGrade(students: List<Student>, grade: Int): List<Student> {
        return students.filter { it.grade == grade }
    }

    fun getMajorStatistics(students: List<Student>): Map<String, Map<String, Any>> {
        return students.groupBy { it.major }.mapValues { (_, majorStudents) ->
            val averageGPA = if (majorStudents.isNotEmpty()) {
                majorStudents.sumOf { it.gpa } / majorStudents.size
            } else {
                0.0
            }

            mapOf(
                "count" to majorStudents.size,
                "averageGPA" to averageGPA,
                "honorStudents" to majorStudents.count { it.isHonorStudent() },
                "scholarshipStudents" to majorStudents.count { it.hasScholarship }
            )
        }
    }
}