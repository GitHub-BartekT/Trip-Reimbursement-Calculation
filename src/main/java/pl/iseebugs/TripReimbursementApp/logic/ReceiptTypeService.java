package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.ReceiptType;
import pl.iseebugs.TripReimbursementApp.model.ReceiptTypeRepository;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptTypeReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptTypeReadModelShort;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptTypeWriteModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReceiptTypeService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptTypeService.class);

    private final ReceiptTypeRepository receiptTypeRepository;
    private final UserGroupRepository userGroupRepository;

    public ReceiptTypeService(ReceiptTypeRepository receiptTypeRepository, UserGroupRepository userGroupRepository) {
        this.receiptTypeRepository = receiptTypeRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public List<ReceiptTypeReadModel> readAll(){
        List<ReceiptTypeReadModel> result = receiptTypeRepository.findAll().stream()
                .map(ReceiptMapper::toReadModel).toList();
        logger.info("Read All Receipt Type. List size: {}", result.size());
        return result;
    }

    public List<ReceiptTypeReadModel> readAllByUserGroup_Id(int id) throws UserGroupNotFoundException {
        if (!userGroupRepository.existsById(id)){
            logger.info("No found User Group with id {}: ", id);
            throw new UserGroupNotFoundException();
        }
        List<ReceiptTypeReadModel> result = receiptTypeRepository.findAllByUserGroups_Id(id)
                .stream().map(ReceiptMapper::toReadModel).toList();
        logger.info("Read All Receipt Type with User Id: {}. List size: {}",id,result.size());
        return result;
    }

    public ReceiptTypeReadModel readById(int id) throws ReceiptTypeNotFoundException {
        ReceiptTypeReadModel result = ReceiptMapper.toReadModel(receiptTypeRepository
                .findById(id).orElseThrow(ReceiptTypeNotFoundException::new));
        logger.info("Read Receipt Type with ID: {}", result.getId());
        return result;
    }

    public ReceiptTypeReadModel saveReceiptTypeToAllUserGroup(ReceiptTypeWriteModel receiptTypeWriteModel) {
        if (receiptTypeRepository.existsById(receiptTypeWriteModel.getId())){
            throw new IllegalArgumentException("This Receipt Type already exists");
        }

        ReceiptType receiptType = new ReceiptType();
        receiptType.setName(receiptTypeWriteModel.getName());
        receiptType.setMaxValue(receiptTypeWriteModel.getMaxValue());

        receiptType = receiptTypeRepository.save(receiptType);

        List<UserGroup> allUserGroups = userGroupRepository.findAll();

        for(UserGroup userGroup : allUserGroups) {
            receiptType.getUserGroups().add(userGroup);
            userGroup.getReceiptTypes().add(receiptType);
            userGroupRepository.save(userGroup);
        }

        ReceiptType result = receiptTypeRepository.save(receiptType);
        logger.info("Create Receipt Type wit ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public ReceiptTypeReadModel saveReceiptTypeWithUserGroupIds
            (ReceiptTypeWriteModel receiptTypeWriteModel, List<Integer> userGroupIds)
            throws UserGroupNotFoundException{
        if (receiptTypeRepository.existsById(receiptTypeWriteModel.getId())){
            throw new IllegalArgumentException("This Receipt Type already exists");
        }

        ReceiptType receiptType = new ReceiptType();
        receiptType.setName(receiptTypeWriteModel.getName());
        receiptType.setMaxValue(receiptTypeWriteModel.getMaxValue());

        receiptType = receiptTypeRepository.save(receiptType);

        for (Integer id : userGroupIds) {
            UserGroup userGroup = userGroupRepository.findById(id)
                    .orElseThrow(UserGroupNotFoundException::new);
            receiptType.getUserGroups().add(userGroup);
            userGroup.getReceiptTypes().add(receiptType);
            userGroupRepository.save(userGroup);
        }

        ReceiptType result = receiptTypeRepository.save(receiptType);
        logger.info("Create Receipt Type with ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public ReceiptTypeReadModel updateReceiptType (ReceiptTypeWriteModel receiptTypeWriteModel)
            throws ReceiptTypeNotFoundException, UserGroupNotFoundException {
        ReceiptType toUpdate = receiptTypeRepository.findById(receiptTypeWriteModel.getId())
                .orElseThrow(ReceiptTypeNotFoundException::new);

        toUpdate.setName(receiptTypeWriteModel.getName());
        toUpdate.setMaxValue(receiptTypeWriteModel.getMaxValue());

        List<Integer> userGroupIds = toUpdate.getUserGroups().stream().map(UserGroup::getId).toList();

        for (int userGroupId : userGroupIds){
            UserGroup userGroup = userGroupRepository.findById(userGroupId)
                    .orElseThrow(UserGroupNotFoundException::new);
            userGroup.getReceiptTypes().add(toUpdate);
            userGroupRepository.save(userGroup);
        }

        ReceiptType result = receiptTypeRepository.save(toUpdate);
        logger.info("Updated Receipt Type with ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public ReceiptTypeReadModel updateReceiptTypeWithUserGroupIds
            (ReceiptTypeWriteModel receiptTypeWriteModel, List<Integer> userGroupIds)
            throws ReceiptTypeNotFoundException, UserGroupNotFoundException {
        ReceiptType toUpdate = receiptTypeRepository.findById(receiptTypeWriteModel.getId())
                .orElseThrow(ReceiptTypeNotFoundException::new);

        Set<UserGroup> currentUserGroups = toUpdate.getUserGroups();
        Set<UserGroup> newUserGroups =
                new HashSet<>(userGroupRepository.findAllById(userGroupIds));

        for (UserGroup userGroup : currentUserGroups){
            if(!newUserGroups.contains(userGroup)){
                userGroup.getReceiptTypes().add(toUpdate);
                userGroupRepository.save(userGroup);
            }
        }

        for(UserGroup userGroup : newUserGroups){
            if(!currentUserGroups.contains(userGroup)){
                userGroup.getReceiptTypes().remove(toUpdate);
                userGroupRepository.save(userGroup);
            }
        }

        toUpdate.setUserGroups(newUserGroups);
        toUpdate.setName(receiptTypeWriteModel.getName());
        toUpdate.setMaxValue(receiptTypeWriteModel.getMaxValue());


        ReceiptType result = receiptTypeRepository.save(toUpdate);
        logger.info("Updated Receipt Type with ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public ReceiptTypeReadModelShort updateReceiptTypeAddUserGroupsIds(int receiptTypeId, List<Integer> userGroupsIds) throws ReceiptTypeNotFoundException {
        ReceiptType toUpdate = receiptTypeRepository.findById(receiptTypeId)
                .orElseThrow(ReceiptTypeNotFoundException::new);

        Set<UserGroup> userGroupsToAdd =
                new HashSet<>(userGroupRepository.findAllById(userGroupsIds));

        for (UserGroup userGroup : userGroupsToAdd){
            if(!toUpdate.getUserGroups().contains(userGroup)){
                toUpdate.getUserGroups().add(userGroup);
                userGroup.getReceiptTypes().add(toUpdate);
                userGroupRepository.save(userGroup);
            }
        }

        ReceiptType updatedReceiptType = receiptTypeRepository.save(toUpdate);
        logger.info("Updated Receipt Type with ID: {}, user groups size: {}",
                updatedReceiptType.getId(), updatedReceiptType.getUserGroups().size());
        return ReceiptMapper.toReadModelShort(updatedReceiptType);
    }

    public void deleteById (int id) throws ReceiptTypeNotFoundException {
        ReceiptType toDelete = receiptTypeRepository.findById(id)
                .orElseThrow(ReceiptTypeNotFoundException::new);

        Set<UserGroup> currentUserGroups =
                new HashSet<>(toDelete.getUserGroups());

        for (UserGroup userGroup : currentUserGroups){
                userGroup.getReceiptTypes().remove(toDelete);
                userGroupRepository.save(userGroup);
            }

        receiptTypeRepository.deleteById(id);
        logger.info("Deleted Receipt Type with Id: {}",id);
    }
}
