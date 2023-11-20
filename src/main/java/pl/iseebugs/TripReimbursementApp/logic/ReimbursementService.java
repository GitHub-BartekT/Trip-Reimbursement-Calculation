package pl.iseebugs.TripReimbursementApp.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.iseebugs.TripReimbursementApp.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReimbursementService {

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);
    public final ReimbursementRepository repository;

    public ReimbursementService(ReimbursementRepository repository) {
        this.repository = repository;
    }

    public List<ReimbursementDTO> readAll(){
        return repository.findAll().stream()
                .map(ReimbursementDTO::new)
                .collect(Collectors.toList());
    }

    public ReimbursementDTO readById(int id) throws ReimbursementNotFoundException {
        ReimbursementDTO toRead = repository.findById(id).map(ReimbursementDTO::new)
                .orElseThrow(ReimbursementNotFoundException::new);
        logger.info("Read reimbursement with ID {}, User ID {}", toRead.getId(), toRead.getUserId());
        return toRead;
    }

    public ReimbursementDTO createReimbursementById(ReimbursementDTO reimbursement) throws ReimbursementNotFoundException{
        if(repository.findById(reimbursement.getId()).isPresent()){
            throw new IllegalArgumentException("This Reimbursement already exists.");
        }
        Reimbursement toUpdate = repository.save(reimbursement.toReimbursement());
        return new ReimbursementDTO(toUpdate);
    }

    public ReimbursementDTO updateReimbursementById(ReimbursementDTO reimbursement) throws ReimbursementNotFoundException{
        if(repository.findById(reimbursement.getId()).isEmpty()){
            throw new ReimbursementNotFoundException();
        }
        Reimbursement toUpdate = repository.save(reimbursement.toReimbursement());
        return new ReimbursementDTO(toUpdate);
    }

    public void deleteReimbursement(int id) throws ReimbursementNotFoundException {
        repository.findById(id).orElseThrow(ReimbursementNotFoundException::new);

        try {
            repository.deleteById(id);
            logger.info("Deleted reimbursement with ID {}", id);
        } catch (Exception e){
            logger.error("Error deleting reimbursement with ID {}: {}", id, e.getMessage());
        }
    }

}
