package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cost_types")
public class CostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double maxValue;
    @ManyToMany(mappedBy = "costTypes")
    private Set<Reimbursement> reimbursements = new HashSet<>();

    protected CostType() {
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public double getMaxValue() {
        return maxValue;
    }

    void setMaxValue(double maxValue) {
        if (maxValue < 0){
            this.maxValue = 0;
        } else {
        this.maxValue = maxValue;
        }
    }

    public Set<Reimbursement> getReimbursements() {
        return reimbursements;
    }

    public void setReimbursements(Set<Reimbursement> reimbursements) {
        this.reimbursements = reimbursements;
    }
}
