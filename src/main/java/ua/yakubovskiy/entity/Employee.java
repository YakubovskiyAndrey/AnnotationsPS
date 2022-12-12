package ua.yakubovskiy.entity;

import ua.yakubovskiy.annotation.PropertyConfiguration;
import ua.yakubovskiy.annotation.PropertyElement;
import java.time.Instant;

@PropertyConfiguration
public class Employee {

    @PropertyElement
    private String name;

    @PropertyElement(name = "numberProperty")
    private int salary;

    @PropertyElement(name = "timeProperty", format = "dd.MM.yyyy HH:mm")
    private Instant employmentDate;

    public Employee() {
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
