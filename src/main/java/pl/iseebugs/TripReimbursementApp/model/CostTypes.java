package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "cost_types")
public class CostTypes {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "Cost type must not be empty")
    String name;
    double maxValue;

    protected CostTypes() {
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    double getMaxValue() {
        return maxValue;
    }

    void setMaxValue(double maxValue) {
        if (maxValue <= 0){
            this.maxValue = 0;
        }
        this.maxValue = maxValue;
    }
}
