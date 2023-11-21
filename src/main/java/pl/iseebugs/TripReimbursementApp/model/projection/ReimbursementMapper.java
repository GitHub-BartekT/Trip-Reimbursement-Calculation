package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;

public class ReimbursementMapper {
    public static ReimbursementWriteModel toWriteModel(Reimbursement reimbursement){
        return new ReimbursementWriteModel(reimbursement);
    }

    public static ReimbursementReadModel toReadModel(Reimbursement reimbursement){
        return new ReimbursementReadModel(reimbursement);
    }
}
