package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Objects;
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
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "receipt_type_id")
    )
    private Set<ReceiptType> receiptTypes = new HashSet<>();

    public UserGroup() {
    }

    public UserGroup(UserGroup userGroup) {
        setId(userGroup.getId());
        setName(userGroup.getName());
        setDailyAllowance(userGroup.getDailyAllowance());
        setCostPerKm(userGroup.getCostPerKm());
        setMaxRefund(userGroup.getMaxRefund());
        setMaxMileage(userGroup.getMaxMileage());
        setReceiptTypes(userGroup.getReceiptTypes());
        setUsers(userGroup.getUsers());
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<ReceiptType> getReceiptTypes() {
        return receiptTypes;
    }

    public void setReceiptTypes(Set<ReceiptType> receiptTypes) {
        this.receiptTypes = receiptTypes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
