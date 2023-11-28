package pl.iseebugs.TripReimbursementApp.logic;

public class ReceiptTypeNotFoundException extends Exception {
    public ReceiptTypeNotFoundException() {
        super("Receipt Type not found.");
    }
    public ReceiptTypeNotFoundException(String message) {
        super(message);
    }
}
