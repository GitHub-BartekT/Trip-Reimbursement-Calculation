package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.exception.ReimbursementNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.ReimbursementRepository;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementReadModelShort;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementWriteModel;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReimbursementService {

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);
    public final ReimbursementRepository repository;
    public UserRepository userRepository;

    @Autowired
    public ReimbursementService(ReimbursementRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<ReimbursementReadModel> readAll() {
        return repository.findAll().stream()
                .map(ReimbursementMapper::toReadModel)
                .collect(Collectors.toList());
    }



    public List<ReimbursementReadModel> readAllByUser_Id(int id) throws UserNotFoundException {
        if (!userRepository.existsById(id)){
           throw new UserNotFoundException();
        }
        return repository.findAllByUser_Id(id).stream()
                .map(ReimbursementMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public List<ReimbursementReadModelShort> readAllBySendForApprovalAndUser_Id(boolean pushedToAccept, int user_id)
            throws UserNotFoundException {
        if (!userRepository.existsById(user_id)){
            throw new UserNotFoundException();
        }

        return repository.findAllByPushedToAcceptAndUser_Id(pushedToAccept, user_id).stream()
                .map(ReimbursementMapper::toReadModelShort)
                .collect(Collectors.toList());
    }

    public List<ReimbursementReadModelShort> readAllShort() {
        return repository.findAll().stream()
                .map(ReimbursementMapper::toReadModelShort)
                .collect(Collectors.toList());
    }

    public List<ReimbursementReadModelShort> readAllShortByUser_Id(int id) throws UserNotFoundException {
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException();
        }
        return repository.findAllByUser_Id(id).stream()
                .map(ReimbursementMapper::toReadModelShort)
                .collect(Collectors.toList());
    }

    public ReimbursementReadModel readById(int id) throws ReimbursementNotFoundException {
        Reimbursement reimbursement =
                repository.findById(id).orElseThrow(ReimbursementNotFoundException::new);
        ReimbursementReadModel toRead = ReimbursementMapper.toReadModel(reimbursement);
        logger.info("Read reimbursement with ID {}, User ID {}", toRead.getId(), toRead.getUserId());
        return toRead;
    }

    public ReimbursementReadModel createReimbursement(ReimbursementWriteModel toCreate) throws UserNotFoundException {
        if (repository.existsById(toCreate.getId())) {
            throw new IllegalArgumentException("This Reimbursement already exists.");
        } else if (!userRepository.existsById(toCreate.getUserId())) {
            throw new UserNotFoundException();
        }
        return ReimbursementMapper.toReadModel(repository.save(toEntity(toCreate)));
    }

    public ReimbursementReadModel updateReimbursementById(ReimbursementWriteModel toUpdate) throws ReimbursementNotFoundException, UserNotFoundException {
        if (!repository.existsById(toUpdate.getId())) {
            throw new ReimbursementNotFoundException();
        } else if (!userRepository.existsById(toUpdate.getUserId())) {
            throw new UserNotFoundException();
        }
        return ReimbursementMapper.toReadModel(repository.save(toEntity(toUpdate)));
    }

    public ReimbursementReadModel setSendForApprovalToTrue(int id) throws ReimbursementNotFoundException {
        Reimbursement reimbursement = repository.findById(id).orElseThrow(ReimbursementNotFoundException::new);
        reimbursement.setPushedToAccept(true);
        return ReimbursementMapper.toReadModel(repository.save(reimbursement));
    }

    public void deleteReimbursementById(int id) throws ReimbursementNotFoundException {
        if (!repository.existsById(id)){
            throw new ReimbursementNotFoundException();
        }

        try {
            repository.deleteById(id);
            logger.info("Deleted reimbursement with ID {}", id);
        } catch (Exception e) {
            logger.error("Error deleting reimbursement with ID {}: {}", id, e.getMessage());
        }
    }

    private static boolean isValidDate(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    public static void validate(ReimbursementWriteModel toValidate){
        if (toValidate.getStartDate() != null && !isValidDate(toValidate.getStartDate())) {
            throw new java.time.DateTimeException("Wrong date data.");
        } else if (toValidate.getEndDate() == null) {
                throw new IllegalArgumentException("No end date.");
        } else if (!isValidDate(toValidate.getEndDate())) {
            throw new java.time.DateTimeException("Wrong date data.");
        } else if (toValidate.getStartDate() != null && toValidate.getStartDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Start date should be in the past or present.");
        } else if (toValidate.getEndDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("End date should be in the past or present.");
        } else if (toValidate.getStartDate() != null && toValidate.getStartDate().isAfter(toValidate.getEndDate())) {
            throw new IllegalArgumentException("Start Day is after End Day.");
        }

          else if (toValidate.getName() == null || toValidate.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Reimbursement name couldn't be empty.");
        } else if (toValidate.getName().length() > 100){
            throw new IllegalArgumentException("Reimbursement name is too long.");
        } else if (toValidate.getDistance() < 0){
            throw new IllegalArgumentException("Distance should be positive.");
        }
    }

    public Reimbursement toEntity(ReimbursementWriteModel reimbursementWriteModel) throws UserNotFoundException {
        validate(reimbursementWriteModel);

        var result = new Reimbursement();
        result.setId(reimbursementWriteModel.getId());
        result.setName(reimbursementWriteModel.getName());
        result.setStartDate(reimbursementWriteModel.getStartDate());
        result.setEndDate(reimbursementWriteModel.getEndDate());
        result.setDistance(reimbursementWriteModel.getDistance());
        result.setPushedToAccept(reimbursementWriteModel.isPushedToAccept());

        User user = userRepository
                .findById(reimbursementWriteModel.getUserId())
                        .orElseThrow(UserNotFoundException::new);
        result.setUser(user);
        return result;
    }
}
