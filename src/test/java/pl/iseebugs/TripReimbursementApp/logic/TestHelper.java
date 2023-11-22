package pl.iseebugs.TripReimbursementApp.logic;

import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.InMemoryUserGroupRepository;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.InMemoryUserRepository;

public class TestHelper {

    protected static String createLongString(int length){
        if (length <=0 ){
            return "";
        }
        return String.valueOf('A').repeat(length);
    }

    private static List<String> userGroupsDataOnlyNames(){
    return List.of("fooGroup","barGroup", "foobarGroup");
    }

    private static List<UserGroup> userGroupsDataOnlyWithNames(){
        List<String> userGroupsNames = userGroupsDataOnlyNames();
        List<UserGroup> result = new ArrayList<>();
        for (String entity : userGroupsNames) {
            UserGroup userGroup = new UserGroup();
            userGroup.setName(entity);
            result.add(userGroup);
        };
        return result;
    }

    private static List<UserGroup> userGroupsDataAllParams(){
        List<UserGroup> result = new ArrayList<>();
        UserGroup userGroup_01 = new UserGroup();
        userGroup_01.setName("group_001_NoDailyAllowance");
        userGroup_01.setDailyAllowance(0);
        userGroup_01.setCostPerKm(0.1);
        userGroup_01.setMaxMileage(1000);
        userGroup_01.setMaxRefund(10);
        result.add(userGroup_01);

        UserGroup userGroup_02 = new UserGroup();
        userGroup_02.setName("group_002_NoCostPerKm");
        userGroup_02.setDailyAllowance(10);
        userGroup_02.setCostPerKm(0.1);
        userGroup_02.setMaxMileage(1000);
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
        userGroup_05.setMaxMileage(100);
        userGroup_05.setMaxRefund(100);
        result.add(userGroup_05);

        return result;
    }

    protected static void userGroupRepositoryInitializeData(InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository, List<UserGroup> entities){
        for (UserGroup entity : entities) {
            inMemoryUserGroupRepository.save(entity);
        }
    }

    protected static void userGroupRepositoryInitialDataOnlyNames(InMemoryRepositories.InMemoryUserGroupRepository inMemoryUserGroupRepository){
        userGroupRepositoryInitializeData(inMemoryUserGroupRepository, userGroupsDataOnlyWithNames());
    }

    protected static void userGroupRepositoryInitialDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository){
        userGroupRepositoryInitializeData(inMemoryUserGroupRepository, userGroupsDataAllParams());
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
        user_01.setName("user_noDailyRefund");
        user_01.setUserGroup(inMemoryUserGroupRepository.findById(1).orElse(null));
        result.add(user_01);

        User user_02 = new User();
        user_02.setName("user_noCostPerKm");
        user_02.setUserGroup(inMemoryUserGroupRepository.findById(2).orElse(null));
        result.add(user_02);

        User user_03 = new User();
        user_03.setName("user_noMaxMileage");
        user_03.setUserGroup(inMemoryUserGroupRepository.findById(3).orElse(null));
        result.add(user_03);

        User user_04 = new User();
        user_04.setName("user_noRefund");
        user_04.setUserGroup(inMemoryUserGroupRepository.findById(4).orElse(null));
        result.add(user_04);

        User user_05 = new User();
        user_05.setName("user_ok");
        user_05.setUserGroup(inMemoryUserGroupRepository.findById(5).orElse(null));
        result.add(user_05);

        return result;
    }

    protected static void userRepositoryInitializeData(InMemoryUserRepository inMemoryUserRepository, List<User> entities){
        for (User entity : entities) {
            inMemoryUserRepository.save(entity);
        }
    }

    protected static void userRepositoryInitialDataOnlyNames(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository) throws UserGroupNotFoundException {
        userGroupRepositoryInitialDataOnlyNames(inMemoryUserGroupRepository);
        userRepositoryInitializeData(inMemoryUserRepository, userDataOnlyWithNames(inMemoryUserGroupRepository));
    }

    protected static void userRepositoryInitialDataAllParams(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository) throws UserGroupNotFoundException {
        userGroupRepositoryInitialDataAllParams(inMemoryUserGroupRepository);
        userRepositoryInitializeData(inMemoryUserRepository, userDataAllParams(inMemoryUserGroupRepository));
    }
}
