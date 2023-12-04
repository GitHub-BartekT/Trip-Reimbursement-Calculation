package pl.iseebugs.TripReimbursementApp.model.projection;

import pl.iseebugs.TripReimbursementApp.model.UserGroup;

public class UserGroupReadModelShort {
    private int id;
    private String name;

    public UserGroupReadModelShort(){}

    public UserGroupReadModelShort(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
