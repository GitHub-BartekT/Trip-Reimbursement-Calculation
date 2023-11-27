package pl.iseebugs.TripReimbursementApp.model.projection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostDTO {
    private int id;
    @NotBlank(message = "Cost type must not be empty")
    @Size(max = 100, message = "Reimbursement name is too long.")
    private String name;
    @Positive(message = "Max value should be positive.")
    private double cost_value;

    public UserCostDTO() {
    }

    public UserCostDTO(UserCost costType) {
        this.id = costType.getId();
        this.name = costType.getName();
        this.cost_value = costType.getCost_value();
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

    public double getCost_value() {
        return cost_value;
    }

    public void setCost_value(double cost_value) {
        this.cost_value = cost_value;
    }
}
