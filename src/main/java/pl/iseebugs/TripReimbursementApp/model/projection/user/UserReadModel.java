package pl.iseebugs.TripReimbursementApp.model.projection.user;

import pl.iseebugs.TripReimbursementApp.model.User;

public class UserReadModel {
    private int id;
    private String name;
    private boolean isAdmin;
    private int userGroupId;

    public UserReadModel() {
    }

    public UserReadModel(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.isAdmin = user.isAdmin();
        this.userGroupId = user.getUserGroup().getId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}