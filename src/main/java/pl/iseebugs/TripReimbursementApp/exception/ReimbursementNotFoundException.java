package pl.iseebugs.TripReimbursementApp.exception;

public class ReimbursementNotFoundException extends Exception {
    public ReimbursementNotFoundException() {
        super("Reimbursement not found.");
    }
    public ReimbursementNotFoundException(String message) {
        super(message);
    }
}

