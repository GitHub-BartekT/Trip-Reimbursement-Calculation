package pl.iseebugs.TripReimbursementApp.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reimbursements")
public class Reimbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    int distance;
    boolean pushedToAccept;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToMany
    @JoinTable(
            name = "reimbursements_user_costs",
            joinColumns = @JoinColumn(name = "reimbursement_id"),
            inverseJoinColumns = @JoinColumn(name = "user_cost_id")
    )
    private Set<UserCost> userCosts = new HashSet<>();

    public Reimbursement() {
    }

    public Reimbursement(String name, LocalDate startDate, LocalDate endDate, int distance, boolean pushedToAccept, User user) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.distance = distance;
        this.pushedToAccept = pushedToAccept;
        this.user = user;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate start_date) {
        this.startDate = start_date;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate end_date) {
        this.endDate = end_date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isPushedToAccept() {
        return pushedToAccept;
    }

    public void setPushedToAccept(boolean pushedToAccept) {
        this.pushedToAccept = pushedToAccept;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<UserCost> getUserCosts() {
        return userCosts;
    }

    public void setUserCosts(Set<UserCost> costTypes) {
        this.userCosts = costTypes;
    }
}