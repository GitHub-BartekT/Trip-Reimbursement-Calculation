package pl.iseebugs.TripReimbursementApp.model.projection.user;

import pl.iseebugs.TripReimbursementApp.model.User;

public class UserMapper {
    public static UserWriteModel toWriteModel(User user){
        return new UserWriteModel(user);
    }
}
