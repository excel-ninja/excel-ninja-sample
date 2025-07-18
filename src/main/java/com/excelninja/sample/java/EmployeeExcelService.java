package com.excelninja.sample.java;

import com.excelninja.application.facade.NinjaExcel;
import com.excelninja.domain.model.ExcelDocument;
import org.springframework.stereotype.Service;

import java.util.List;

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
}