package pl.iseebugs.TripReimbursementApp.model.projection.reimbursement;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReimbursementHelper {
    protected static double returnValue(Reimbursement reimbursement) {
        UserGroup userGroup = reimbursement.getUser().getUserGroup();
        double distance = reimbursement.getDistance();
        double maxMileage = userGroup.getMaxMileage();
        double costPerKm = userGroup.getCostPerKm();
        double dailyAllowance = userGroup.getDailyAllowance();
        double maxRefund = userGroup.getMaxRefund();
        long duration;
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
        double result = (duration * dailyAllowance) + (distance * costPerKm);
        if (result > maxRefund){
            result = maxRefund;
        }
        return result;
    }
}
