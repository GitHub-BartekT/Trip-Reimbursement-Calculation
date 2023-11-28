package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.ReceiptType;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;

import java.util.HashSet;
import java.util.Set;

public class ReceiptTypeReadModel {
    private int id;
    private String name;
    private double maxValue;
    private Set<UserGroup> userGroups = new HashSet<>();

    public ReceiptTypeReadModel(ReceiptType receiptType) {
        id = receiptType.getId();
        name = receiptType.getName();
        maxValue = receiptType.getMaxValue();
        getUserGroups().addAll(receiptType.getUserGroups());
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public double getMaxValue() {
        return maxValue;
    }

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }
}
