package pl.iseebugs.TripReimbursementApp.model;

import org.springframework.beans.factory.annotation.Autowired;
import pl.iseebugs.TripReimbursementApp.logic.UserGroupNotFoundException;

public class UserDTO {
    private int id;
    private String name;
    private int userGroupId;

    @Autowired
    private UserGroupRepository repository;

    public UserDTO() {
    }

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.userGroupId = user.getUserGroup().getId();
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

    public User toUser() throws UserGroupNotFoundException {
        var result = new User();
        result.setId(this.id);
        result.setName(this.name);
        UserGroup userGroup = repository.findById(userGroupId)
                .orElseThrow(UserGroupNotFoundException::new);
        result.setUserGroup(userGroup);
        return result;
    }
}
