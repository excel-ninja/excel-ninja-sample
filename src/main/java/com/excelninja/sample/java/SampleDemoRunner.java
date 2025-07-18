package com.excelninja.sample.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class SampleDemoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SampleDemoRunner.class);
    private final EmployeeExcelService excelService;

    public SampleDemoRunner(EmployeeExcelService excelService) {
        this.excelService = excelService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("ExcelNinja Sample Application Started!");
        logger.info("=====================================");

        try {
            runExcelWriteDemo();
            runExcelReadDemo();
            runDepartmentFilterDemo();

            logger.info("=====================================");
            logger.info("All ExcelNinja demos completed successfully!");

        } catch (Exception e) {
            logger.error("Error during demo execution: {}", e.getMessage(), e);
        }
    }

    private void runExcelWriteDemo() {
        logger.info("\n=== Excel Write Demo ===");

        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Hyunsoo", "Card", 85000, LocalDate.of(2020, 3, 15)),
                new Employee(2L, "Eunmi", "UI/UX", 72000, LocalDate.of(2019, 7, 20)),
                new Employee(3L, "Changhee", "BRM", 8000, LocalDate.of(2021, 1, 10)),
                new Employee(4L, "Wanjoo", "BRM", 92000, LocalDate.of(2018, 11, 5)),
                new Employee(5L, "Daejoon", "Remittance", 65000, LocalDate.of(2022, 6, 1)),
                new Employee(6L, "Ilchan", "Card", 88000, LocalDate.of(2020, 9, 23)),
                new Employee(7L, "Jonghyun", "Card", 70000, LocalDate.of(2023, 2, 14))
        );

        excelService.saveEmployeesToExcel(employees, "output/employees.xlsx");

        logger.info("Sample employee data saved successfully!");
        employees.forEach(emp -> logger.info("  - {}", emp));
    }

    private void runExcelReadDemo() {
        logger.info("\n=== Excel Read Demo ===");

        List<Employee> readEmployees = excelService.readEmployeesFromExcel("output/employees.xlsx");

        logger.info("Employee data read from Excel:");

        double avgSalary = readEmployees.stream()
                .mapToInt(Employee::getSalary)
                .average()
                .orElse(0);

    }

    private void runDepartmentFilterDemo() {
        logger.info("\n=== Department Filter Demo ===");

        List<Employee> allEmployees = excelService.readEmployeesFromExcel("output/employees.xlsx");

        excelService.saveEmployeesByDepartment(allEmployees, "Development", "dev_team.xlsx");
        excelService.saveEmployeesByDepartment(allEmployees, "Marketing", "marketing_team.xlsx");

        logger.info("Department statistics:");
        allEmployees.stream()
                .collect(java.util.stream.Collectors.groupingBy(Employee::getDepartment))
                .forEach((dept, empList) -> {
                    double deptAvgSalary = empList.stream()
                            .mapToInt(Employee::getSalary)
                            .average()
                            .orElse(0);
                    logger.info("  - {}: {} employees (Avg salary: {})", dept, empList.size(), String.format("$%,d", Math.round(deptAvgSalary)));
                });
    }
}