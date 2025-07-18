package com.excelninja.sample.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("직원 엑셀 서비스 테스트")
public class EmployeeExcelServiceTest {

    private EmployeeExcelService employeeExcelService;
    private final String testFileName = "test_employees.xlsx";
    private final String outputDir = "test_output";

    @BeforeEach
    void setUp() {
        employeeExcelService = new EmployeeExcelService();
        new File(outputDir).mkdirs();
    }

    @AfterEach
    void tearDown() {
        new File(outputDir + "/" + testFileName).delete();
        deleteDirectory(new File(outputDir));
    }

    @Test
    @DisplayName("직원 목록을 엑셀 파일로 저장할 수 있다")
    void saveEmployeesToExcel() {
        List<Employee> employees = createTestEmployees();
        String fileName = outputDir + "/" + testFileName;

        employeeExcelService.saveEmployeesToExcel(employees, fileName);

        assertThat(new File(fileName)).exists();
        assertThat(new File(fileName).length()).isGreaterThan(0);
    }

    @Test
    @DisplayName("엑셀 파일에서 직원 목록을 읽을 수 있다")
    void readEmployeesFromExcel() {
        List<Employee> originalEmployees = createTestEmployees();
        String fileName = outputDir + "/" + testFileName;

        employeeExcelService.saveEmployeesToExcel(originalEmployees, fileName);
        List<Employee> readEmployees = employeeExcelService.readEmployeesFromExcel(fileName);

        assertThat(readEmployees).hasSize(originalEmployees.size());
        assertThat(readEmployees.get(0).getName()).isEqualTo("현수");
        assertThat(readEmployees.get(0).getDepartment()).isEqualTo("Card");
        assertThat(readEmployees.get(0).getSalary()).isEqualTo(85000);
    }

    @Test
    @DisplayName("부서별로 직원을 필터링하여 저장할 수 있다")
    void saveEmployeesByDepartment() {
        List<Employee> employees = createTestEmployees();
        String originalFileName = outputDir + "/" + testFileName;
        String departmentFileName = outputDir + "/card_employees.xlsx";

        employeeExcelService.saveEmployeesToExcel(employees, originalFileName);
        List<Employee> allEmployees = employeeExcelService.readEmployeesFromExcel(originalFileName);

        employeeExcelService.saveEmployeesByDepartment(allEmployees, "Card", departmentFileName);

        assertThat(new File(departmentFileName)).exists();

        List<Employee> cardEmployees = employeeExcelService.readEmployeesFromExcel(departmentFileName);
        assertThat(cardEmployees).hasSize(3);
        assertThat(cardEmployees).allMatch(emp -> "Card".equals(emp.getDepartment()));
    }

    @Test
    @DisplayName("존재하지 않는 부서로 필터링하면 파일이 생성되지 않는다")
    void saveEmployeesByNonExistentDepartment() {
        List<Employee> employees = createTestEmployees();
        String departmentFileName = outputDir + "/nonexistent_department.xlsx";

        employeeExcelService.saveEmployeesByDepartment(employees, "NonExistent", departmentFileName);

        assertThat(new File(departmentFileName)).doesNotExist();
    }

    @Test
    @DisplayName("존재하지 않는 파일을 읽으려 하면 예외가 발생한다")
    void readNonExistentFile() {
        assertThrows(RuntimeException.class, () -> {
            employeeExcelService.readEmployeesFromExcel("nonexistent.xlsx");
        });
    }

    private List<Employee> createTestEmployees() {
        return Arrays.asList(
                new Employee(1L, "현수", "Card", 85000, LocalDate.of(2020, 3, 15)),
                new Employee(2L, "은미", "UI/UX", 72000, LocalDate.of(2019, 7, 20)),
                new Employee(3L, "창희", "BRM", 80000, LocalDate.of(2021, 1, 10)),
                new Employee(4L, "완주", "BRM", 92000, LocalDate.of(2018, 11, 5)),
                new Employee(5L, "대준", "Remittance", 65000, LocalDate.of(2022, 6, 1)),
                new Employee(6L, "일찬", "Card", 88000, LocalDate.of(2020, 9, 23)),
                new Employee(7L, "종현", "Card", 70000, LocalDate.of(2023, 2, 14))
        );
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}