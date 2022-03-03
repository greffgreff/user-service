package io.rently.userservice.annotations;

import com.fasterxml.jackson.annotation.JsonInclude;

public @interface Jsonable {
    JsonInclude include() default @JsonInclude(JsonInclude.Include.NON_NULL);
}
