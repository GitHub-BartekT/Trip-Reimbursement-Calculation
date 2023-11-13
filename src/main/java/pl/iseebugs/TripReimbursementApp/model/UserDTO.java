package pl.iseebugs.TripReimbursementApp.model;

public class UserDTO {
    private int id;
    private String name;
    private int userGroupId;

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

    public User toUser(){
        var result = new User();
        result.setId(this.id);
        result.setName(this.name);

        /*
        TODO:
            userGroup -> findById - add method!!! + tests
            UserGroup -> userGroupService.findById(this.userGroupId);
            result.setUserGroup(UserGroup);
        */
        return result;
    }
}
