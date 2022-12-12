package ua.yakubovskiy;

import ua.yakubovskiy.annotation.PropertyConfiguration;
import ua.yakubovskiy.annotation.PropertyElement;
import ua.yakubovskiy.exception.PropertyLoadingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class LoadProperties {

    private LoadProperties() {}

    public static void loadFromProperties(Object object, Class<?> cls, String propertiesName) {
        checkIfAnnotated(object, cls);
        try {
            Properties properties = new Properties();
            properties.load(LoadProperties.class.getClassLoader().getResourceAsStream(propertiesName));
            for (Field field : cls.getDeclaredFields()) {
                if (field.isAnnotationPresent(PropertyElement.class)) {
                    field.setAccessible(true);
                    field.set(object, castObject(field, properties));
                }
            }
        } catch (IllegalAccessException | IOException ex){
            ex.printStackTrace();
        }
    }

    private static Object castObject(Field field, Properties properties) {
        Object result;

        String valueAnnotationName = properties.getProperty(field.getAnnotation(PropertyElement.class).name());
        String valueName;
        if(!"".equals(valueAnnotationName) && valueAnnotationName != null) {
            valueName = valueAnnotationName;
        }else {
            valueName = properties.getProperty(field.getName());
        }
        if(valueName == null) throw new PropertyLoadingException("\"valueName\" must not be empty");

        String valueFormat = field.getAnnotation(PropertyElement.class).format();
        Class<?> fieldType = field.getType();

        if(fieldType == Integer.TYPE){
            try{
                result = Integer.parseInt(valueName.trim());
            } catch (NumberFormatException ex){
                throw new NumberFormatException("Unable to cast value to type " + Integer.TYPE.getTypeName());
            }
        }else if(fieldType.isAssignableFrom(Integer.class)) {
            try {
                result = Integer.valueOf(valueName.trim());
            } catch (NumberFormatException ex){
                throw new NumberFormatException("Unable to cast value to type " + Integer.class.getTypeName());
            }
        }else if(fieldType.isAssignableFrom(String.class)) {
            result = valueName.trim();
        }else if(fieldType.isAssignableFrom(Instant.class)) {
            if(!"".equals(valueFormat)){
                DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                        .appendPattern(valueFormat.trim())
                        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                        .toFormatter(Locale.ROOT)
                        .withZone(ZoneOffset.UTC);
                try {
                    result = dateTimeFormatter.parse(valueName.trim(), Instant::from);
                }catch (DateTimeParseException ex){
                    throw new DateTimeParseException("Unable to cast value to date", valueName.trim(), 0);
                }
            }else {
                throw new PropertyLoadingException("Date format not specified");
            }
        }else {
            throw new PropertyLoadingException("Type not supported");
        }
        return result;
    }

    private static void checkIfAnnotated(Object object, Class<?> cls) {
        if (Objects.isNull(object)) throw new PropertyLoadingException("The object is null");

        if (!cls.isAnnotationPresent(PropertyConfiguration.class)) {
            throw new PropertyLoadingException("The class "
                    + cls.getSimpleName()
                    + " is not annotated with PropertyConfiguration");
        }
    }
}
