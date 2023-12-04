package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.ReceiptTypeRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.ReceiptTypeWriteModel;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReceiptTypeRepository receiptTypeRepository;

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
    @Sql({"/sql/001-test-schema.sql"})
    void readById_throwsReceiptTypeNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/receipts/855"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Receipt Type not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void readById_returnsReceiptType() throws Exception {
        //when
        mockMvc.perform(get("/receipts/2"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Aeroplane_CEO"))
                .andExpect(jsonPath("$.maxValue").value(2000))
                .andExpect(jsonPath("$.userGroups[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.userGroups[*].name", containsInAnyOrder("CEO")))
                .andExpect(jsonPath("$.userGroups", hasSize(1)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void createReceiptTypeToAllUserGroup_throwsIllegalArgumentException() throws Exception {
        ReceiptTypeWriteModel toCreate = new ReceiptTypeWriteModel();
        toCreate.setId(1);
        toCreate.setName("NewReceipt");
        toCreate.setMaxValue(167);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/receipts/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This Receipt Type already exists"));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void createReceiptTypeToAllUserGroup_createsReceiptType() throws Exception {
        ReceiptTypeWriteModel toCreate = new ReceiptTypeWriteModel();
        String name = "NewReceipt";
        toCreate.setName(name);
        toCreate.setMaxValue(167);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //and
        int beforeSize = receiptTypeRepository.findAll().size();
        int newReceiptId = beforeSize + 1;

        //when
        mockMvc.perform(post("/receipts/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:8080/receipts/" + newReceiptId));

        //and when
        mockMvc.perform(get("/receipts/" + newReceiptId))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.maxValue").value(167))
                .andExpect(jsonPath("$.userGroups", hasSize(5)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void createReceiptTypeWithUserGroupIds_throwsIllegalArgumentException() throws Exception{
        ReceiptTypeWriteModel toCreate = new ReceiptTypeWriteModel();
        toCreate.setId(1);
        toCreate.setName("NewReceipt");
        toCreate.setMaxValue(167);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/receipts?integerList=1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This Receipt Type already exists"));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void createReceiptTypeWithUserGroupIds_throwsUserGroupNotFoundException() throws Exception{
        ReceiptTypeWriteModel toCreate = new ReceiptTypeWriteModel();
        toCreate.setId(1);
        toCreate.setName("NewReceipt");
        toCreate.setMaxValue(167);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/receipts?integerList=1,25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void createReceiptTypeWithUserGroupIds_createsReceiptType() throws Exception{
        ReceiptTypeWriteModel toCreate = new ReceiptTypeWriteModel();
        String name = "NewReceipt";
        toCreate.setName(name);
        toCreate.setMaxValue(167);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);
        //and
        int beforeSize = receiptTypeRepository.findAll().size();
        int newReceiptId = beforeSize + 1;

        //when
        mockMvc.perform(post("/receipts?integerList=1,2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:8080/receipts/" + newReceiptId));

        //and when
        mockMvc.perform(get("/receipts/" + newReceiptId))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.maxValue").value(167))
                .andExpect(jsonPath("$.userGroups", hasSize(2)));
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