package pl.iseebugs.TripReimbursementApp.model.projection.user;

import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupDTO;

public class UserDTO {
    private int id;
    private String name;
    private boolean isAdmin;
    private UserGroupDTO userGroupDTO;

    public UserDTO() {
    }

    public UserDTO(User user){
        this.isAdmin = user.isAdmin();
        this.id = user.getId();
        this.name = user.getName();
        userGroupDTO = new UserGroupDTO(user.getUserGroup());
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

    public UserGroupDTO getUserGroup() {
        return userGroupDTO;
    }

    public void setUserGroup(UserGroupDTO userGroupDTO) {
        this.userGroupDTO = userGroupDTO;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public User toUser() throws UserGroupNotFoundException {
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name couldn't be empty.");
        }  else if (name.length() > 100){
            throw new IllegalArgumentException("User name is too long.");
        }
        if (this.userGroupDTO == null || this.userGroupDTO.getId() <= 0){
            throw new UserGroupNotFoundException();
        }
        var result = new User();
        result.setId(this.id);
        result.setName(this.name);
        result.setUserGroup(userGroupDTO.toUserGroup());
        return result;
    }
}
