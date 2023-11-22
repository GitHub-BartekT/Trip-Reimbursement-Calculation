package pl.iseebugs.TripReimbursementApp.logic;

import pl.iseebugs.TripReimbursementApp.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    protected static void userGroupRepositoryInitializeData(InMemoryUserGroupRepository inMemoryUserGroupRepository, List<UserGroup> entities){
        for (UserGroup entity : entities) {
            inMemoryUserGroupRepository.save(entity);
        }
    }

    protected static void userGroupRepositoryInitialDataOnlyNames(InMemoryUserGroupRepository inMemoryUserGroupRepository){
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

    protected static InMemoryUserGroupRepository inMemoryUserGroupRepository(){
        return new InMemoryUserGroupRepository();
    }

    protected static class InMemoryUserGroupRepository implements UserGroupRepository {
        private final AtomicInteger index = new AtomicInteger(1);

        private final Map<Integer,UserGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<UserGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<UserGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public boolean existsByName(String name) {
            return map.values().stream()
                    .anyMatch(userGroup -> name.equals(userGroup.getName()));
        }

        @Override
        public UserGroup save(UserGroup entity) {
            if (entity.getId() == 0) {
                int id = index.getAndIncrement();
                entity.setId(id);
            }
            try {
                map.put(entity.getId(), entity);
            } catch (Exception e){
                throw new RuntimeException("Failed to save the entity to the database.");
            }
            return entity;
        }

        @Override
        public void deleteById(int id) {
            map.remove(id);
        }
        @Override
        public void deleteAll() {
            map.clear();
        }

    }

    protected static InMemoryUserRepository inMemoryUserRepository(){
        return new InMemoryUserRepository();
    }

    protected static class InMemoryUserRepository implements UserRepository {
        private final AtomicInteger index = new AtomicInteger(1);
        private final Map<Integer, User> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<User> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public List<User> findAllByUserGroup_Id(Integer userGroupId) {
            return map.values().stream()
                    .filter((user) -> user.getUserGroup().getId() == userGroupId)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean existsById(int id) {
            return map.containsKey(id);
        }

        @Override
        public boolean existsByUserGroup_Id(int userGroupId) {
            return map.values().stream()
                    .anyMatch((user) -> user.getUserGroup().getId() == userGroupId);
        }

        @Override
        public User save(User entity) {
            if (entity.getId() == 0) {
                int id = index.getAndIncrement();
                entity.setId(id);
            }
            try {
                map.put(entity.getId(), entity);
            } catch (Exception e){
                throw new RuntimeException("Failed to save the entity to the database.");
            }
            return entity;
        }

        @Override
        public void deleteById(int id) {
            map.remove(id);
        }

        @Override
        public void deleteAll() {
            map.clear();
        }
    }

    protected static InMemoryReimbursementRepository inMemoryReimbursementRepository(){
        return new InMemoryReimbursementRepository();
    }

    protected static class InMemoryReimbursementRepository implements ReimbursementRepository {
        private final AtomicInteger index = new AtomicInteger(1);
        private final Map<Integer, Reimbursement> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<Reimbursement> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<Reimbursement> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public List<Reimbursement> findAllByUser_Id(Integer userId) {
            return map.values().stream()
                    .filter((reimbursement) -> reimbursement.getUser().getId() == userId)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean existsById(int id) {
            return map.containsKey(id);
        }

        @Override
        public boolean existsByUser_Id(int userId) {
            return map.values().stream()
                    .anyMatch((reimbursement) -> reimbursement.getUser().getId() == userId);
        }

        @Override
        public Reimbursement save(Reimbursement entity) {
            if (entity.getId() == 0) {
                int id = index.getAndIncrement();
                entity.setId(id);
            }
            try {
                map.put(entity.getId(), entity);
            } catch (Exception e){
                throw new RuntimeException("Failed to save the entity to the database.");
            }
            return entity;
        }

        @Override
        public void deleteById(int id) {
            map.remove(id);
        }

        @Override
        public void deleteAll() {
            map.clear();
        }
    }
}
