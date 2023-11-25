package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.ReimbursementRepository;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.ReimbursementWriteModel;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReimbursementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository groupRepository;

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void testReadAllReimbursements_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void testReadAllReimbursements_returnsAllReimbursements() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("reimbursement_001_zeroDaysNoRefund"))
                .andExpect(jsonPath("$[0].startDate").isEmpty())
                .andExpect(jsonPath("$[0].endDate").value("2022-03-20"))
                .andExpect(jsonPath("$[0].distance").value(0))
                .andExpect(jsonPath("$[0].pushedToAccept").value(false))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].returnValue").value(0))
                .andExpect(jsonPath("$[12].returnValue").value(125));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void testReadAllByUserId_throwsReimbursementNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements/user/748"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void testReadAllByUserId_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements/user/7"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void testReadAllByUserId_returnsAllReimbursements() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements/user/5"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("reimbursement_004_zeroDaysNoRefund"))
                .andExpect(jsonPath("$[0].startDate").isEmpty())
                .andExpect(jsonPath("$[0].endDate").value("2022-03-20"))
                .andExpect(jsonPath("$[0].distance").value(0))
                .andExpect(jsonPath("$[0].pushedToAccept").value(false))
                .andExpect(jsonPath("$[0].userId").value(5))
                .andExpect(jsonPath("$[0].returnValue").value(0))
                .andExpect(jsonPath("$[1].name").value("reimbursement_005_oneDayRefund"))
                .andExpect(jsonPath("$[2].name").value("reimbursement_006_moreDaysRefund"))
                .andExpect(jsonPath("$[5].name").value("reimbursement_011_noMaxMileage"));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void testReadById_throwsReimbursementNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements/748"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reimbursement not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void testReadById_readReimbursement() throws Exception {
        //when
        mockMvc.perform(get("/reimbursements/2"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("reimbursement_002_oneDayNoRefund"))
                .andExpect(jsonPath("$.startDate").value("2022-03-21"))
                .andExpect(jsonPath("$.endDate").value("2022-03-21"))
                .andExpect(jsonPath("$.distance").value(0))
                .andExpect(jsonPath("$.pushedToAccept").value(false))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.returnValue").value(0));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void createReimbursement_throwsIllegalArgumentException() throws Exception {
        //given
        ReimbursementWriteModel reimbursementWriteModel = new ReimbursementWriteModel();
        reimbursementWriteModel.setId(1);
        reimbursementWriteModel.setName("CreateNew");
        reimbursementWriteModel.setStartDate(null);
        reimbursementWriteModel.setEndDate(LocalDate.of(2022,5,21));
        reimbursementWriteModel.setDistance(153);
        reimbursementWriteModel.setPushedToAccept(false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(reimbursementWriteModel);

        //when
        mockMvc.perform(post("/reimbursements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This Reimbursement already exists."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void createReimbursement_throwsUserNotFoundException() throws Exception {
        //given
        ReimbursementWriteModel reimbursementWriteModel = new ReimbursementWriteModel();
        reimbursementWriteModel.setName("CreateNew");
        reimbursementWriteModel.setStartDate(null);
        reimbursementWriteModel.setEndDate(LocalDate.of(2022,5,21));
        reimbursementWriteModel.setDistance(153);
        reimbursementWriteModel.setPushedToAccept(false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(reimbursementWriteModel);

        //when
        mockMvc.perform(post("/reimbursements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void createReimbursement_createsReimbursement() throws Exception {
        //given
        ReimbursementWriteModel reimbursementWriteModel = new ReimbursementWriteModel();
        reimbursementWriteModel.setName("CreateNew");
        reimbursementWriteModel.setStartDate(null);
        reimbursementWriteModel.setEndDate(LocalDate.of(2022,5,21));
        reimbursementWriteModel.setDistance(153);
        reimbursementWriteModel.setPushedToAccept(false);
        reimbursementWriteModel.setUserId(1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(reimbursementWriteModel);

        //and
        int beforeSize = reimbursementRepository.findAll().size();
        int newReimbursementId = beforeSize + 1;
        //when
        mockMvc.perform(post("/reimbursements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:8080/reimbursements/" + newReimbursementId));

        int afterSize = reimbursementRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void updateReimbursementById_throwsReimbursementNotFoundException() throws Exception {
        //given
        ReimbursementWriteModel reimbursementWriteModel = new ReimbursementWriteModel();
        reimbursementWriteModel.setId(824);
        reimbursementWriteModel.setName("CreateNew");
        reimbursementWriteModel.setStartDate(null);
        reimbursementWriteModel.setEndDate(LocalDate.of(2022,5,21));
        reimbursementWriteModel.setDistance(153);
        reimbursementWriteModel.setPushedToAccept(false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(reimbursementWriteModel);

        //when
        mockMvc.perform(put("/reimbursements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reimbursement not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void updateReimbursementById_throwsUserNotFoundException() throws Exception {
        //given
        ReimbursementWriteModel reimbursementWriteModel = new ReimbursementWriteModel();
        reimbursementWriteModel.setId(7);
        reimbursementWriteModel.setName("CreateNew");
        reimbursementWriteModel.setStartDate(null);
        reimbursementWriteModel.setEndDate(LocalDate.of(2022,5,21));
        reimbursementWriteModel.setDistance(153);
        reimbursementWriteModel.setPushedToAccept(false);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(reimbursementWriteModel);

        //when
        mockMvc.perform(put("/reimbursements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void updateReimbursementById_updatesReimbursement() throws Exception {
        //given
        ReimbursementWriteModel reimbursementWriteModel = new ReimbursementWriteModel();
        reimbursementWriteModel.setId(7);
        reimbursementWriteModel.setName("CreateNew");
        reimbursementWriteModel.setStartDate(null);
        reimbursementWriteModel.setEndDate(LocalDate.of(2022,5,21));
        reimbursementWriteModel.setDistance(153);
        reimbursementWriteModel.setPushedToAccept(false);
        reimbursementWriteModel.setUserId(2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(reimbursementWriteModel);

        //when
        mockMvc.perform(put("/reimbursements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/reimbursements/7"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("CreateNew"))
                .andExpect(jsonPath("$.startDate").isEmpty())
                .andExpect(jsonPath("$.endDate").value("2022-05-21"))
                .andExpect(jsonPath("$.distance").value(153))
                .andExpect(jsonPath("$.pushedToAccept").value(false))
                .andExpect(jsonPath("$.userId").value(2));

    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void deleteReimbursement_throwsReimbursementNotFoundException() throws Exception {
        mockMvc.perform(delete("/reimbursements/1565")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reimbursement not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/004-test-data-reimbursements.sql"})
    void deleteReimbursement_deletesReimbursement() throws Exception {
        //given
        int beforeSize = reimbursementRepository.findAll().size();
        //when
        mockMvc.perform(delete("/reimbursements/3")
                        .contentType(MediaType.APPLICATION_JSON))
            //then
            .andExpect(status().isNoContent());

        int afterSize = reimbursementRepository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }
}