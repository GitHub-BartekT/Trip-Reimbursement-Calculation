package pl.iseebugs.TripReimbursementApp.exception;

public class UserCostNotFoundException extends Exception {
    public UserCostNotFoundException() {
        super("User Cost not found.");
    }
    public UserCostNotFoundException(String message) {
        super(message);
    }
}
