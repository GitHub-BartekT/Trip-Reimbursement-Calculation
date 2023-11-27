package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_costs")
public class UserCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double cost_value;
    @ManyToMany(mappedBy = "userCosts")
    private Set<Reimbursement> reimbursements = new HashSet<>();

    protected UserCost() {
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

    public double getCost_value() {
        return cost_value;
    }

    void setCost_value(double maxValue) {
        if (maxValue < 0){
            this.cost_value = 0;
        } else {
        this.cost_value = maxValue;
        }
    }

    public Set<Reimbursement> getReimbursements() {
        return reimbursements;
    }

    public void setReimbursements(Set<Reimbursement> reimbursements) {
        this.reimbursements = reimbursements;
    }
}
