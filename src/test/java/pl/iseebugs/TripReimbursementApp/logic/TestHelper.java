package pl.iseebugs.TripReimbursementApp.logic;

import pl.iseebugs.TripReimbursementApp.exception.ReimbursementNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.of;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.*;

public class TestHelper {

    protected static String createLongString(int length){
        if (length <=0 ){
            return "";
        }
        return String.valueOf('A').repeat(length);
    }

    private static List<String> userGroupsDataNames(){
    return List.of("fooGroup","barGroup", "foobarGroup");
    }

    private static List<UserGroup> userGroupsDataOnlyWithNames(List<String> userGroupsNames){
        List<UserGroup> result = new ArrayList<>();
        for (String entity : userGroupsNames) {
            UserGroup userGroup = new UserGroup();
            userGroup.setName(entity);
            result.add(userGroup);
        }
        return result;
    }

    private static List<UserGroup> userGroupsDataOnlyWithNamesPreparedData(){
        return userGroupsDataOnlyWithNames(userGroupsDataNames());
    }

    private static List<UserGroup> userGroupsDataAllParams(){
        List<UserGroup> result = new ArrayList<>();
        UserGroup userGroup_01 = new UserGroup();
        userGroup_01.setName("group_001_NoDailyAllowance");
        userGroup_01.setDailyAllowance(0);
        userGroup_01.setCostPerKm(0.1);
        userGroup_01.setMaxMileage(1000);
        userGroup_01.setMaxRefund(100);
        result.add(userGroup_01);

        UserGroup userGroup_02 = new UserGroup();
        userGroup_02.setName("group_002_NoCostPerKm");
        userGroup_02.setDailyAllowance(10);
        userGroup_02.setCostPerKm(0);
        userGroup_02.setMaxMileage(100);
        userGroup_02.setMaxRefund(10);
        result.add(userGroup_02);

        UserGroup userGroup_03 = new UserGroup();
        userGroup_03.setName("group_003_NoMaxMileage");
        userGroup_03.setDailyAllowance(10);
        userGroup_03.setCostPerKm(0.25);
        userGroup_03.setMaxMileage(0);
        userGroup_03.setMaxRefund(500);
        result.add(userGroup_03);

        UserGroup userGroup_04 = new UserGroup();
        userGroup_04.setName("group_004_NoMaxRefund");
        userGroup_04.setDailyAllowance(10);
        userGroup_04.setCostPerKm(0.25);
        userGroup_04.setMaxMileage(100);
        userGroup_04.setMaxRefund(0);
        result.add(userGroup_04);

        UserGroup userGroup_05 = new UserGroup();
        userGroup_05.setName("group_005_Ok");
        userGroup_05.setDailyAllowance(10);
        userGroup_05.setCostPerKm(0.25);
        userGroup_05.setMaxMileage(500);
        userGroup_05.setMaxRefund(100);
        result.add(userGroup_05);

        UserGroup userGroup_06 = new UserGroup();
        userGroup_06.setName("group_006_Ok");
        userGroup_06.setDailyAllowance(10);
        userGroup_06.setCostPerKm(0.25);
        userGroup_06.setMaxMileage(500);
        userGroup_06.setMaxRefund(200);
        result.add(userGroup_06);

        return result;
    }

    protected static void userGroupRepositoryInitializeData(InMemoryUserGroupRepository inMemoryUserGroupRepository, List<UserGroup> entities){
        for (UserGroup entity : entities) {
            inMemoryUserGroupRepository.save(entity);
        }
    }

    protected static void userGroupRepositoryInitialDataOnlyNames(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        userGroupRepositoryInitializeData(inMemoryUserGroupRepository, userGroupsDataOnlyWithNamesPreparedData());
    }

    protected static void userGroupRepositoryInitialDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        userGroupRepositoryInitializeData(inMemoryUserGroupRepository, userGroupsDataAllParams());
    }

    private static List<String> receiptTypesDataOnlyNames(){
        return List.of("Hotel_01","Aeroplane_02", "Food_03", "Train_04");
    }

    private static List<ReceiptType> receiptTypesOnlyWithNames(){
        List<String> receiptTypeNames = receiptTypesDataOnlyNames();
        List<ReceiptType> result = new ArrayList<>();
        for (String entity : receiptTypeNames) {
            ReceiptType receiptType = new ReceiptType();
            receiptType.setName(entity);
            result.add(receiptType);
        }
        return result;
    }

    private static List<ReceiptType> receiptTypesDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        List<ReceiptType> result = new ArrayList<>();
        ReceiptType receiptType_01 = new ReceiptType();
        receiptType_01.setName("Train_AllUsers");
        receiptType_01.setMaxValue(100);
        for (int i = 1 ; i <= inMemoryUserGroupRepository.count(); i++){
        receiptType_01.getUserGroups().add(inMemoryUserGroupRepository.findById(i).orElse(null));
        }
        result.add(receiptType_01);

        ReceiptType receiptType_02 = new ReceiptType();
        receiptType_02.setName("Aeroplane_CEO");
        receiptType_02.setMaxValue(2000);
        receiptType_02.getUserGroups().add(inMemoryUserGroupRepository.findById(1).orElse(null));
        result.add(receiptType_02);

        ReceiptType receiptType_03 = new ReceiptType();
        receiptType_03.setName("Food_AllUsers");
        receiptType_03.setMaxValue(45);
        for (int i = 1 ; i <= inMemoryUserGroupRepository.count(); i++){
            receiptType_03.getUserGroups().add(inMemoryUserGroupRepository.findById(i).orElse(null));
        }
        result.add(receiptType_03);

        ReceiptType receiptType_04 = new ReceiptType();
        receiptType_04.setName("Food_CEO");
        receiptType_04.setMaxValue(450);
        receiptType_04.getUserGroups().add(inMemoryUserGroupRepository.findById(1).orElse(null));
        result.add(receiptType_04);

        ReceiptType receiptType_05 = new ReceiptType();
        receiptType_05.setName("Hotels_Sellers");
        receiptType_05.setMaxValue(450);
        receiptType_05.getUserGroups().add(inMemoryUserGroupRepository.findById(2).orElse(null));
        result.add(receiptType_05);

        ReceiptType receiptType_06 = new ReceiptType();
        receiptType_06.setName("Other_Directors");
        receiptType_06.setMaxValue(450);
        result.add(receiptType_06);

        return result;
    }

    protected static void receiptTypesRepositoryInitializeData(InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository, List<ReceiptType> entities){
        for (ReceiptType entity : entities) {
            inMemoryReceiptTypeRepository.save(entity);
        }
    }

    protected static void receiptTypesRepositoryInitialDataAllParams(InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository,InMemoryUserGroupRepository inMemoryUserGroupRepository){
        List<String> userGroupsNames = List.of("CEO", "Sellers", "Regular employee", "Office employee");
        List<UserGroup> userGroups = userGroupsDataOnlyWithNames(userGroupsNames);
        userGroupRepositoryInitializeData(inMemoryUserGroupRepository, userGroups);
        receiptTypesRepositoryInitializeData(inMemoryReceiptTypeRepository, receiptTypesDataAllParams(inMemoryUserGroupRepository));
    }

    protected static void receiptTypesRepositoryInitialDataForUserCosts(InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository,InMemoryUserGroupRepository inMemoryUserGroupRepository){
        receiptTypesRepositoryInitializeData(inMemoryReceiptTypeRepository, receiptTypesDataAllParams(inMemoryUserGroupRepository));
    }

    private static List<String> userDataOnlyNames(){
        return List.of("foo","bar", "foobar");
    }

    private static List<User> userDataOnlyWithNames(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        int groupRepoSize = inMemoryUserGroupRepository.count();
        List<String> userNames = userDataOnlyNames();
        List<User> result = new ArrayList<>();
        for(int i = 1; i <= groupRepoSize; i++) {
            for (String entity : userNames) {
                User user = new User();
                user.setName(entity);
                user.setUserGroup(inMemoryUserGroupRepository.findById(i).orElse(null));
                result.add(user);
            }
        }
        return result;
    }

    private static List<User> userDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        List<User> result = new ArrayList<>();
        User user_01 = new User();
        user_01.setName("user_01_noDailyRefund");
        user_01.setUserGroup(inMemoryUserGroupRepository.findById(1).orElse(null));
        result.add(user_01);

        User user_02 = new User();
        user_02.setName("user_02_noCostPerKm");
        user_02.setUserGroup(inMemoryUserGroupRepository.findById(2).orElse(null));
        result.add(user_02);

        User user_03 = new User();
        user_03.setName("user_03_noMaxMileage");
        user_03.setUserGroup(inMemoryUserGroupRepository.findById(3).orElse(null));
        result.add(user_03);

        User user_04 = new User();
        user_04.setName("user_04_noRefund");
        user_04.setUserGroup(inMemoryUserGroupRepository.findById(4).orElse(null));
        result.add(user_04);

        User user_05 = new User();
        user_05.setName("user_05_ok");
        user_05.setUserGroup(inMemoryUserGroupRepository.findById(5).orElse(null));
        result.add(user_05);

        User user_06 = new User();
        user_06.setName("user_06_ok");
        user_06.setUserGroup(inMemoryUserGroupRepository.findById(6).orElse(null));
        result.add(user_06);

        User user_07 = new User();
        user_07.setName("user_07_no_reimbursements");
        user_07.setUserGroup(inMemoryUserGroupRepository.findById(6).orElse(null));
        result.add(user_07);

        return result;
    }

    private static List<User> userDataForUserCosts(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        List<User> result = new ArrayList<>();
        User user_01 = new User();
        user_01.setName("user_01");
        user_01.setUserGroup(inMemoryUserGroupRepository.findById(1).orElse(null));
        result.add(user_01);

        User user_02 = new User();
        user_02.setName("user_02");
        user_02.setUserGroup(inMemoryUserGroupRepository.findById(2).orElse(null));
        result.add(user_02);

        User user_03 = new User();
        user_03.setName("user_03");
        user_03.setUserGroup(inMemoryUserGroupRepository.findById(3).orElse(null));
        result.add(user_03);

        User user_04 = new User();
        user_04.setName("user_04");
        user_04.setUserGroup(inMemoryUserGroupRepository.findById(1).orElse(null));
        result.add(user_04);

        User user_05 = new User();
        user_05.setName("user_05");
        user_05.setUserGroup(inMemoryUserGroupRepository.findById(2).orElse(null));
        result.add(user_05);

        User user_06 = new User();
        user_06.setName("user_06");
        user_06.setUserGroup(inMemoryUserGroupRepository.findById(3).orElse(null));
        result.add(user_06);
        return result;
    }

    protected static void userRepositoryInitializeData(InMemoryUserRepository inMemoryUserRepository, List<User> entities){
        for (User entity : entities) {
            inMemoryUserRepository.save(entity);
        }
    }

    protected static void userRepositoryInitialDataOnlyNames(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository) {
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        userRepositoryInitializeData(inMemoryUserRepository, userDataOnlyWithNames(inMemoryUserGroupRepository));
    }

    protected static void userRepositoryInitialDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository) {
        userGroupRepositoryInitialDataAllParams(inMemoryUserGroupRepository);
        userRepositoryInitializeData(inMemoryUserRepository, userDataAllParams(inMemoryUserGroupRepository));
    }

    protected static void userRepositoryInitialDataForUserCosts(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository) {
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        userRepositoryInitializeData(inMemoryUserRepository, userDataForUserCosts(inMemoryUserGroupRepository));
    }

    private static List<Reimbursement> reimbursementsDataAllParams(InMemoryUserRepository inMemoryUserRepository){
        List<Reimbursement> result = new ArrayList<>();
        Reimbursement reimbursement_01 = new Reimbursement(
                "reimbursement_001_zeroDaysNoRefund", null,
                of(2022,3,20), 0,
                false, inMemoryUserRepository.findById(1).orElse(null));
        result.add(reimbursement_01);

        Reimbursement reimbursement_02 = new Reimbursement(
                "reimbursement_002_oneDayNoRefund", of(2022,3,21),
                of(2022,3,21), 0,
                false, inMemoryUserRepository.findById(1).orElse(null));
        result.add(reimbursement_02);

        Reimbursement reimbursement_03 = new Reimbursement(
                "reimbursement_003_moreDaysNoRefund", of(2022,3,21),
                of(2022,3,22), 0,
                false, inMemoryUserRepository.findById(1).orElse(null));
        result.add(reimbursement_03);

        Reimbursement reimbursement_04 = new Reimbursement(
                "reimbursement_004_zeroDaysNoRefund", null,
                of(2022,3,20), 0,
                false, inMemoryUserRepository.findById(5).orElse(null));
        result.add(reimbursement_04);

        Reimbursement reimbursement_05 = new Reimbursement(
                "reimbursement_005_oneDayRefund", of(2022,3,21),
                of(2022,3,21), 0,
                false, inMemoryUserRepository.findById(5).orElse(null));
        result.add(reimbursement_05);

        Reimbursement reimbursement_06 = new Reimbursement(
                "reimbursement_006_moreDaysRefund", of(2022,3,21),
                of(2022,3,25), 0,
                false, inMemoryUserRepository.findById(5).orElse(null));
        result.add(reimbursement_06);

        Reimbursement reimbursement_07 = new Reimbursement(
                "reimbursement_007_moreDaysRefundMaxRefund", of(2022,3,11),
                of(2022,3,25), 0,
                false, inMemoryUserRepository.findById(5).orElse(null));
        result.add(reimbursement_07);

        Reimbursement reimbursement_08 = new Reimbursement(
                "reimbursement_008_noCostPerKm", null,
                of(2022,3,30), 100,
                false, inMemoryUserRepository.findById(2).orElse(null));
        result.add(reimbursement_08);

        Reimbursement reimbursement_09 = new Reimbursement(
                "reimbursement_009_noMaxMileage", null,
                of(2022,3,30), 100,
                false, inMemoryUserRepository.findById(3).orElse(null));
        result.add(reimbursement_09);

        Reimbursement reimbursement_10 = new Reimbursement(
                "reimbursement_010_ok", null,
                of(2022,3,30), 100,
                false, inMemoryUserRepository.findById(5).orElse(null));
        result.add(reimbursement_10);

        Reimbursement reimbursement_11 = new Reimbursement(
                "reimbursement_011_noMaxMileage", null,
                of(2022,3,30), 500,
                false, inMemoryUserRepository.findById(5).orElse(null));
        result.add(reimbursement_11);

        Reimbursement reimbursement_12 = new Reimbursement(
                "reimbursement_012_noMaxRefund", of(2022,3,21),
                of(2022,3,30), 100,
                false, inMemoryUserRepository.findById(4).orElse(null));
        result.add(reimbursement_12);

        Reimbursement reimbursement_13 = new Reimbursement(
                "reimbursement_013_ok", of(2022,3,21),
                of(2022,3,30), 100,
                false, inMemoryUserRepository.findById(6).orElse(null));
        result.add(reimbursement_13);
        return result;
    }

    protected static void reimbursementRepositoryInitializeData(InMemoryReimbursementRepository inMemoryReimbursementRepository, List<Reimbursement> entities){
        for (Reimbursement entity : entities) {
            inMemoryReimbursementRepository.save(entity);
        }
    }

    protected static void reimbursementRepositoryInitialDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository, InMemoryReimbursementRepository inMemoryReimbursementRepository) {
        userGroupRepositoryInitialDataAllParams(inMemoryUserGroupRepository);
        userRepositoryInitialDataAllParams(inMemoryUserGroupRepository, inMemoryUserRepository);
        reimbursementRepositoryInitializeData(inMemoryReimbursementRepository, reimbursementsDataAllParams(inMemoryUserRepository));
    }

    protected static void reimbursementRepositoryInitialDataForUserCosts(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository, InMemoryReimbursementRepository inMemoryReimbursementRepository) {
        userRepositoryInitialDataForUserCosts(inMemoryUserGroupRepository, inMemoryUserRepository);
        reimbursementRepositoryInitializeData(inMemoryReimbursementRepository, reimbursementsDataAllParams(inMemoryUserRepository));
    }

    protected static List<UserCost> userCostDataAllParams(InMemoryReimbursementRepository inMemoryReimbursementRepository, InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository) throws ReimbursementNotFoundException {
        List<UserCost> result = new ArrayList<>();
        //UserGroups:       Users:  Reimbursements:
        //  1 -   "fooGroup",   1 - "user_01",  1, 2,3,
        //                          "user_04"   12
        //  2 -   "barGroup",   2 - "user_02",  8
        //                          "user_05"   4, 5, 6, 7, 10, 11
        //  3 -   "foobarGroup" 3 - "user_03",  9
        //                          "user_06"   13
        //ReceiptTypes:
        //  1 - "Train_AllUsers"    - All groups
        //  2 - "Aeroplane_CEO"     - 1
        //  3 - "Food_AllUsers"     - All groups
        //  4 - "Food_CEO"          - 1
        //  5 - "Hotels_Sellers"    - 2
        //  6 - "Other_Directors"   - --


        UserCost userCost_01 = new UserCost();
        userCost_01.setName("receipt_1_train");
        userCost_01.setCostValue(56);
        userCost_01.setReimbursement(inMemoryReimbursementRepository.findById(1)
                .orElseThrow(ReimbursementNotFoundException::new));
        userCost_01.setReceiptType(inMemoryReceiptTypeRepository.findById(1).orElseThrow());
        result.add(userCost_01);

        UserCost userCost_02 = new UserCost();
        userCost_02.setName("receipt_2_food");
        userCost_02.setCostValue(56);
        userCost_02.setReimbursement(inMemoryReimbursementRepository.findById(1)
                .orElseThrow(ReimbursementNotFoundException::new));
        userCost_02.setReceiptType(inMemoryReceiptTypeRepository.findById(3).orElseThrow());
        result.add(userCost_02);

        UserCost userCost_03 = new UserCost();
        userCost_03.setName("user_2_hotel");
        userCost_03.setCostValue(456);
        userCost_03.setReimbursement(inMemoryReimbursementRepository.findById(8)
                .orElseThrow(ReimbursementNotFoundException::new));
        userCost_03.setReceiptType(inMemoryReceiptTypeRepository.findById(5).orElseThrow());
        result.add(userCost_03);

        UserCost userCost_04 = new UserCost();
        userCost_04.setName("user_4_hotel");
        userCost_04.setCostValue(10000);
        userCost_04.setReimbursement(inMemoryReimbursementRepository.findById(8)
                .orElseThrow(ReimbursementNotFoundException::new));
        userCost_04.setReceiptType(inMemoryReceiptTypeRepository.findById(1).orElseThrow());
        result.add(userCost_04);

        UserCost userCost_05 = new UserCost();
        userCost_05.setName("cost");
        userCost_05.setCostValue(23);
        userCost_05.setReimbursement(inMemoryReimbursementRepository.findById(12)
                .orElseThrow(ReimbursementNotFoundException::new));
        userCost_05.setReceiptType(inMemoryReceiptTypeRepository.findById(1).orElseThrow());
        result.add(userCost_05);

        return result;
    }

    protected static void userCostsInitializeData(InMemoryUserCostRepository inMemoryUserCostRepository, List<UserCost> entities){
        for (UserCost entity : entities) {
            inMemoryUserCostRepository.save(entity);
        }
    }

    protected static void userCostInitialDataForUserCosts(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository,
                                                          InMemoryReimbursementRepository inMemoryReimbursementRepository, InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository,
                                                          InMemoryUserCostRepository inMemoryUserCostRepository) throws ReimbursementNotFoundException {
        reimbursementRepositoryInitialDataForUserCosts(inMemoryUserGroupRepository, inMemoryUserRepository, inMemoryReimbursementRepository);
        receiptTypesRepositoryInitialDataForUserCosts(inMemoryReceiptTypeRepository, inMemoryUserGroupRepository);
        userCostsInitializeData(inMemoryUserCostRepository,userCostDataAllParams(inMemoryReimbursementRepository, inMemoryReceiptTypeRepository));
    }
}
