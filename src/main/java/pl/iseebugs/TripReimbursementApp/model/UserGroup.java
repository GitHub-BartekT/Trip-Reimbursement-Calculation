package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_groups")
public class UserGroup {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "Group name must not be empty")
    String name;
    double dailyAllowance;
    double costPerKm;
    double maxMileage;
    double maxRefund;
    @OneToMany(mappedBy = "userGroup")
    private Set<User> users;
    @ManyToMany
    @JoinTable(
            name = "user_groups_receipt_types",
            joinColumns = @JoinColumn(name = "receipt_type_id"),
            inverseJoinColumns = @JoinColumn(name = "user_group_id")
    )
    private Set<ReceiptType> receiptTypes = new HashSet<>();

    public UserGroup() {
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

    public double getDailyAllowance() {
        return dailyAllowance;
    }

    public void setDailyAllowance(double dailyAllowance) {
        this.dailyAllowance = dailyAllowance;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(double costPerKm) {
        this.costPerKm = costPerKm;
    }

    public double getMaxMileage() {
        return maxMileage;
    }

    public void setMaxMileage(double maxMileage) {
        this.maxMileage = maxMileage;
    }

    public double getMaxRefund() {
        return maxRefund;
    }

    public void setMaxRefund(double maxRefund) {
        this.maxRefund = maxRefund;
    }

    Set<User> getUsers() {
        return users;
    }

    void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<ReceiptType> getReceiptTypes() {
        return receiptTypes;
    }

    public void setReceiptTypes(Set<ReceiptType> receiptTypes) {
        this.receiptTypes = receiptTypes;
    }
}
