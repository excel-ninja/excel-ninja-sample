package com.excelninja.sample.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SampleDemoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SampleDemoRunner.class);
    private final EmployeeExcelService excelService;
    private final List<String> createdFiles = new ArrayList<>();

    public SampleDemoRunner(EmployeeExcelService excelService) {
        this.excelService = excelService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("ExcelNinja Sample Application Started!");
        logger.info("=====================================");

        try {
            createOutputDirectory();

            runExcelWriteDemo();
            runExcelReadDemo();
            runDepartmentFilterDemo();
            runSalaryAnalysisDemo();

            logger.info("=====================================");
            logger.info("All ExcelNinja demos completed successfully!");

        } catch (Exception e) {
            logger.error("Error during demo execution: {}", e.getMessage(), e);
        } finally {
            cleanupFiles();
        }
    }

    private void createOutputDirectory() {
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
            logger.info("Created output directory: {}", outputDir.getAbsolutePath());
        }
    }

    private void saveFileAndTrack(String filePath, Runnable saveAction) {
        try {
            saveAction.run();
            createdFiles.add(filePath);
            logger.info("File created and tracked: {}", filePath);
        } catch (Exception e) {
            logger.error("Failed to create file: {}", filePath, e);
        }
    }

    private void runExcelWriteDemo() {
        logger.info("\n=== Excel Write Demo (BigDecimal Support) ===");

        LocalDateTime now = LocalDateTime.now();
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Hyunsoo", "Card", new BigDecimal("85000.00"), LocalDate.of(2020, 3, 15), now),
                new Employee(2L, "Eunmi", "UI/UX", new BigDecimal("72000.50"), LocalDate.of(2019, 7, 20), now),
                new Employee(3L, "Changhee", "BRM", new BigDecimal("80000.00"), LocalDate.of(2021, 1, 10), now),
                new Employee(4L, "Wanjoo", "BRM", new BigDecimal("92000.75"), LocalDate.of(2018, 11, 5), now),
                new Employee(5L, "Daejoon", "Remittance", new BigDecimal("65000.00"), LocalDate.of(2022, 6, 1), now),
                new Employee(6L, "Ilchan", "Card", new BigDecimal("88000.25"), LocalDate.of(2020, 9, 23), now),
                new Employee(7L, "Jonghyun", "Card", new BigDecimal("70000.00"), LocalDate.of(2023, 2, 14), now)
        );

        String employeesFile = "output/employees.xlsx";
        saveFileAndTrack(employeesFile, () ->
                excelService.saveEmployeesToExcel(employees, employeesFile)
        );

        logger.info("Sample employee data saved successfully!");
        employees.forEach(emp -> logger.info("  - {}", emp));
    }

    private void runExcelReadDemo() {
        logger.info("\n=== Excel Read Demo ===");

        List<Employee> readEmployees = excelService.readEmployeesFromExcel("output/employees.xlsx");

        BigDecimal totalAnnualExpense = excelService.getCompanyTotalSalaryExpense(readEmployees);

        logger.info("Employee data read from Excel:");
        logger.info("  - Total employees: {}", readEmployees.size());
        logger.info("  - Total annual salary expense: {}", totalAnnualExpense.longValue());

        BigDecimal avgSalary = readEmployees.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(readEmployees.size()), 2, java.math.RoundingMode.HALF_UP);

        logger.info("  - Average monthly salary: ${}", avgSalary.longValue());
    }

    private void runDepartmentFilterDemo() {
        logger.info("\n=== Department Filter Demo ===");

        List<Employee> allEmployees = excelService.readEmployeesFromExcel("output/employees.xlsx");

        String cardTeamFile = "output/card_team.xlsx";
        String brmTeamFile = "output/brm_team.xlsx";

        saveFileAndTrack(cardTeamFile, () ->
                excelService.saveEmployeesByDepartment(allEmployees, "Card", cardTeamFile)
        );

        saveFileAndTrack(brmTeamFile, () ->
                excelService.saveEmployeesByDepartment(allEmployees, "BRM", brmTeamFile)
        );

        Map<String, Object> deptStats = excelService.getDepartmentStatistics(allEmployees);
        logger.info("Department statistics:");
        deptStats.forEach((dept, stats) -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> statMap = (Map<String, Object>) stats;
            logger.info("  - {}: {} employees (Avg salary: ${}, Senior count: {})",
                    dept,
                    statMap.get("count"),
                    ((BigDecimal) statMap.get("averageSalary")).longValue(),
                    statMap.get("seniorCount")
            );
        });
    }

    private void runSalaryAnalysisDemo() {
        logger.info("\n=== Salary Analysis Demo (BigDecimal Precision) ===");

        List<Employee> allEmployees = excelService.readEmployeesFromExcel("output/employees.xlsx");

        BigDecimal highSalaryThreshold = new BigDecimal("80000");
        List<Employee> highSalaryEmployees = excelService.getHighSalaryEmployees(allEmployees, highSalaryThreshold);

        logger.info("High salary employees (>${}):", highSalaryThreshold.longValue());
        highSalaryEmployees.forEach(emp ->
                logger.info("  - {} ({}): {} - {} years of service",
                        emp.getName(),
                        emp.getDepartment(),
                        emp.getFormattedSalary(),
                        emp.getServiceYears()
                )
        );

        String seniorEmployeesFile = "output/senior_employees.xlsx";
        String midLevelEmployeesFile = "output/mid_level_employees.xlsx";

        saveFileAndTrack(seniorEmployeesFile, () ->
                excelService.saveEmployeesBySalaryGrade(allEmployees, "Senior", seniorEmployeesFile)
        );

        saveFileAndTrack(midLevelEmployeesFile, () ->
                excelService.saveEmployeesBySalaryGrade(allEmployees, "Mid", midLevelEmployeesFile)
        );

        logger.info("Salary grade distribution:");
        long seniorCount = allEmployees.stream().filter(emp -> "Senior".equals(emp.getSalaryGrade())).count();
        long midCount = allEmployees.stream().filter(emp -> "Mid".equals(emp.getSalaryGrade())).count();
        long juniorCount = allEmployees.stream().filter(emp -> "Junior".equals(emp.getSalaryGrade())).count();

        logger.info("  - Senior level: {} employees", seniorCount);
        logger.info("  - Mid level: {} employees", midCount);
        logger.info("  - Junior level: {} employees", juniorCount);

        BigDecimal totalAnnualExpense = excelService.getCompanyTotalSalaryExpense(allEmployees);
        BigDecimal avgAnnualSalary = totalAnnualExpense.divide(new BigDecimal(allEmployees.size()), 2, java.math.RoundingMode.HALF_UP);

        logger.info("Company Financial Overview:");
        logger.info("  - Total annual salary expense: {}", totalAnnualExpense.longValue());
        logger.info("  - Average annual salary: {}", avgAnnualSalary.longValue());
        logger.info("  - Monthly payroll: {}", totalAnnualExpense.divide(new BigDecimal(12), 2, java.math.RoundingMode.HALF_UP).longValue());
    }

    private void cleanupFiles() {
        logger.info("\n=== Cleaning up generated files ===");

        int deletedCount = 0;
        int failedCount = 0;

        for (String filePath : createdFiles) {
            try {
                File file = new File(filePath);
                if (file.exists() && file.delete()) {
                    logger.info("Deleted: {}", filePath);
                    deletedCount++;
                } else {
                    logger.warn("File not found or failed to delete: {}", filePath);
                    failedCount++;
                }
            } catch (Exception e) {
                logger.error("Error deleting file {}: {}", filePath, e.getMessage());
                failedCount++;
            }
        }

        cleanupOutputDirectory();

        logger.info("Cleanup completed - Deleted: {}, Failed: {}", deletedCount, failedCount);
    }

    private void cleanupOutputDirectory() {
        try {
            File outputDir = new File("output");
            if (outputDir.exists() && outputDir.isDirectory()) {
                File[] files = outputDir.listFiles();
                if (files == null || files.length == 0) {
                    if (outputDir.delete()) {
                        logger.info("Deleted empty output directory");
                    }
                } else {
                    logger.info("Output directory not empty, keeping it (remaining files: {})", files.length);
                }
            }
        } catch (Exception e) {
            logger.error("Error cleaning up output directory: {}", e.getMessage());
        }
    }
}