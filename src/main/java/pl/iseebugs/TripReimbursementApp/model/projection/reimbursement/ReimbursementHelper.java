package pl.iseebugs.TripReimbursementApp.model.projection.reimbursement;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.UserCost;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class ReimbursementHelper {
    protected static double returnValue(Reimbursement reimbursement) {
        UserGroup userGroup = reimbursement.getUser().getUserGroup();
        double distance = reimbursement.getDistance();
        double maxMileage = userGroup.getMaxMileage();
        double costPerKm = userGroup.getCostPerKm();
        double dailyAllowance = userGroup.getDailyAllowance();
        double maxRefund = userGroup.getMaxRefund();
        long duration;
        Set<UserCost> userCosts = new HashSet<>();
        double allCostsSum = 0;

        if (reimbursement.getUserCosts() != null) {
            userCosts.addAll(reimbursement.getUserCosts());
            for (UserCost userCost : userCosts) {
                allCostsSum += Math.min(userCost.getCostValue(), userCost.getReceiptType().getMaxValue());
            }
        }

        LocalDate start = reimbursement.getStartDate();
        LocalDate end = reimbursement.getEndDate();

        if (distance > maxMileage){
            distance = maxMileage;
        }
        if (start == null){
            duration = 0;
        } else {
            duration = ChronoUnit.DAYS.between(start,end) + 1;
        }
        double result = (duration * dailyAllowance) + (distance * costPerKm) + allCostsSum;
        if (result > maxRefund){
            result = maxRefund;
        }
        return result;
    }
}
