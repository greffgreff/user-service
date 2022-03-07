package io.rently.userservice.persistency;

import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.annotations.PersistentKeyField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class PersistenceUtils {

    public static ArrayList<Field> getPersistentFields(Class<?> persistentObj) {
        ArrayList<Field> persistentFields = new ArrayList<>();
        for (Field field : persistentObj.getDeclaredFields()) {
            if (Arrays.stream(field.getAnnotations()).anyMatch(a -> a instanceof PersistentField)) {
                persistentFields.add(field);
            }
        }
//        for (Field f: MyClass.class.getFields()) {
//            Column column = f.getAnnotation(Column.class);
//            if (column != null)
//                System.out.println(column.columnName());
//        }
        return persistentFields;
    }

    public static String getObjectName(Class<?> persistentObj) {
        return persistentObj.getDeclaredAnnotation(PersistentObject.class).name();
    }

    public static String getObjectId(Class<?> persistentObj) {
        return persistentObj.getDeclaredAnnotation(PersistentKeyField.class).name();
    }
}
