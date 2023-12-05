package pl.iseebugs.TripReimbursementApp.model.projection.userCost;

import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostReadModel {
    private int id;
    private String name;
    private double costValue;

    public UserCostReadModel() {
    }

    public UserCostReadModel(UserCost userCost) {
        id = userCost.getId();
        name = userCost.getName();
        costValue = userCost.getCostValue();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxValue() {
        return costValue;
    }
}
