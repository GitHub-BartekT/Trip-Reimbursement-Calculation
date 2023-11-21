package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;

import java.time.LocalDate;

public class ReimbursementWriteModel {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int distance;
    private boolean pushedToAccept;
    private int userId;

    public ReimbursementWriteModel(Reimbursement reimbursement) {
        id = reimbursement.getId();
        name = reimbursement.getName();
        startDate = reimbursement.getStartDate();
        endDate = reimbursement.getEndDate();
        distance = reimbursement.getDistance();
        pushedToAccept = reimbursement.isPushedToAccept();
        userId = reimbursement.getUser().getId();
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

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
