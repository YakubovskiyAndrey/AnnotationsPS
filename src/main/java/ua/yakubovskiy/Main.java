package ua.yakubovskiy;

import ua.yakubovskiy.entity.Employee;

public class Main {
    public static void main(String[] args) {
        Employee employee = new Employee();
        LoadProperties.loadFromProperties(employee, Employee.class, "prop.properties");
        System.out.println(employee);
    }
}