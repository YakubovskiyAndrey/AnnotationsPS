package ua.yakubovskiy.entity;

import ua.yakubovskiy.annotation.PropertyConfiguration;
import ua.yakubovskiy.annotation.PropertyElement;
import java.time.Instant;

@PropertyConfiguration
public class Employee {

    @PropertyElement(name = "employeeName")
    private String name;

    @PropertyElement(name = "employeeSalary")
    private int salary;

    private Instant employmentDate;

    public Employee() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setEmploymentDate(Instant employmentDate) {
        this.employmentDate = employmentDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", employmentDate=" + employmentDate +
                '}';
    }
}
