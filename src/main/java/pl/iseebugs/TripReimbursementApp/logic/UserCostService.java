package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.ReimbursementNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserCostNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.*;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCostService {

    private static final Logger logger = LoggerFactory.getLogger(UserCostRepository.class);
    private final UserCostRepository userCostRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final ReceiptTypeRepository receiptTypeRepository;

    public UserCostService(UserCostRepository userCostRepository, ReimbursementRepository reimbursementRepository, ReceiptTypeRepository receiptTypeRepository) {
        this.userCostRepository = userCostRepository;
        this.reimbursementRepository = reimbursementRepository;
        this.receiptTypeRepository = receiptTypeRepository;
    }

    public List<UserCostReadModel> readAll(){
        return userCostRepository.findAll().stream()
                .map(UserCostMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public UserCostReadModel readById(int id) throws UserCostNotFoundException {
        UserCostReadModel toRead = userCostRepository.findById(id).map(UserCostMapper::toReadModel)
                .orElseThrow(UserCostNotFoundException::new);
        logger.info("Read User Group with ID {}", toRead.getId());
        return toRead;
    }

    public List<UserCostReadModel> readAllByReimbursement_Id(int id) throws ReimbursementNotFoundException {
        if (!reimbursementRepository.existsById(id)){
            throw new ReimbursementNotFoundException();
        }
        return userCostRepository.findAllByReimbursement_Id(id).stream()
                .map(UserCostMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public List<UserCostReadModel> readAllByReceiptType_Id(int id) throws ReceiptTypeNotFoundException {
        if (!receiptTypeRepository.existsById(id)){
            throw new ReceiptTypeNotFoundException();
        }
        return userCostRepository.findAllByReceiptType_Id(id).stream()
                .map(UserCostMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public UserCostReadModel createUserCost(UserCostWriteModel toCreate) throws Exception {
        if (userCostRepository.existsById(toCreate.getId())) {
            throw new IllegalArgumentException("This User Cost already exists.");
        }
        return UserCostMapper.toReadModel(userCostRepository.save(toEntity(toCreate)));
    }

    public UserCostReadModel updateUserCost(UserCostWriteModel toUpdate) throws Exception {
        if (!userCostRepository.existsById(toUpdate.getId())) {
            throw new UserCostNotFoundException();
        }
        return UserCostMapper.toReadModel(userCostRepository.save(toEntity(toUpdate)));
    }

    public void deleteById(int id) throws ReimbursementNotFoundException, UserCostNotFoundException {
        if (!userCostRepository.existsById(id)){
            throw new UserCostNotFoundException();
        }
        try {
            userCostRepository.deleteById(id);
            logger.info("Deleted User Cost with ID {}", id);
        } catch (Exception e) {
            logger.error("Error deleting User Cost with ID {}: {}", id, e.getMessage());
        }
    }

    private UserCost toEntity(UserCostWriteModel toWrite) throws ReceiptTypeNotFoundException, ReimbursementNotFoundException {
        UserCost userCost = new UserCost();

        userCost.setId(toWrite.getId());
        userCost.setName(toWrite.getName());
        userCost.setCostValue(toWrite.getCostValue());
        Reimbursement reimbursement = reimbursementRepository
                .findById(toWrite.getReimbursementId())
                .orElseThrow(ReimbursementNotFoundException::new);
        userCost.setReimbursement(reimbursement);
        ReceiptType receiptType = receiptTypeRepository
                .findById(toWrite.getReceiptTypeId())
                .orElseThrow(ReceiptTypeNotFoundException::new);
        userCost.setReceiptType(receiptType);
        return userCost;
    }
}
