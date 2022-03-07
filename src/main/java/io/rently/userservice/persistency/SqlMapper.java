package io.rently.userservice.persistency;

import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.errors.enums.Errors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SqlMapper {

    // Make use of supplier instead of reflection
    public static <T> T mapResultSetToObject(Class<T> dto, HashMap<String, String> data) {
        T persistentObj = getObjectFromClass(dto);

        for (Field field: persistentObj.getClass().getDeclaredFields()) {
            PersistentField persistentField = field.getDeclaredAnnotation(PersistentField.class);
            if (persistentField != null) {
                field.setAccessible(true);

                try {
                    for (Map.Entry<String, String> pair : data.entrySet()) {
                        if (Objects.equals(pair.getKey(), persistentField.name())) {
                            System.out.println(pair);
                            field.set(persistentObj, pair.getValue());
                        }
                    }
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Setting field error");
//                    throw Errors.INTERNAL_SERVER_ERROR.getException();
                }
                finally {
                    try { field.set(persistentObj, null); }
                    catch (Exception ignore) { }
                }
            }
        }

        return persistentObj;
    }

    private static <T> T getObjectFromClass(Class<T> obj) {
        Constructor<T> dtoConstructor;
        try {
            dtoConstructor = obj.getConstructor();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        T newObj;
        try {
            newObj = dtoConstructor.newInstance();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        return newObj;
    }
}
