package pl.iseebugs.TripReimbursementApp.model;

import pl.iseebugs.TripReimbursementApp.logic.UserGroupNotFoundException;

public class UserDTO {
    private int id;
    private String name;
    private UserGroupDTO userGroupDTO;


    public UserDTO() {
    }

    public UserDTO(User user){
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

    public int getUserGroupId() {
        return userGroupDTO.getId();

    }

    public UserGroupDTO getUserGroup() {
        return userGroupDTO;
    }

    public void setUserGroup(UserGroupDTO userGroupDTO) {
        this.userGroupDTO = userGroupDTO;
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
