package pl.iseebugs.TripReimbursementApp.model.projection.userCost;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostWriteModel {
    private int id;
    @NotNull(message = "User Cost name couldn't be empty.")
    @Size(max = 100, message = "User Cost name is too long.")
    String name;
    @PositiveOrZero(message = "Cost Value should be positive.")
    double costValue;
    @NotNull
    private int reimbursementId;
    @NotNull
    private int receiptTypeId;

    public UserCostWriteModel() {
    }

    public UserCostWriteModel(UserCost userCost) {
        setId(userCost.getId());
        setName(userCost.getName());
        setCostValue(userCost.getCostValue());
        setReimbursementId(userCost.getReimbursement().getId());
        setReceiptTypeId(userCost.getReceiptType().getId());
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

    public double getCostValue() {
        return costValue;
    }

    public void setCostValue(double costValue) {
        this.costValue = costValue;
    }

    public int getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(int reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public int getReceiptTypeId() {
        return receiptTypeId;
    }

    public void setReceiptTypeId(int receiptTypeId) {
        this.receiptTypeId = receiptTypeId;
    }
}
