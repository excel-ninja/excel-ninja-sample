package com.excelninja.sample.kotlin

import com.excelNinja.sample.Student
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("학생 모델 테스트")
class StudentTest {

    @Test
    @DisplayName("학년별 레벨을 반환할 수 있다")
    fun getGradeLevel() {
        assertThat(Student(grade = 1).getGradeLevel()).isEqualTo("Freshman")
        assertThat(Student(grade = 2).getGradeLevel()).isEqualTo("Sophomore")
        assertThat(Student(grade = 3).getGradeLevel()).isEqualTo("Junior")
        assertThat(Student(grade = 4).getGradeLevel()).isEqualTo("Senior")
        assertThat(Student(grade = 5).getGradeLevel()).isEqualTo("Graduate")
    }

    @Test
    @DisplayName("GPA가 3.5 이상이면 우등생으로 판단한다")
    fun isHonorStudent() {
        val honorStudent = Student(gpa = 3.8)
        val normalStudent = Student(gpa = 3.2)

        assertThat(honorStudent.isHonorStudent()).isTrue()
        assertThat(normalStudent.isHonorStudent()).isFalse()
    }

    @Test
    @DisplayName("이메일 도메인을 추출할 수 있다")
    fun getEmailDomain() {
        val student = Student(email = "test@university.edu")
        val invalidEmailStudent = Student(email = "invalid-email")

        assertThat(student.getEmailDomain()).isEqualTo("university.edu")
        assertThat(invalidEmailStudent.getEmailDomain()).isEqualTo("unknown")
    }
}