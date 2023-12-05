package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_costs")
public class UserCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double costValue;
    @ManyToOne
    @JoinColumn(name = "reimbursement_id")
    Reimbursement reimbursement;
    @ManyToOne
    @JoinColumn(name = "receipt_type_id")
    private ReceiptType receiptType;

    public UserCost() {
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

    public double getCostValue() {
        return costValue;
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

    public void setCostValue(double maxValue) {
        if (maxValue < 0){
            this.costValue = 0;
        } else {
        this.costValue = maxValue;
        }
    }
}
