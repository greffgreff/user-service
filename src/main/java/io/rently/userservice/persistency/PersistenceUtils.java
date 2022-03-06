package io.rently.userservice.persistency;

import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.annotations.PersistentObjectId;

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
        return persistentFields;
    }

    public static String getObjectName(Class<?> persistentObj) {
        return persistentObj.getDeclaredAnnotation(PersistentObject.class).name();
    }

    public static String getObjectId(Class<?> persistentObj) {
        return persistentObj.getDeclaredAnnotation(PersistentObjectId.class).name();
    }
}
