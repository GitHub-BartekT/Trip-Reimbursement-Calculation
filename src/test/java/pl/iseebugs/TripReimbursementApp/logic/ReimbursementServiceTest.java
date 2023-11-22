package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementReadModel;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.InMemoryReimbursementRepository;
import static pl.iseebugs.TripReimbursementApp.logic.InMemoryRepositories.inMemoryReimbursementRepository;

class ReimbursementServiceTest {

    @Test
    @DisplayName("should returns empty list when no objects")
    void readAll_returnsEmptyList() throws ReimbursementNotFoundException{
        //given
        InMemoryReimbursementRepository inMemoryReimbursementRepository =
                inMemoryReimbursementRepository();

        //system under test
        var toTest = new ReimbursementService(inMemoryReimbursementRepository);

        //when
        List<ReimbursementReadModel> result = toTest.readAll();

        //then
        assertThat(result.size()).isEqualTo(0);
        assertThat(result).isNotNull();
    }

    @Test
    void readById() {
    }

    @Test
    void createReimbursement() {
    }

    @Test
    void updateReimbursementById() {
    }

    @Test
    void deleteReimbursementById() {
    }

    @Test
    void toEntity() {
    }
}