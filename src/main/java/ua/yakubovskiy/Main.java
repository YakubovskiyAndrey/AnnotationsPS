package ua.yakubovskiy;


import ua.yakubovskiy.entity.Employee;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, IOException {
        Employee employee = new Employee();
        LoadProperties.loadFromProperties(employee, Employee.class, "prop.properties");
        System.out.println(employee);
    }
}