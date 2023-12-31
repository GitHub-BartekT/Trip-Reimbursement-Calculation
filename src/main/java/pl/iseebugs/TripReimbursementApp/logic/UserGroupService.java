package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.*;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    private static final Logger logger = LoggerFactory.getLogger(UserGroupService.class);
    private final UserGroupRepository repository;
    private final UserRepository userRepository;
    private final ReceiptTypeRepository receiptTypeRepository;

    @Autowired
    public UserGroupService(UserGroupRepository repository, UserRepository userRepository, ReceiptTypeRepository receiptTypeRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.receiptTypeRepository = receiptTypeRepository;
    }


    public List<UserGroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(UserGroupMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public List<UserGroupReadModelShort> readAllByReceiptType_Id(int id) throws ReceiptTypeNotFoundException {
        if (!receiptTypeRepository.existsById(id)){
            logger.info("No found Receipt Type with id {}: ", id);
            throw new ReceiptTypeNotFoundException();
        }
        List<UserGroupReadModelShort> result = repository.findAllByReceiptTypes_Id(id)
                .stream().map(UserGroupMapper::toReadModelShort).toList();
        logger.info("Read All User Group with Receipt Id: {}. List size: {}",id,result.size());
        return result;
    }

    public UserGroupReadModelFull readById(int id) throws UserGroupNotFoundException {
        UserGroupReadModelFull toRead = repository.findById(id).map(UserGroupMapper::toReadModelFull)
                .orElseThrow(UserGroupNotFoundException::new);
        logger.info("Read User Group with ID {}", toRead.getId());
        return toRead;
    }

    public UserGroupReadModel createUserGroup(UserGroupWriteModel group){
        if(repository.findById(group.getId()).isPresent()){
            throw new IllegalArgumentException("This User Group already exists.");
        } else if (repository.existsByName(group.getName())){
            throw new IllegalArgumentException("User Group with that name already exist.");
        }
        UserGroup userGroup = repository.save(group.toUserGroup());
        logger.info("Created user group with ID {}", userGroup.getId());
        return UserGroupMapper.toReadModel(userGroup);
    }

    public UserGroupReadModel updateUserGroupById(UserGroupWriteModel toWrite) throws UserGroupNotFoundException{
        if(repository.findById(toWrite.getId()).isEmpty()){
            throw new UserGroupNotFoundException();
        }

        UserGroup groupWithSameName = repository.findByName(toWrite.getName());
        if (groupWithSameName != null && (groupWithSameName.getId() != toWrite.getId())) {
            throw new IllegalArgumentException("User Group with that name already exist.");
        }

        UserGroup toUpdate = repository.save(toWrite.toUserGroup());
        return UserGroupMapper.toReadModel(toUpdate);
    }

    public UserGroupReadModelFull updateUserGroupWithReceiptTypesIds
            (UserGroupWriteModel userGroupWriteModel, List<Integer> receiptTypesIds) throws UserGroupNotFoundException {
        UserGroup toUpdate = repository.findById(userGroupWriteModel.getId())
                .orElseThrow(UserGroupNotFoundException::new);
        if (repository.existsByName(userGroupWriteModel.getName())) {
            throw new IllegalArgumentException("User Group with that name already exist.");
        }

        Set<ReceiptType> currentReceiptTypes = toUpdate.getReceiptTypes();
        Set<ReceiptType> newReceiptTypes =
                new HashSet<>(receiptTypeRepository.findAllById(receiptTypesIds));

        for (ReceiptType receiptType : currentReceiptTypes){
            if (!newReceiptTypes.contains(receiptType)){
                receiptType.getUserGroups().add(toUpdate);
                receiptTypeRepository.save(receiptType);
            }
        }

        for (ReceiptType receiptType : newReceiptTypes){
            if (!currentReceiptTypes.contains(receiptType)){
                receiptType.getUserGroups().remove(toUpdate);
                receiptTypeRepository.save(receiptType);
            }
        }

        toUpdate.setName(userGroupWriteModel.getName());
        toUpdate.setDailyAllowance(userGroupWriteModel.getDailyAllowance());
        toUpdate.setCostPerKm(userGroupWriteModel.getCostPerKm());
        toUpdate.setMaxMileage(userGroupWriteModel.getMaxMileage());
        toUpdate.setMaxRefund(userGroupWriteModel.getMaxRefund());
        toUpdate.setReceiptTypes(newReceiptTypes);

        UserGroup result = repository.save(toUpdate);
        logger.info("Updated UserGroup with ID: {}", result.getId());
        return UserGroupMapper.toReadModelFull(result);
   }

    public UserGroupReadModelFull updateUserGroupAddReceiptTypesIds
            (int userGroupId, List<Integer> receiptTypesIds) throws UserGroupNotFoundException {
        UserGroup toUpdate = repository.findById(userGroupId)
                .orElseThrow(UserGroupNotFoundException::new);

        Set<ReceiptType> currentReceiptTypes = toUpdate.getReceiptTypes();
        Set<ReceiptType> newReceiptTypes =
                new HashSet<>(receiptTypeRepository.findAllById(receiptTypesIds));

        for (ReceiptType receiptType : currentReceiptTypes){
            if (!newReceiptTypes.contains(receiptType)){
                newReceiptTypes.add(receiptType);
                receiptType.getUserGroups().add(toUpdate);
                receiptTypeRepository.save(receiptType);
            }
        }

        toUpdate.setReceiptTypes(newReceiptTypes);

        UserGroup result = repository.save(toUpdate);
        logger.info("Updated UserGroup with ID: {}, receipt size: {}",
                result.getId(), toUpdate.getReceiptTypes().size());
        return UserGroupMapper.toReadModelFull(result);
    }

    public UserGroupReadModelFull updateUserGroupRemoveReceiptTypesIds
            (int userGroupId, List<Integer> receiptTypesIds) throws UserGroupNotFoundException {
        UserGroup toUpdate = repository.findById(userGroupId)
                .orElseThrow(UserGroupNotFoundException::new);

        Set<ReceiptType> currentReceiptTypes = toUpdate.getReceiptTypes();
        Set<ReceiptType> newReceiptTypes =
                new HashSet<>(receiptTypeRepository.findAllById(receiptTypesIds));

        for (ReceiptType receiptType : newReceiptTypes){
            if (currentReceiptTypes.contains(receiptType)){
                currentReceiptTypes.remove(receiptType);
                receiptType.getUserGroups().remove(toUpdate);
                receiptTypeRepository.save(receiptType);
            }
        }

        UserGroup result = repository.save(toUpdate);
        logger.info("Updated UserGroup with ID: {}", result.getId());
        return UserGroupMapper.toReadModelFull(result);
    }

    public void deleteUserGroup(int id) throws UserGroupNotFoundException {
        UserGroup toDelete = repository.findById(id).orElseThrow(UserGroupNotFoundException::new);

        try {
            if (userRepository.existsByUserGroup_Id(id)){
                throw new IllegalStateException("User Group contains user.");
            }
            Set<ReceiptType> currentReceiptTypes = toDelete.getReceiptTypes();

            for (ReceiptType receiptType : currentReceiptTypes){
                receiptType.getUserGroups().remove(toDelete);
                receiptTypeRepository.save(receiptType);
            }

            repository.deleteById(id);
            logger.info("Deleted user group with ID {}", id);
        } catch (Exception e){
            logger.error("Error deleting user group with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}
