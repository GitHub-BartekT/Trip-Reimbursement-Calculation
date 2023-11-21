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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReimbursementService {

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);
    public final ReimbursementRepository repository;

    public ReimbursementService(ReimbursementRepository repository) {
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

    public ReimbursementReadModel createReimbursementById(ReimbursementWriteModel toWrite) throws ReimbursementNotFoundException {
        if (repository.findById(toWrite.getId()).isPresent()) {
            throw new IllegalArgumentException("This Reimbursement already exists.");
        }
        ReimbursementReadModel toRead =
                ReimbursementMapper.toReadModel(repository.save(toEntity(toWrite)));
        return toRead;
    }

    public ReimbursementReadModel updateReimbursementById(ReimbursementWriteModel toUpdate) throws ReimbursementNotFoundException {
        if (repository.findById(toUpdate.getId()).isEmpty()) {
            throw new ReimbursementNotFoundException();
        }
        ReimbursementReadModel toRead =
                ReimbursementMapper.toReadModel(repository.save(toEntity(toUpdate)));
        return toRead;
    }

    public void deleteReimbursement(int id) throws ReimbursementNotFoundException {
        repository.findById(id).orElseThrow(ReimbursementNotFoundException::new);

        try {
            repository.deleteById(id);
            logger.info("Deleted reimbursement with ID {}", id);
        } catch (Exception e) {
            logger.error("Error deleting reimbursement with ID {}: {}", id, e.getMessage());
        }
    }

    public static Reimbursement toEntity(ReimbursementWriteModel reimbursementWriteModel) {
        var result = new Reimbursement();
        result.setId(reimbursementWriteModel.getId());
        result.setName(reimbursementWriteModel.getName());
        result.setStartDate(reimbursementWriteModel.getStartDate());
        result.setEndDate(reimbursementWriteModel.getEndDate());
        result.setDistance(reimbursementWriteModel.getDistance());
        result.setPushedToAccept(reimbursementWriteModel.isPushedToAccept());

        UserRepository repository = null;
        User user = new User();
        user = repository.findById(reimbursementWriteModel.getUserId()).orElse(null);
        assert user != null;
        result.setUser(user);
        return result;
    }
}
