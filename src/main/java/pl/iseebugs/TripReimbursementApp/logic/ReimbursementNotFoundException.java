package pl.iseebugs.TripReimbursementApp.logic;

public class ReimbursementNotFoundException extends Exception {
    public ReimbursementNotFoundException() {
        super("Reimbursement not found.");
    }
    public ReimbursementNotFoundException(String message) {
        super(message);
    }
}

