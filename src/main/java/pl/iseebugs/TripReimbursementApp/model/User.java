package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "User name must not be empty")
    String name;
    boolean isAdmin;
    @ManyToOne
    @JoinColumn(name = "user_group_id")
    UserGroup userGroup;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reimbursement> reimbursements = new HashSet<>();

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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
        this.reimbursements.addAll(reimbursements);
    }
}
