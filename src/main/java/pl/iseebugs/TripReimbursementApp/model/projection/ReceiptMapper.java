package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.ReceiptType;

public class ReceiptMapper {
    public static ReceiptTypeWriteModel toWriteModel(ReceiptType receiptType){
        return new ReceiptTypeWriteModel(receiptType);
    }
    public static ReceiptTypeReadModel toReadModel(ReceiptType receiptType){
        return new ReceiptTypeReadModel(receiptType);
    }
}
