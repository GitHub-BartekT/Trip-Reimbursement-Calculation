package pl.iseebugs.TripReimbursementApp.model.projection.userCost;

import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostReadModel {
    private int id;
    private String name;
    private double costValue;
    private int reimbursementId;
    private int receiptId;

    public UserCostReadModel() {
    }

    public UserCostReadModel(UserCost userCost) {
        id = userCost.getId();
        name = userCost.getName();
        costValue = userCost.getCostValue();
        reimbursementId = userCost.getReimbursement().getId();
        receiptId = userCost.getReceiptType().getId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCostValue() {
        return costValue;
    }

    public int getReimbursementId() {
        return reimbursementId;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
