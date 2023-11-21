package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;

public class ReimbursementReadModelShort{
    private int id;
    private String name;
    private double returnValue;

    public ReimbursementReadModelShort(Reimbursement reimbursement) {
        id = reimbursement.getId();
        name = reimbursement.getName();
        returnValue = returnValue(reimbursement);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getReturnValue() {
        return returnValue;
    }

    private double returnValue(Reimbursement reimbursement){
       return ReimbursementHelper.returnValue(reimbursement);
    }
}
