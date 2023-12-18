package pl.iseebugs.TripReimbursementApp.model.projection.receiptType;

import pl.iseebugs.TripReimbursementApp.model.ReceiptType;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostReadModel;

public class ReceiptMapper {
    public static ReceiptTypeWriteModel toWriteModel(ReceiptType receiptType){
        return new ReceiptTypeWriteModel(receiptType);
    }
    public static ReceiptTypeReadModel toReadModel(ReceiptType receiptType){
        return new ReceiptTypeReadModel(receiptType);
    }
    public static ReceiptTypeReadModelShort toReadModelShort(ReceiptType receiptType){
        return new ReceiptTypeReadModelShort(receiptType);
    }
}
