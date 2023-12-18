package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "receipt_types")
public class ReceiptType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double maxValue;
    @ManyToMany(mappedBy = "receiptTypes")
    private Set<UserGroup> userGroups = new HashSet<>();
    @OneToMany(mappedBy = "receiptType")
    private Set<UserCost> userCosts = new HashSet<>();

    public ReceiptType() {
    }

    public ReceiptType(ReceiptType receiptType) {
        setId(receiptType.getId());
        setName(receiptType.getName());
        setMaxValue(receiptType.getMaxValue());
        getUserGroups().addAll(receiptType.getUserGroups());
        setUserCosts(receiptType.getUserCosts());
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

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public Set<UserCost> getUserCosts() {
        return userCosts;
    }

    public void setUserCosts(Set<UserCost> userCosts) {
        this.userCosts = userCosts;
    }

    public void setMaxValue(double maxValue) {
        if (maxValue < 0){
            this.maxValue = 0;
        } else {
        this.maxValue = maxValue;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReceiptType other = (ReceiptType) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder beginning = new StringBuilder(("ReceiptType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxValue=" + maxValue +
                ", userGroups="));
        for (UserGroup userGroup: userGroups) {
            beginning.append("{id =").append(userGroup.getId()).append(", name=").append(userGroup.getName()).append("}");
        }
       return beginning.toString();
    }
}
