package pl.iseebugs.TripReimbursementApp.model.projection.reimbursement;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostReadModelToReimbursement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementReadModel {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int distance;
    private boolean pushedToAccept;
    private int userId;
    private double returnValue;
    private List<UserCostReadModelToReimbursement> userCosts = new ArrayList<>();

    public ReimbursementReadModel(Reimbursement reimbursement) {
        id = reimbursement.getId();
        name = reimbursement.getName();
        startDate = reimbursement.getStartDate();
        endDate = reimbursement.getEndDate();
        distance = reimbursement.getDistance();
        pushedToAccept = reimbursement.isPushedToAccept();
        userId = reimbursement.getUser().getId();
        returnValue = returnValue(reimbursement);
        if (reimbursement.getUserCosts() != null) {
            userCosts = reimbursement.getUserCosts().stream()
                    .map(UserCostMapper::toReadModeltoReimburcement)
                    .toList();
        }
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

    public List<UserCostReadModelToReimbursement> getUserCosts() {
        return userCosts;
    }
}
