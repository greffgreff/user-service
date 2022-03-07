package io.rently.userservice.persistency;

import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.errors.enums.Errors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SqlMapper {

    // Make use of supplier instead of reflection
    public static <T> T mapResultSetToObject(Class<T> dto, HashMap<String, String> data) throws Exception {
        Constructor<T> dtoConstructor = dto.getConstructor();
        T persistentObj = dtoConstructor.newInstance();

        for (Field field: persistentObj.getClass().getDeclaredFields()) {
            PersistentField persistentField = field.getDeclaredAnnotation(PersistentField.class);
            if (persistentField != null) {
                for (Map.Entry<String, String> pair : data.entrySet()) {
                    if (Objects.equals(pair.getKey(), persistentField.name())) {
                        field.setAccessible(true);
                        field.set(persistentObj, pair.getValue());
                    }
                }
            }
        }

        return persistentObj;
    }
}
