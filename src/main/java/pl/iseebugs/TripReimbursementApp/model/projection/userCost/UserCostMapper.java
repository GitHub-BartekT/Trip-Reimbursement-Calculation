package pl.iseebugs.TripReimbursementApp.model.projection.userCost;

import pl.iseebugs.TripReimbursementApp.model.UserCost;

public class UserCostMapper {
    public static UserCostWriteModel toWriteModel(UserCost userCost){
        return new UserCostWriteModel(userCost);
    }
    public static UserCostReadModel toReadModel(UserCost userCost){
        return new UserCostReadModel(userCost);
    }
    public static UserCostReadModelToReimbursement toReadModelToReimbursement(UserCost userCost){
        return new UserCostReadModelToReimbursement(userCost);
    }

}
