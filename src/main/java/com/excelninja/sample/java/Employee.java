package com.excelninja.sample.java;

import com.excelninja.domain.annotation.ExcelReadColumn;
import com.excelninja.domain.annotation.ExcelWriteColumn;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {

    @ExcelReadColumn(headerName = "Employee ID")
    @ExcelWriteColumn(headerName = "Employee ID", order = 1)
    private Long id;

    @ExcelReadColumn(headerName = "Name")
    @ExcelWriteColumn(headerName = "Name", order = 2)
    private String name;

    @ExcelReadColumn(headerName = "Department")
    @ExcelWriteColumn(headerName = "Department", order = 3)
    private String department;

    @ExcelReadColumn(headerName = "Salary")
    @ExcelWriteColumn(headerName = "Salary", order = 4)
    private Integer salary;

    @ExcelReadColumn(headerName = "Hire Date")
    @ExcelWriteColumn(headerName = "Hire Date", order = 5)
    private LocalDate hireDate;

    public Employee() {}

    public Employee(
            Long id,
            String name,
            String department,
            Integer salary,
            LocalDate hireDate
    ) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    // Getter & Setter
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getDepartment() {return department;}

    public void setDepartment(String department) {this.department = department;}

    public Integer getSalary() {return salary;}

    public void setSalary(Integer salary) {this.salary = salary;}

    public LocalDate getHireDate() {return hireDate;}

    public void setHireDate(LocalDate hireDate) {this.hireDate = hireDate;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', department='%s', salary=%,d, hireDate=%s}", id, name, department, salary, hireDate);
    }
}