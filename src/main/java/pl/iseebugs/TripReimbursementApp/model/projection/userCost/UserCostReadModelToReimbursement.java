package pl.iseebugs.TripReimbursementApp.model.projection.userCost;

import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostReadModelToReimbursement {
    private int id;
    private String name;
    private double costValue;
    private String receiptName;
    private double maxValue;

    public UserCostReadModelToReimbursement() {
    }

    public UserCostReadModelToReimbursement(UserCost userCost) {
        id = userCost.getId();
        name = userCost.getName();
        costValue = userCost.getCostValue();
        receiptName = userCost.getReceiptType().getName();
        maxValue = userCost.getReceiptType().getMaxValue();
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

    public String getReceiptName() {
        return receiptName;
    }

    public double getMaxValue() {
        return maxValue;
    }
}
