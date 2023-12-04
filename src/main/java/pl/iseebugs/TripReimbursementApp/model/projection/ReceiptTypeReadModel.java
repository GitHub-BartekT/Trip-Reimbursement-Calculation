package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.ReceiptType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ReceiptTypeReadModel {
    private int id;
    private String name;
    private double maxValue;
    private Set<UserGroupReadModelShort> userGroups = new HashSet<>();

    public ReceiptTypeReadModel(ReceiptType receiptType) {
        id = receiptType.getId();
        name = receiptType.getName();
        maxValue = receiptType.getMaxValue();
        Set<UserGroupReadModelShort> userGroupDTOS = receiptType.getUserGroups().stream()
                .map(UserGroupReadModelShort::new).collect(Collectors.toSet());
        getUserGroups().addAll(userGroupDTOS);
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

    public Set<UserGroupReadModelShort> getUserGroups() {
        return userGroups;
    }
}
