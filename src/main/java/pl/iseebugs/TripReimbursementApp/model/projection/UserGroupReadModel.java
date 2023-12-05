package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.UserGroup;

public class UserGroupReadModel {
    private int id;
    private String name;
    private double dailyAllowance;
    private double costPerKm;
    private double maxMileage;
    private double maxRefund;

        public UserGroupReadModel(){
        }

        public UserGroupReadModel(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
        this.dailyAllowance = userGroup.getDailyAllowance();
        this.costPerKm = userGroup.getCostPerKm();
        this.maxMileage = userGroup.getMaxMileage();
        this.maxRefund = userGroup.getMaxRefund();
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
}
