package pl.iseebugs.TripReimbursementApp.logic;

public class UserGroupNotFoundException extends Exception {
    public UserGroupNotFoundException() {
        super("User Group not found.");
    }
    public UserGroupNotFoundException(String message) {
        super(message);
    }
}

