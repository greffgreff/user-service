package io.rently.userservice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface SqlEntity {

    String tableName();
}
