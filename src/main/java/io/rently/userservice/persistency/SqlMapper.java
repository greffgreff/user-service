package io.rently.userservice.persistency;

import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.util.Broadcaster;
import io.rently.userservice.util.Util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SqlMapper {

    public static <T> T mapResultSetToObject(T persistentObj, HashMap<String, String> data) throws Exception {
        for (Field field: persistentObj.getClass().getDeclaredFields()) {
            PersistentField persistentField = field.getDeclaredAnnotation(PersistentField.class);

            if (persistentField != null) {
                for (Map.Entry<String, String> pair : data.entrySet()) {
                    String name = Util.getNonNull(persistentField.name(), field.getName()); // <-- implement this

                    if (Objects.equals(pair.getKey(), persistentField.name())) {
                        try {
                            field.setAccessible(true);
                            if (field.getType() == Timestamp.class) field.set(persistentObj, new Timestamp(Long.parseLong(pair.getValue())));
                            else field.set(persistentObj, pair.getValue());
                        }
                        catch (Exception ex) {
                            Broadcaster.warn("Could not parse property { " + field.getName() + " }: " + ex.getMessage());
                        }
                    }
                }
            }
        }

        return persistentObj;
    }
}
