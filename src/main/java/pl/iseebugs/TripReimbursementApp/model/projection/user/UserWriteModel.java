package pl.iseebugs.TripReimbursementApp.model.projection.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.iseebugs.TripReimbursementApp.model.User;

public class UserWriteModel {

    private int id;
    @NotBlank(message = "User name couldn't be empty.")
    @Size(max = 100, message = "User name is too long.")
    private String name;
    @NotNull
    private int userGroupId;

    public UserWriteModel() {
    }

    public UserWriteModel(User user){
        setId(user.getId());
        setName(user.getName());
        setUserGroupId(user.getUserGroup().getId());
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
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }
}
