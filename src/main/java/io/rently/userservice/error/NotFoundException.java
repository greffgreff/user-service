package io.rently.userservice.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        this("Resource could not be found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public static class UserByIdNotFound extends NotFoundException {

        public UserByIdNotFound(String id) {
            super("Could not find user with specified ID { id:" + id + " }");
        }
    }

    public static class UserByEmailNotFound extends NotFoundException {

        public UserByEmailNotFound(String email) {
            super("Could not find user with specified email { email:" + email + " }");
        }
    }
}
