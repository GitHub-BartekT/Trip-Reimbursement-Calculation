package pl.iseebugs.TripReimbursementApp.model.projection.userGroup;

import pl.iseebugs.TripReimbursementApp.model.UserGroup;

public class UserGroupMapper {
    public static UserGroupWriteModel toWriteModel(UserGroup userGroup){
        return new UserGroupWriteModel(userGroup);
    }
    public static UserGroupReadModel toReadModel(UserGroup userGroup){
        return new UserGroupReadModel(userGroup);
    }
    public static UserGroupReadModelShort toReadModelShort(UserGroup userGroup){
        return new UserGroupReadModelShort(userGroup);
    }
    public static UserGroupReadModelFull toReadModelFull(UserGroup userGroup){
        return new UserGroupReadModelFull(userGroup);
    }
}
