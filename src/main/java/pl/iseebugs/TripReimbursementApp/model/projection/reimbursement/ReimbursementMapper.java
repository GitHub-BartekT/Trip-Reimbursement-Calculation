package pl.iseebugs.TripReimbursementApp.model.projection.reimbursement;

import pl.iseebugs.TripReimbursementApp.model.Reimbursement;

public class ReimbursementMapper {
    public static ReimbursementWriteModel toWriteModel(Reimbursement reimbursement){
        return new ReimbursementWriteModel(reimbursement);
    }

    public static ReimbursementReadModel toReadModel(Reimbursement reimbursement){
        return new ReimbursementReadModel(reimbursement);
    }

    public static ReimbursementReadModelShort toReadModelShort(Reimbursement reimbursement){
        return new ReimbursementReadModelShort(reimbursement);
    }
}
