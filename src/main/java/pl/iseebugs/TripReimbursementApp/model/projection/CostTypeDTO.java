package pl.iseebugs.TripReimbursementApp.model.projection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.iseebugs.TripReimbursementApp.model.CostType;

public class CostTypeDTO {
    private int id;
    @NotBlank(message = "Cost type must not be empty")
    @Size(max = 100, message = "Reimbursement name is too long.")
    private String name;
    @Positive(message = "Max value should be positive.")
    private double maxValue;

    public CostTypeDTO() {
    }

    public CostTypeDTO(CostType costType) {
        this.id = costType.getId();
        this.name = costType.getName();
        this.maxValue = costType.getMaxValue();
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
