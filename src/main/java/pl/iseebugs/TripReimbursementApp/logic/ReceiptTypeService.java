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
import pl.iseebugs.TripReimbursementApp.model.projection.ReceiptMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.ReceiptTypeReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.ReceiptTypeWriteModel;

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

    public ReceiptTypeReadModel saveReceiptTypeWithUserGroupIds
            (ReceiptTypeWriteModel receiptTypeWriteModel, List<Integer> userGroupIds)
            throws UserGroupNotFoundException{
        if (receiptTypeRepository.existsById(receiptTypeWriteModel.getId())){
            throw new IllegalArgumentException("This Receipt Type already exists");
        }

        ReceiptType receiptType = new ReceiptType();
        receiptType.setName(receiptTypeWriteModel.getName());
        receiptType.setMaxValue(receiptTypeWriteModel.getMaxValue());

        Set<UserGroup> userGroups = new HashSet<>();
        for (Integer userGroupId : userGroupIds) {
            UserGroup userGroup = userGroupRepository.findById(userGroupId)
                    .orElseThrow(UserGroupNotFoundException::new);
            userGroups.add(userGroup);
        }

        receiptType.setUserGroups(userGroups);
        ReceiptType result = receiptTypeRepository.save(receiptType);
        logger.info("Create Receipt Type with ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public ReceiptTypeReadModel saveReceiptTypeToAllUserGroup(ReceiptTypeWriteModel receiptTypeWriteModel) {
        if (receiptTypeRepository.existsById(receiptTypeWriteModel.getId())){
            throw new IllegalArgumentException("This Receipt Type already exists");
        }

        ReceiptType receiptType = new ReceiptType();
        receiptType.setName(receiptTypeWriteModel.getName());
        receiptType.setMaxValue(receiptTypeWriteModel.getMaxValue());

        List<UserGroup> allUserGroups = userGroupRepository.findAll();

        receiptType.setUserGroups(new HashSet<>(allUserGroups));
        ReceiptType result = receiptTypeRepository.save(receiptType);
        logger.info("Create Receipt Type wit ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public ReceiptTypeReadModel updateReceiptTypeWithUserGroupIds
            (ReceiptTypeWriteModel receiptTypeWriteModel, List<Integer> userGroupIds)
            throws ReceiptTypeNotFoundException {
        ReceiptType toUpdate = receiptTypeRepository.findById(receiptTypeWriteModel.getId())
                .orElseThrow(ReceiptTypeNotFoundException::new);

        toUpdate.setName(receiptTypeWriteModel.getName());
        toUpdate.setMaxValue(receiptTypeWriteModel.getMaxValue());

        Set<UserGroup> currentUserGroups = new HashSet<>(toUpdate.getUserGroups());
        Set<UserGroup> newUserGroups = new HashSet<>(userGroupRepository.findAllById(userGroupIds));

        Set<UserGroup> addedUserGroups = new HashSet<>(newUserGroups);
        addedUserGroups.removeAll(currentUserGroups);

        Set<UserGroup> removedUserGroups = new HashSet<>(currentUserGroups);
        removedUserGroups.removeAll(newUserGroups);

        toUpdate.getUserGroups().addAll(addedUserGroups);
        toUpdate.getUserGroups().removeAll(removedUserGroups);

        ReceiptType result = receiptTypeRepository.save(toUpdate);
        logger.info("Updated Receipt Type with ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }

    public void deleteById (int id) throws ReceiptTypeNotFoundException {
        ReceiptType receiptType = receiptTypeRepository.findById(id)
                .orElseThrow(ReceiptTypeNotFoundException::new);

        receiptType.getUserGroups().clear();

        receiptTypeRepository.deleteById(id);

        logger.info("Read All Receipt Type with User Group Id: {}",id);
    }
}
