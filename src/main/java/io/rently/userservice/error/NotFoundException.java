package io.rently.userservice.error;

public class NotFoundException extends RuntimeException {
    public static String MESSAGE = "Resource could not be found";

    public NotFoundException() {
        this(MESSAGE);
    }

    public NotFoundException(String description) {
        super(String.format("%s. %s", MESSAGE, description));
    }

    public static class UserByIdNotFound extends NotFoundException {

        public UserByIdNotFound(String id) {
            super("Could not find user with specified ID { " + id + " }");
        }
    }

    public static class UserByEmailNotFound extends NotFoundException {

        public UserByEmailNotFound(String email) {
            super("Could not find user with specified email { " + email + " }");
        }
    }
}
