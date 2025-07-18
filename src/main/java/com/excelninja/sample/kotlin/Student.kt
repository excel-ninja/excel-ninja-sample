package com.excelNinja.sample

import com.excelninja.domain.annotation.ExcelReadColumn
import com.excelninja.domain.annotation.ExcelWriteColumn

data class Student(
    @ExcelReadColumn(headerName = "Student ID")
    @ExcelWriteColumn(headerName = "Student ID", order = 1)
    var studentId: String = "",

    @ExcelReadColumn(headerName = "Name")
    @ExcelWriteColumn(headerName = "Name", order = 2)
    var name: String = "",

    @ExcelReadColumn(headerName = "Email")
    @ExcelWriteColumn(headerName = "Email", order = 3)
    var email: String = "",

    @ExcelReadColumn(headerName = "Major")
    @ExcelWriteColumn(headerName = "Major", order = 4)
    var major: String = "",

    @ExcelReadColumn(headerName = "Grade", defaultValue = "1")
    @ExcelWriteColumn(headerName = "Grade", order = 5)
    var grade: Int = 1,

    @ExcelReadColumn(headerName = "GPA", defaultValue = "0.0")
    @ExcelWriteColumn(headerName = "GPA", order = 6)
    var gpa: Double = 0.0,

    @ExcelReadColumn(headerName = "Has Scholarship")
    @ExcelWriteColumn(headerName = "Has Scholarship", order = 7)
    var hasScholarship: Boolean = false
) {

    fun getGradeLevel(): String = when (grade) {
        1 -> "Freshman"
        2 -> "Sophomore"
        3 -> "Junior"
        4 -> "Senior"
        else -> "Graduate"
    }

    fun isHonorStudent(): Boolean = gpa >= 3.5

    fun getEmailDomain(): String = email.substringAfter("@", "unknown")
}