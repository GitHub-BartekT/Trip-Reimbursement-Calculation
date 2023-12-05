package pl.iseebugs.TripReimbursementApp.model.projection.userGroup;

import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptTypeReadModelShort;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserGroupReadModelFull {
    private int id;
    private String name;
    private double dailyAllowance;
    private double costPerKm;
    private double maxMileage;
    private double maxRefund;
    private Set<ReceiptTypeReadModelShort> receiptTypes = new HashSet<>();

        public UserGroupReadModelFull(){
        }

        public UserGroupReadModelFull(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
        this.dailyAllowance = userGroup.getDailyAllowance();
        this.costPerKm = userGroup.getCostPerKm();
        this.maxMileage = userGroup.getMaxMileage();
        this.maxRefund = userGroup.getMaxRefund();
        Set<ReceiptTypeReadModelShort> receiptTypesRMS = userGroup.getReceiptTypes().stream()
                .map(ReceiptMapper::toReadModelShort).collect(Collectors.toSet());
        getReceiptTypes().addAll(receiptTypesRMS);
        }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDailyAllowance() {
        return dailyAllowance;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public double getMaxMileage() {
        return maxMileage;
    }

    public double getMaxRefund() {
        return maxRefund;
    }

    public Set<ReceiptTypeReadModelShort> getReceiptTypes() {
        return receiptTypes;
    }
}
