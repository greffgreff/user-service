package io.rently.userservice.persistency;

import io.rently.userservice.annotations.SqlEntity;
import io.rently.userservice.annotations.SqlField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class SqlUtil {

    public static ArrayList<Field> getSqlFields(Class<?> persistentObj) {
        ArrayList<Field> persistentFields = new ArrayList<>();
        for (Field field : persistentObj.getDeclaredFields()) {
            if (Arrays.stream(field.getAnnotations()).anyMatch(a -> a instanceof SqlField)) {
                persistentFields.add(field);
            }
        }
        return persistentFields;
    }

    public static String getSqlTableName(Class<?> persistentObj) {
        return persistentObj.getDeclaredAnnotation(SqlEntity.class).tableName();
    }
}
