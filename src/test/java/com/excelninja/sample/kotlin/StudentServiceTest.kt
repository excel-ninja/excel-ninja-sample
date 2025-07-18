package com.excelninja.sample.kotlin

import com.excelNinja.sample.Student
import com.excelNinja.sample.StudentService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.*
import java.io.File
import kotlin.math.ceil

@DisplayName("학생 서비스 테스트")
class StudentServiceTest {

    private lateinit var studentService: StudentService
    private val testFileName = "test_students.xlsx"
    private val outputDir = "test_output"

    @BeforeEach
    fun setUp() {
        studentService = StudentService()
        File(outputDir).mkdirs()
    }

    @AfterEach
    fun tearDown() {
        File("$outputDir/$testFileName").delete()
        File(outputDir).deleteRecursively()
    }

    @Test
    @DisplayName("학생 목록을 엑셀 파일로 저장할 수 있다")
    fun saveStudentsToExcel() {
        val students = createTestStudents()
        val fileName = "$outputDir/$testFileName"

        studentService.saveStudentsToExcel(students, fileName)

        assertThat(File(fileName)).exists()
        assertThat(File(fileName).length()).isGreaterThan(0)
    }

    @Test
    @DisplayName("엑셀 파일에서 학생 목록을 읽을 수 있다")
    fun readStudentsFromExcel() {
        val originalStudents = createTestStudents()
        val fileName = "$outputDir/$testFileName"
        
        studentService.saveStudentsToExcel(originalStudents, fileName)
        val readStudents = studentService.readStudentsFromExcel(fileName)

        assertThat(readStudents).hasSize(originalStudents.size)
        assertThat(readStudents[0].name).isEqualTo("현수")
        assertThat(readStudents[0].gpa).isEqualTo(3.8)
        assertThat(readStudents[0].major).isEqualTo("Computer Science")
    }

    @Test
    @DisplayName("전공별로 학생을 필터링할 수 있다")
    fun getStudentsByMajor() {
        val students = createTestStudents()
        
        val csStudents = studentService.getStudentsByMajor(students, "Computer Science")

        assertThat(csStudents).hasSize(2)
    }

    @Test
    @DisplayName("우등생 목록을 조회할 수 있다")
    fun getHonorStudents() {
        val students = createTestStudents()
        
        val honorStudents = studentService.getHonorStudents(students)

        assertThat(honorStudents).hasSize(3)
        assertThat(honorStudents).allMatch { it.gpa >= 3.5 }
    }

    @Test
    @DisplayName("장학생 목록을 조회할 수 있다")
    fun getScholarshipStudents() {
        val students = createTestStudents()
        
        val scholarshipStudents = studentService.getScholarshipStudents(students)

        assertThat(scholarshipStudents).hasSize(3)
        assertThat(scholarshipStudents).allMatch { it.hasScholarship }
    }

    @Test
    @DisplayName("학년별로 학생을 필터링할 수 있다")
    fun getStudentsByGrade() {
        val students = createTestStudents()
        
        val juniors = studentService.getStudentsByGrade(students, 3)

        assertThat(juniors).hasSize(2)
        assertThat(juniors).allMatch { it.grade == 3 }
    }

    @Test
    @DisplayName("전공별 통계를 계산할 수 있다")
    fun getMajorStatistics() {
        val students = createTestStudents()
        
        val majorStats = studentService.getMajorStatistics(students)

        assertThat(majorStats).hasSize(3)
        assertThat(majorStats["Computer Science"]).isNotNull
        
        val csStats = majorStats["Computer Science"]!!
        assertThat(csStats["count"]).isEqualTo(2)
        assertThat(csStats["averageGPA"] as Double).isCloseTo(3.85, within(0.0001))
        assertThat(csStats["honorStudents"]).isEqualTo(2)
        assertThat(csStats["scholarshipStudents"]).isEqualTo(2)
    }

    @Test
    @DisplayName("존재하지 않는 파일을 읽으려 하면 예외가 발생한다")
    fun readNonExistentFile() {
        assertThrows<RuntimeException> {
            studentService.readStudentsFromExcel("nonexistent.xlsx")
        }
    }

    @Test
    @DisplayName("빈 학생 목록의 전공별 통계를 계산할 수 있다")
    fun getEmptyMajorStatistics() {
        val emptyStudents = emptyList<Student>()
        
        val majorStats = studentService.getMajorStatistics(emptyStudents)

        assertThat(majorStats).isEmpty()
    }

    private fun createTestStudents(): List<Student> {
        return listOf(
            Student(
                studentId = "CS001",
                name = "현수",
                email = "hyunsoo@university.edu",
                major = "Computer Science",
                grade = 3,
                gpa = 3.8,
                hasScholarship = true
            ),
            Student(
                studentId = "ME002",
                name = "은미",
                email = "eunmi@university.edu",
                major = "Mechanical Engineering",
                grade = 2,
                gpa = 3.2,
                hasScholarship = false
            ),
            Student(
                studentId = "CS003",
                name = "창희",
                email = "changhee@university.edu",
                major = "Computer Science",
                grade = 4,
                gpa = 3.9,
                hasScholarship = true
            ),
            Student(
                studentId = "BIO004",
                name = "완주",
                email = "wanjoo@university.edu",
                major = "Biology",
                grade = 1,
                gpa = 3.6,
                hasScholarship = false
            ),
            Student(
                studentId = "ME005",
                name = "대준",
                email = "daejoon@university.edu",
                major = "Mechanical Engineering",
                grade = 3,
                gpa = 3.4,
                hasScholarship = true
            )
        )
    }
}