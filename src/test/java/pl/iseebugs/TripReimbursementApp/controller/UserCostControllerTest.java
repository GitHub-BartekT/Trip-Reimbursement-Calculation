package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.UserCostRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostWriteModel;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserCostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCostRepository userCostRepository;

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void readAll_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/costs"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void readAll_returnsUserCosts() throws Exception {
        //when
        mockMvc.perform(get("/costs"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(jsonPath("$[0].name").value("Hotel"))
                .andExpect(jsonPath("$[0].costValue").value(150))
                .andExpect(jsonPath("$[0].reimbursementId").value(1))
                .andExpect(jsonPath("$[0].receiptId").value(5))
                .andExpect(jsonPath("$[1].name").value("Branch"))
                .andExpect(jsonPath("$[5].name").value("Hotel_02"))
                .andExpect(jsonPath("$", hasSize(9)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void readById_throwsUserCostNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/costs/748"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Cost not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void readById_returnsUserCost() throws Exception {
        //when
        mockMvc.perform(get("/costs/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(jsonPath("$.name").value("Hotel"))
                .andExpect(jsonPath("$.costValue").value(150))
                .andExpect(jsonPath("$.reimbursementId").value(1))
                .andExpect(jsonPath("$.receiptId").value(5));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void readAllByReimbursement_Id_throwsReimbursementNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/costs/reimbursement/748"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reimbursement not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void readAllByReimbursement_Id_returnsUserCosts() throws Exception {
        //when
        mockMvc.perform(get("/costs/reimbursement/5"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(jsonPath("$[0].name").value("Hotel"))
                .andExpect(jsonPath("$[0].costValue").value(75))
                .andExpect(jsonPath("$[0].reimbursementId").value(5))
                .andExpect(jsonPath("$[0].receiptId").value(5))
                .andExpect(jsonPath("$[1].name").value("Branch"))
                .andExpect(jsonPath("$[2].name").value("Train"))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void readAllByReceiptType_Id_throwsReceiptTypeNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/costs/receipt/748"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Receipt Type not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void readAllByReceiptType_Id_returnsUserCosts() throws Exception {
        //when
        mockMvc.perform(get("/costs" +
                        "/receipt/3"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(jsonPath("$[0].name").value("Branch"))
                .andExpect(jsonPath("$[0].costValue").value(30))
                .andExpect(jsonPath("$[0].reimbursementId").value(1))
                .andExpect(jsonPath("$[1].name").value("Branch"))
                .andExpect(jsonPath("$[1].reimbursementId").value(5))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void createUserCost_throwsIllegalArgument() throws Exception {
        //given
        UserCostWriteModel writeModel = new UserCostWriteModel();
        writeModel.setName("toCreate");
        writeModel.setId(1);
        writeModel.setCostValue(75);
        writeModel.setReimbursementId(2);
        writeModel.setReceiptTypeId(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(writeModel);

        //when
        mockMvc.perform(post("/costs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This User Cost already exists."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void createUserCost_createsUserCost() throws Exception {
        //given
        UserCostWriteModel writeModel = new UserCostWriteModel();
        writeModel.setName("toCreate");
        writeModel.setCostValue(75);
        writeModel.setReimbursementId(2);
        writeModel.setReceiptTypeId(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(writeModel);
        //and
        int beforeSize = userCostRepository.findAll().size();
        int newUserCostId = beforeSize + 1;

        //when
        mockMvc.perform(post("/costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:8080/costs/" + newUserCostId));

        int afterSize = userCostRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void updateUserCost_throwsUserCostNotFoundException() throws Exception {
        //given
        UserCostWriteModel writeModel = new UserCostWriteModel();
        writeModel.setName("toCreate");
        writeModel.setId(987);
        writeModel.setCostValue(75);
        writeModel.setReimbursementId(2);
        writeModel.setReceiptTypeId(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(writeModel);

        //when
        mockMvc.perform(put("/costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Cost not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void updateUserCost_updatesUserCost() throws Exception {
        //given
        UserCostWriteModel writeModel = new UserCostWriteModel();
        writeModel.setName("toCreate");
        writeModel.setCostValue(75);
        writeModel.setId(1);
        writeModel.setReimbursementId(2);
        writeModel.setReceiptTypeId(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(writeModel);

        //when
        mockMvc.perform(put("/costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //and
        mockMvc.perform(get("/costs/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(jsonPath("$.name").value("toCreate"))
                .andExpect(jsonPath("$.costValue").value(75))
                .andExpect(jsonPath("$.reimbursementId").value(2))
                .andExpect(jsonPath("$.receiptId").value(1));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void deleteById_throwsUserCostNotFoundException() throws Exception {
        //when
        mockMvc.perform(delete("/costs/748"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Cost not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/006-test-data-user-costs.sql"})
    void deleteById_deletesUserCost() throws Exception {
        int beforeSize = userCostRepository.findAll().size();
        mockMvc.perform(delete("/costs/3")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        int afterSize = userCostRepository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }
}