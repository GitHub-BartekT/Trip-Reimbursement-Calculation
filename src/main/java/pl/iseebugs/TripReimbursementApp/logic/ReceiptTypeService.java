package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
        ReceiptTypeReadModel result = ReceiptMapper.toReadModel(receiptTypeRepository.findById(id).orElseThrow(ReceiptTypeNotFoundException::new));
        logger.info("Read Receipt Type with ID: {}", result.getId());
        return result;
    }


    public ReceiptTypeReadModel saveReceiptTypeWithUserGroupIds(ReceiptTypeWriteModel receiptTypeWriteModel, List<Integer> userGroupIds) throws UserGroupNotFoundException {
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

    public ReceiptTypeReadModel saveReceiptTypeToAllUserGroup(ReceiptTypeWriteModel receiptTypeWriteModel) throws UserGroupNotFoundException {
        ReceiptType receiptType = new ReceiptType();
        receiptType.setName(receiptTypeWriteModel.getName());
        receiptType.setMaxValue(receiptTypeWriteModel.getMaxValue());

        List<UserGroup> allUserGroups = userGroupRepository.findAll();

        if (allUserGroups.isEmpty()){
            throw new UserGroupNotFoundException("No UserGroups found.");
        }

        receiptType.setUserGroups(new HashSet<>(allUserGroups));
        ReceiptType result = receiptTypeRepository.save(receiptType);
        logger.info("Create Receipt Type wit ID: {}", result.getId());
        return ReceiptMapper.toReadModel(result);
    }
}
