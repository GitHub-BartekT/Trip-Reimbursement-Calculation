package pl.iseebugs.TripReimbursementApp.model;

public class UserGroupDTO {
    private int id;
    private String name;

    public UserGroupDTO() {    }

    public UserGroupDTO(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
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

    public UserGroup toUserGroup(){
        var result = new UserGroup();
        result.setId(this.id);
        result.setName(this.name);
        return result;
    }
}
