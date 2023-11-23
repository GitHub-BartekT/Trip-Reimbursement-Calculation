package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.model.Reimbursement;
import pl.iseebugs.TripReimbursementApp.model.ReimbursementRepository;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementMapper;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementWriteModel;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReimbursementService {

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);
    public final ReimbursementRepository repository;
    public UserRepository userRepository;

    public ReimbursementService(ReimbursementRepository repository, UserRepository userRepository) {
        this.repository = repository;
    }

    public List<ReimbursementReadModel> readAll() {
        return repository.findAll().stream()
                .map(ReimbursementMapper::toReadModel)
                .collect(Collectors.toList());
    }

    public ReimbursementReadModel readById(int id) throws ReimbursementNotFoundException {
        Reimbursement reimbursement =
                repository.findById(id).orElseThrow(ReimbursementNotFoundException::new);
        ReimbursementReadModel toRead = ReimbursementMapper.toReadModel(reimbursement);
        logger.info("Read reimbursement with ID {}, User ID {}", toRead.getId(), toRead.getUserId());
        return toRead;
    }

    public ReimbursementReadModel createReimbursement(ReimbursementWriteModel toWrite) throws UserNotFoundException {
        if (repository.findById(toWrite.getId()).isPresent()) {
            throw new IllegalArgumentException("This Reimbursement already exists.");
        }  else if (!userRepository.existsById(toWrite.getUserId())) {
            throw new UserNotFoundException();
        }
        return ReimbursementMapper.toReadModel(repository.save(toEntity(toWrite)));
    }

    public ReimbursementReadModel updateReimbursementById(ReimbursementWriteModel toUpdate) throws ReimbursementNotFoundException, UserNotFoundException {
        if (repository.findById(toUpdate.getId()).isEmpty()) {
            throw new ReimbursementNotFoundException();
        } else if (!userRepository.existsById(toUpdate.getUserId())) {
            throw new UserNotFoundException();
        }
        return ReimbursementMapper.toReadModel(repository.save(toEntity(toUpdate)));
    }

    public void deleteReimbursementById(int id) throws ReimbursementNotFoundException {
        repository.findById(id).orElseThrow(ReimbursementNotFoundException::new);

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

    private static void validate(ReimbursementWriteModel toValidate){
        if (toValidate.getStartDate() != null && isValidDate(toValidate.getEndDate())){
            throw new java.time.DateTimeException("Wrong data.");
        }else if (isValidDate(toValidate.getEndDate())){
            throw new java.time.DateTimeException("Wrong data.");
        }
    }

    public Reimbursement toEntity(ReimbursementWriteModel reimbursementWriteModel) {
        validate(reimbursementWriteModel);

        var result = new Reimbursement();
        result.setId(reimbursementWriteModel.getId());
        result.setName(reimbursementWriteModel.getName());
        result.setStartDate(reimbursementWriteModel.getStartDate());
        result.setEndDate(reimbursementWriteModel.getEndDate());
        result.setDistance(reimbursementWriteModel.getDistance());
        result.setPushedToAccept(reimbursementWriteModel.isPushedToAccept());

        User user = userRepository.findById(reimbursementWriteModel.getUserId()).orElse(null);
        assert user != null;
        result.setUser(user);
        return result;
    }
}
