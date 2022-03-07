package io.rently.userservice.persistency;

import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.errors.enums.Errors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlMapper {

    private static Field ;

    // Make use of supplier instead of reflection
    public static <T> T mapResultSetToObject(Class<T> dto, ResultSet resultSet) {
        Constructor<T> dtoConstructor;
        try {
            dtoConstructor = dto.getConstructor();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        T persistentObj;
        try {
            persistentObj = dtoConstructor.newInstance();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        for (Field field: persistentObj.getClass().getDeclaredFields()) {
            PersistentField persistentField = field.getDeclaredAnnotation(PersistentField.class);
            if (persistentField != null) {
                field.setAccessible(true);

                try {
                    field.set(persistentObj, resultSet.getString(persistentField.name()));
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    throw Errors.INTERNAL_SERVER_ERROR.getException();
                }
            }
        }

        return persistentObj;
    }
}
