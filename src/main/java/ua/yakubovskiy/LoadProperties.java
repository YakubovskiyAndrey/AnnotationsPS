package ua.yakubovskiy;

import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import ua.yakubovskiy.annotation.PropertyConfiguration;
import ua.yakubovskiy.annotation.PropertyElement;
import ua.yakubovskiy.exception.PropertyLoadingException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;


public class LoadProperties {

    public static void loadFromProperties(Object object, Class<?> cls, String propertiesName) throws IllegalAccessException, IOException {
        checkIfAnnotated(object);
        Properties properties = new Properties();
        InputStream is = LoadProperties.class.getClassLoader().getResourceAsStream(propertiesName);
        properties.load(is);
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(PropertyElement.class)) {
                field.setAccessible(true);
                field.set(object,
                        castObject(field.getType().getTypeName(), field.getType(),
                                properties.getProperty(field.getName())));
            }
        }
    }

    private static  <T> T castObject(String s, Class<T> to, Object from) {
        return (T) TypeConverters.convert(s, to, from);
    }

    private static void checkIfAnnotated(Object object) {
        if (Objects.isNull(object)) {
            throw new PropertyLoadingException("The object is null");
        }

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(PropertyConfiguration.class)) {
            throw new PropertyLoadingException("The class "
                    + clazz.getSimpleName()
                    + " is not annotated with PropertyConfiguration");
        }
    }
}
