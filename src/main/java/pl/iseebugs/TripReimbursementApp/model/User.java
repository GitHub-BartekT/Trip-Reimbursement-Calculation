package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "User name must not be empty")
    String name;
    @ManyToOne
    @JoinColumn(name = "user_group_id")
    UserGroup userGroup;
    @OneToMany(mappedBy = "user")
    private Set<Reimbursement> reimbursements;

    public User() {
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

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Set<Reimbursement> getReimbursements() {
        return reimbursements;
    }

    public void setReimbursements(Set<Reimbursement> reimbursements) {
        this.reimbursements = reimbursements;
    }
}
