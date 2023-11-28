package pl.iseebugs.TripReimbursementApp.model.projection;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.iseebugs.TripReimbursementApp.model.ReceiptType;

public class ReceiptTypeWriteModel {
    private int id;
    @NotNull(message = "Reimbursement name couldn't be empty.")
    @Size(max = 100, message = "Reimbursement name is too long.")
    String name;
    @Positive(message = "Max value should be positive.")
    double maxValue;

    public ReceiptTypeWriteModel() {
    }

    public ReceiptTypeWriteModel(ReceiptType receiptType) {
        setId(receiptType.getId());
        setName(receiptType.getName());
        setMaxValue(receiptType.getMaxValue());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
}
