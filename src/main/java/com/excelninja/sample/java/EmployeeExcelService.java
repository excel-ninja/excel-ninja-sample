package com.excelninja.sample.java;

import com.excelninja.application.facade.NinjaExcel;
import com.excelninja.domain.model.ExcelDocument;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeExcelService {

    public void saveEmployeesToExcel(
            List<Employee> employees,
            String fileName
    ) {
        try {
            ExcelDocument document = ExcelDocument.writer()
                    .objects(employees)
                    .sheetName("Employee List")
                    .columnWidth(1, 4000)
                    .columnWidth(2, 3000)
                    .columnWidth(3, 4000)
                    .columnWidth(5, 5000)
                    .create();

            NinjaExcel.write(document, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save employee Excel file", e);
        }
    }

    public List<Employee> readEmployeesFromExcel(String fileName) {
        try {
            return NinjaExcel.read(fileName, Employee.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read employee Excel file", e);
        }
    }

    public void saveEmployeesByDepartment(
            List<Employee> employees,
            String department,
            String fileName
    ) {
        List<Employee> filteredEmployees = employees.stream()
                .filter(emp -> department.equals(emp.getDepartment()))
                .toList();

        if (filteredEmployees.isEmpty()) {
            return;
        }
        saveEmployeesToExcel(filteredEmployees, fileName);
    }

    public void saveEmployeesBySalaryGrade(
            List<Employee> employees,
            String salaryGrade,
            String fileName
    ) {
        List<Employee> filteredEmployees = employees.stream()
                .filter(emp -> salaryGrade.equals(emp.getSalaryGrade()))
                .toList();

        if (filteredEmployees.isEmpty()) {
            return;
        }
        saveEmployeesToExcel(filteredEmployees, fileName);
    }

    public List<Employee> getHighSalaryEmployees(
            List<Employee> employees,
            BigDecimal threshold
    ) {
        return employees.stream()
                .filter(emp -> emp.getSalary().compareTo(threshold) >= 0)
                .toList();
    }

    public Map<String, Object> getDepartmentStatistics(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<Employee> deptEmployees = entry.getValue();
                            BigDecimal avgSalary = deptEmployees.stream()
                                    .map(Employee::getSalary)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(deptEmployees.size()), 2, RoundingMode.HALF_UP);

                            return Map.of(
                                    "count", deptEmployees.size(),
                                    "averageSalary", avgSalary,
                                    "totalSalary", deptEmployees.stream()
                                            .map(Employee::getSalary)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add),
                                    "seniorCount", deptEmployees.stream()
                                            .mapToInt(emp -> "Senior".equals(emp.getSalaryGrade()) ? 1 : 0)
                                            .sum()
                            );
                        }
                ));
    }

    public BigDecimal getCompanyTotalSalaryExpense(List<Employee> employees) {
        return employees.stream()
                .map(Employee::getAnnualSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}