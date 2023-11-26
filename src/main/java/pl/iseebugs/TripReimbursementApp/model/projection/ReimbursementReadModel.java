package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;

import java.time.LocalDate;

public class ReimbursementReadModel {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int distance;
    private boolean pushedToAccept;
    private int userId;
    private double returnValue;

    public ReimbursementReadModel(Reimbursement reimbursement) {
        id = reimbursement.getId();
        name = reimbursement.getName();
        startDate = reimbursement.getStartDate();
        endDate = reimbursement.getEndDate();
        distance = reimbursement.getDistance();
        pushedToAccept = reimbursement.isPushedToAccept();
        userId = reimbursement.getUser().getId();
        returnValue = returnValue(reimbursement);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isPushedToAccept() {
        return pushedToAccept;
    }

    public int getUserId() {
        return userId;
    }

    public double getReturnValue() {
        return returnValue;
    }

    private double returnValue(Reimbursement reimbursement){
        return ReimbursementHelper.returnValue(reimbursement);
    }
}
