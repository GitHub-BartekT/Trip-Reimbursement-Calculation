package pl.iseebugs.TripReimbursementApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void readAll_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/receipts"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void readAll_returnsObjects() throws Exception {
        //when
        mockMvc.perform(get("/receipts"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Train_AllUsers"))
                .andExpect(jsonPath("$[0].maxValue").value(100))
                .andExpect(jsonPath("$[0].userGroups[*].id", containsInAnyOrder(1, 2, 3, 4)))
                .andExpect(jsonPath("$[0].userGroups[*].name", containsInAnyOrder("CEO", "Sellers", "Regular employee", "Office employee")))
                .andExpect(jsonPath("$[1].name").value("Aeroplane_CEO"))
                .andExpect(jsonPath("$[1].maxValue").value(2000))
                .andExpect(jsonPath("$[1].userGroups[*].name", containsInAnyOrder("CEO")))
                .andExpect(jsonPath("$[2].name").value("Food_AllUsers"))
                .andExpect(jsonPath("$[2].maxValue").value(45))
                .andExpect(jsonPath("$[2].userGroups[*].name", containsInAnyOrder("CEO", "Sellers", "Regular employee", "Office employee")))
                .andExpect(jsonPath("$[3].name").value("Food_CEO"))
                .andExpect(jsonPath("$[3].maxValue").value(450))
                .andExpect(jsonPath("$[4].name").value("Hotels_Sellers"))
                .andExpect(jsonPath("$[4].maxValue").value(450))
                .andExpect(jsonPath("$[5].name").value("Other_Directors"))
                .andExpect(jsonPath("$[5].maxValue").value(450));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void readAllByUserGroup_Id_throwsUserGroupNotFound() throws Exception {
        //when
        mockMvc.perform(get("/receipts/userGroup/855"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void readAllByUserGroup_Id_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/receipts/userGroup/5"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void readAllByUserGroup_Id_returnsObjects() throws Exception {
        //when
        mockMvc.perform(get("/receipts/userGroup/3"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Train_AllUsers"))
                .andExpect(jsonPath("$[0].maxValue").value(100))
                .andExpect(jsonPath("$[0].userGroups[*].id", containsInAnyOrder(1, 2, 3, 4)))
                .andExpect(jsonPath("$[0].userGroups[*].name", containsInAnyOrder("CEO", "Sellers", "Regular employee", "Office employee")))
                .andExpect(jsonPath("$[1].name").value("Food_AllUsers"))
                .andExpect(jsonPath("$[1].maxValue").value(45))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void readById() {
    }

    @Test
    void createReceiptTypeToAllUserGroup() {
    }

    @Test
    void saveReceiptTypeWithUserGroupIds() {
    }

    @Test
    void updateReceiptTypeToAllUserGroups() {
    }

    @Test
    void updateReceiptType() {
    }

    @Test
    void deleteReceiptType() {
    }
}