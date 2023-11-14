package pl.iseebugs.TripReimbursementApp.model;

import pl.iseebugs.TripReimbursementApp.logic.UserGroupNotFoundException;

public class UserDTO {
    private int id;
    private String name;
    private UserGroup userGroup;


    public UserDTO() {
    }

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.userGroup = user.getUserGroup();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserGroupId() {
        return userGroup.getId();

    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public User toUser() throws UserGroupNotFoundException {
        var result = new User();
        result.setId(this.id);
        result.setName(this.name);
        result.setUserGroup(userGroup);
        return result;
    }
}
