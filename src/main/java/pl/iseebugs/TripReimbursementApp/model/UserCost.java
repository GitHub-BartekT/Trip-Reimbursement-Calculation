package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_costs")
public class UserCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double cost_value;
    @ManyToOne
    @JoinColumn(name = "reimbursement_id")
    Reimbursement reimbursement;
    @ManyToOne
    @JoinColumn(name = "receipt_type_id")
    private ReceiptType receiptType;

    protected UserCost() {
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

    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(Reimbursement reimbursements) {
        this.reimbursement = reimbursements;
    }

    public ReceiptType getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(ReceiptType receiptType) {
        this.receiptType = receiptType;
    }

    void setCost_value(double maxValue) {
        if (maxValue < 0){
            this.cost_value = 0;
        } else {
        this.cost_value = maxValue;
        }
    }
}
