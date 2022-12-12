package ua.yakubovskiy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.yakubovskiy.entity.Employee;
import ua.yakubovskiy.exception.PropertyLoadingException;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadPropertiesTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
    }

    @Test
    void whenObjectIsNull(){
        assertThrows(IllegalArgumentException.class, () ->
                LoadProperties.loadFromProperties(null, Employee.class,
                        "prop.properties"));
    }

    @Test
    void whenValueNameIsEmpty(){
        assertThrows(IllegalArgumentException.class, () ->
                LoadProperties.loadFromProperties(employee, Employee.class,
                        "propTestWhenValueNameIsEmpty.properties"));
    }

    @Test
    void whenUnableCastValueToInt(){
        assertThrows(NumberFormatException.class, () ->
                LoadProperties.loadFromProperties(employee, Employee.class,
                        "propTestWhenUnableCastValueToInt.properties"));
    }

    @Test
    void whenUnableCastValueToDate(){
        assertThrows(DateTimeParseException.class, () ->
                LoadProperties.loadFromProperties(employee, Employee.class,
                        "propTestWhenUnableCastValueToDate.properties"));
    }

    @Test
    void whenPropertyIsCorrect(){
        LoadProperties.loadFromProperties(employee, Employee.class,
                "prop.properties");
        assertEquals("Employee{name='Mike', salary=2500, employmentDate=2022-11-29T18:30:00Z}",
                employee.toString());
    }
}