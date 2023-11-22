package pl.iseebugs.TripReimbursementApp.logic;

import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.UserDTO;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupDTO;

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


    protected static void userGroupRepositoryWith (InMemoryUserGroupRepository inMemoryUserGroupRepository, List<String> entities){
        for (String entity : entities) {
            UserGroupDTO userGroup = new UserGroupDTO();
            userGroup.setName(entity);
            inMemoryUserGroupRepository.save(userGroup.toUserGroup());
        }
    }

    protected static void userRepositoryWith(InMemoryUserGroupRepository inMemoryUserGroupRepository, InMemoryUserRepository inMemoryUserRepository, List<String> userGroups, List<String> users) throws UserGroupNotFoundException {
        for (String entity : userGroups) {
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            userGroupDTO.setName(entity);
            UserGroup userGroup = inMemoryUserGroupRepository.save(userGroupDTO.toUserGroup());
            for (String entityUser : users) {
                UserDTO user = new UserDTO();
                user.setName(entityUser);
                user.setUserGroup(new UserGroupDTO(userGroup));
                inMemoryUserRepository.save(user.toUser());
            }
        }
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
        public List<User> findAllByUserGroup_Id(Integer userGroupId) {
            return map.values().stream()
                    .filter((user) -> user.getUserGroup().getId() == userGroupId)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean existsByUserGroup_Id(int id) {
            return false;
        }

        @Override
        public Optional<User> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
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
        }

        @Override
        public boolean existsById(int id) {
            return map.containsKey(id);
        }
    }
}
