package pl.iseebugs.TripReimbursementApp.controller;

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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    void testReadAllUsersGroup_returnsEmptyList() throws Exception {
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
    void testReadAllReimbursements_returnsAllUsersGroups() throws Exception {
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
    void readById() {
    }

    @Test
    void createReimbursement() {
    }

    @Test
    void updateReimbursement() {
    }

    @Test
    void deleteReimbursement() {
    }
}