package pl.iseebugs.TripReimbursementApp.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found.");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}

