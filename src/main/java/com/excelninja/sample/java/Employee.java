package com.excelninja.sample.java;

import com.excelninja.domain.annotation.ExcelReadColumn;
import com.excelninja.domain.annotation.ExcelWriteColumn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private BigDecimal salary;

    @ExcelReadColumn(headerName = "Hire Date")
    @ExcelWriteColumn(headerName = "Hire Date", order = 5)
    private LocalDate hireDate;

    @ExcelReadColumn(headerName = "Last Updated")
    @ExcelWriteColumn(headerName = "Last Updated", order = 6)
    private LocalDateTime lastUpdated;

    public Employee() {}

    public Employee(
            Long id,
            String name,
            String department,
            BigDecimal salary,
            LocalDate hireDate,
            LocalDateTime lastUpdated
    ) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getAnnualSalary() {
        return salary.multiply(BigDecimal.valueOf(12));
    }

    public String getSalaryGrade() {
        if (salary.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            return "Senior";
        } else if (salary.compareTo(BigDecimal.valueOf(70000)) >= 0) {
            return "Mid";
        } else {
            return "Junior";
        }
    }

    public long getServiceYears() {
        return java.time.Period.between(hireDate, LocalDate.now()).getYears();
    }

    public String getFormattedSalary() {
        return String.format("$%,d", salary.longValue());
    }

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public BigDecimal getSalary() {return salary;}
    public void setSalary(BigDecimal salary) {this.salary = salary;}

    public LocalDate getHireDate() {return hireDate;}
    public void setHireDate(LocalDate hireDate) {this.hireDate = hireDate;}

    public LocalDateTime getLastUpdated() {return lastUpdated;}
    public void setLastUpdated(LocalDateTime lastUpdated) {this.lastUpdated = lastUpdated;}

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
        return String.format(
                "Employee{id=%d, name='%s', department='%s', salary=%s, hireDate=%s, grade='%s', serviceYears=%d}",
                id, name, department, getFormattedSalary(), hireDate, getSalaryGrade(), getServiceYears()
        );
    }
}