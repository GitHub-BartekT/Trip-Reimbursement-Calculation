package pl.iseebugs.TripReimbursementApp.logic;

import pl.iseebugs.TripReimbursementApp.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryRepositories {

    protected static InMemoryUserGroupRepository inMemoryUserGroupRepository(){
        return new InMemoryUserGroupRepository();
    }

    protected static class InMemoryUserGroupRepository implements UserGroupRepository {
        private final AtomicInteger index = new AtomicInteger(1);

        private final Map<Integer, UserGroup> map = new HashMap<>();

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
        public List<UserGroup> findAllById(Iterable<Integer> idList) {
            List<UserGroup> result = new ArrayList<>();
            for (Integer id : idList) {
                UserGroup userGroup = map.get(id);
                if (userGroup != null) {
                    result.add(userGroup);
                }
            }
            return result;
        }

        @Override
        public List<UserGroup> findAllByReceiptTypes_Id(int id) {
            List<UserGroup> result = new ArrayList<>();
            result = map.values().stream()
                    .filter(userGroup -> userGroup.getReceiptTypes().stream()
                            .anyMatch(receiptType -> receiptType.getId() == id))
                    .toList();
            return result;
        }

        @Override
        public boolean existsByName(String name) {
            return map.values().stream()
                    .anyMatch(userGroup -> name.equals(userGroup.getName()));
        }

        @Override
        public boolean existsById(int id) {
            return map.containsKey(id);
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


    protected static InMemoryReceiptTypeRepository inMemoryReceiptTypeRepository(){
        return new InMemoryReceiptTypeRepository();
    }

    protected static class InMemoryReceiptTypeRepository implements ReceiptTypeRepository {
        private final AtomicInteger index = new AtomicInteger(1);

        private final Map<Integer, ReceiptType> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<ReceiptType> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<ReceiptType> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public List<ReceiptType> findAllById(Iterable<Integer> idList) {
            List<ReceiptType> result = new ArrayList<>();
            for (Integer id : idList) {
                ReceiptType receiptType = map.get(id);
                if (receiptType != null) {
                    result.add(receiptType);
                }
            }
            return result;
        }

        @Override
        public List<ReceiptType> findAllByUserGroups_Id(int id) {
            List<ReceiptType> result = new ArrayList<>();
            result = map.values().stream()
                    .filter(receiptType -> receiptType.getUserGroups().stream()
                            .anyMatch(userGroup -> userGroup.getId() == id))
                    .toList();
            return result;
        }

        @Override
        public boolean existsById(int id) {
            return map.containsKey(id);
        }

        @Override
        public ReceiptType save(ReceiptType entity) {
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

    protected static InMemoryUserCostRepository inMemoryUserCostRepository(){
        return new InMemoryUserCostRepository();
    }

    protected static class InMemoryUserCostRepository implements UserCostRepository {
        private final AtomicInteger index = new AtomicInteger(1);
        private final Map<Integer, UserCost> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<UserCost> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<UserCost> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public List<UserCost> findAllByReimbursement_Id(int reimbursements_id) {
            return map.values().stream()
                    .filter((userCost) -> userCost.getReimbursement().getId() == reimbursements_id)
                    .collect(Collectors.toList());
        }

        @Override
        public List<UserCost> findAllByReceiptType_Id(int receiptTypeId) {
            return map.values().stream()
                    .filter((userCost) -> userCost.getReceiptType().getId() == receiptTypeId)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean existsById(int id) {
            return map.containsKey(id);
        }

        @Override
        public UserCost save(UserCost entity) {
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
