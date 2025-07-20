package com.excelninja.sample.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("직원 모델 테스트")
public class EmployeeTest {

    @Test
    @DisplayName("직원 객체를 생성할 수 있다")
    void createEmployee() {
        Employee employee = new Employee(1L, "조현수", "개발팀", new BigDecimal(80000), LocalDate.of(2020, 1, 1), LocalDateTime.now());
        assertThat(employee.getId()).isEqualTo(1L);
        assertThat(employee.getName()).isEqualTo("조현수");
        assertThat(employee.getDepartment()).isEqualTo("개발팀");
        assertThat(employee.getSalary()).isEqualTo(new BigDecimal(80000));
        assertThat(employee.getHireDate()).isEqualTo(LocalDate.of(2020, 1, 1));
    }

    @Test
    @DisplayName("같은 ID를 가진 직원은 동일하다고 판단한다")
    void employeeEquality() {
        Employee employee1 = new Employee(1L, "조현수", "개발팀", new BigDecimal(80000), LocalDate.of(2020, 1, 1),LocalDateTime.now());
        Employee employee2 = new Employee(1L, "이은미", "디자인팀", new BigDecimal(75000), LocalDate.of(2021, 1, 1),LocalDateTime.now());
        assertThat(employee1).isEqualTo(employee2);
        assertThat(employee1.hashCode()).isEqualTo(employee2.hashCode());
    }

    @Test
    @DisplayName("toString 메서드가 올바른 형식을 반환한다")
    void toStringFormat() {
        Employee employee = new Employee(1L, "조현수", "개발팀", new BigDecimal(80000), LocalDate.of(2020, 1, 1),LocalDateTime.now());
        String result = employee.toString();
        assertThat(result).contains("Employee{");
        assertThat(result).contains("id=1");
        assertThat(result).contains("name='조현수'");
        assertThat(result).contains("department='개발팀'");
        assertThat(result).contains("salary=$80,000");
        assertThat(result).contains("hireDate=2020-01-01");
    }
}