package pl.iseebugs.TripReimbursementApp.model.projection.receiptType;

import pl.iseebugs.TripReimbursementApp.model.ReceiptType;

public class ReceiptTypeReadModelShort {
    private int id;
    private String name;
    private double maxValue;

    public ReceiptTypeReadModelShort() {
    }

    public ReceiptTypeReadModelShort(ReceiptType receiptType) {
        id = receiptType.getId();
        name = receiptType.getName();
        maxValue = receiptType.getMaxValue();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxValue() {
        return maxValue;
    }
}
