package pl.iseebugs.TripReimbursementApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "User name must not be empty")
    String name;
    Double maxValue;
    @ManyToOne
    @JoinColumn(name = "user_group_id")
    UserGroup users;

    protected User() {
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

    Double getMaxValue() {
        return maxValue;
    }

    void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public UserGroup getUsers() {
        return users;
    }

    public void setUsers(UserGroup userGroup) {
        this.users = userGroup;
    }
}
