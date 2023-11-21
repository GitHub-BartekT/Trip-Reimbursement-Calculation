package pl.iseebugs.TripReimbursementApp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "reimbursement")
public class Reimbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "User name must not be empty")
    String name;
    LocalDate startDate;
    LocalDate endDate;
    int distance;
    boolean pushedToAccept;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public Reimbursement() {
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
}