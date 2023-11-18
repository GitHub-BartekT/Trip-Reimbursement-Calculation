package pl.iseebugs.TripReimbursementApp.model;

public class UserGroupDTO {
    private int id;
    private String name;
    double dailyAllowance;
    double costPerKm;
    double maxMileage;
    double maxRefund;

    public UserGroupDTO() {    }

    public UserGroupDTO(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
        setDailyAllowance(userGroup.getDailyAllowance());
        setCostPerKm(userGroup.getCostPerKm());
        setMaxMileage(userGroup.getMaxMileage());
        setMaxRefund(userGroup.getMaxRefund());
        }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDailyAllowance() {
        return dailyAllowance;
    }

    public void setDailyAllowance(double dailyAllowance) {
        if (dailyAllowance < 0){
            this.dailyAllowance = 0.0;
        } else {
            this.dailyAllowance = dailyAllowance;
        }
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(double costPerKm) {
        if(costPerKm < 0){
            this.costPerKm = 0.0;
        } else {
            this.costPerKm = costPerKm;
        }
    }

    public double getMaxMileage() {
        return maxMileage;
    }

    public void setMaxMileage(double maxMileage) {
        if (maxMileage < 0){
            this.maxMileage = 0.0;}
        else {
            this.maxMileage = maxMileage;
        }
    }

    public double getMaxRefund() {
        return maxRefund;
    }

    public void setMaxRefund(double maxRefund) {
        if (maxRefund < 0){
            this.maxRefund = 0.0;
        } else {
            this.maxRefund = maxRefund;
        }
    }

    private void validate(){
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("User Group name couldn't be empty.");
        } else if (name.length() > 100){
            throw new IllegalArgumentException("User Group name is too long.");
        } else if (this.dailyAllowance < 0){
            throw new IllegalArgumentException("Daily allowance cannot be negative.");
        } else if (this.costPerKm < 0){
            throw new IllegalArgumentException("Cost per kilometer cannot be negative.");
        } else if (this.maxMileage < 0){
            throw new IllegalArgumentException("Max mileage cannot be negative.");
        } else if (this.maxRefund < 0){
            throw new IllegalArgumentException("Max refund cannot be negative.");
        }
    }

    public UserGroup toUserGroup(){
        validate();

        var result = new UserGroup();
        result.setId(this.id);
        result.setName(this.name);
        result.setDailyAllowance(this.dailyAllowance);
        result.setCostPerKm(this.costPerKm);
        result.setMaxMileage(this.maxMileage);
        result.setMaxRefund(this.maxRefund);
        return result;
    }
}
