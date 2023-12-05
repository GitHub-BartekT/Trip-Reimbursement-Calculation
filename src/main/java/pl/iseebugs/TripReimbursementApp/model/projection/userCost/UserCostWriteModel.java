package pl.iseebugs.TripReimbursementApp.model.projection.userCost;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostWriteModel {
    private int id;
    @NotNull(message = "User Cost name couldn't be empty.")
    @Size(max = 100, message = "User Cost name is too long.")
    String name;
    @Positive(message = "Cost Value should be positive.")
    double costValue;

    public UserCostWriteModel() {
    }

    public UserCostWriteModel(UserCost userCost) {
        setId(userCost.getId());
        setName(userCost.getName());
        setMaxValue(userCost.getCostValue());
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
        return costValue;
    }

    public void setMaxValue(double maxValue) {
        this.costValue = maxValue;
    }
}
