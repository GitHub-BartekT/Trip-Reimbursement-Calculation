package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.ReceiptTypeRepository;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupWriteModel;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGroupRepository repository;

    @Autowired
    private ReceiptTypeRepository receiptTypeRepository;

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void testReadAllUsersGroup_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/groups"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testReadAllUsersGroup_returnsAllUsersGroups() throws Exception {
        //when
        mockMvc.perform(get("/groups"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("fooGroup"))
                .andExpect(jsonPath("$[1].name").value("barGroup"))
                .andExpect(jsonPath("$[2].name").value("foobarGroup"));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void testReadById_throwsUserGroupNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/groups/7"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void testReadById_readUserGroup() throws Exception {
        //when
        mockMvc.perform(get("/groups/2"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Sellers"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.receiptTypes[*].id", containsInAnyOrder(1, 3, 5)))
                .andExpect(jsonPath("$.receiptTypes[*].name", containsInAnyOrder("Train_AllUsers", "Food_AllUsers", "Hotels_Sellers")))
                .andExpect(jsonPath("$.receiptTypes", hasSize(3)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testCreateUsersGroup_whenGivenNameAlreadyExist_throwsIllegalArgumentException() throws Exception {
        //given
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        toCreate.setName("fooGroup");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        //then
                .andExpect(status().isBadRequest())
                    .andExpect(content().string("User Group with that name already exist."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testCreateUsersGroup_whenEmptyNameParam_throwsIllegalArgumentException() throws Exception {
        //given
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        toCreate.setName("   ");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group name couldn't be empty."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testCreateUsersGroup_whenGivenNameHasMoreThen_100_characters_throwsIllegalArgumentException() throws Exception {
        //given
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        String name = createLongString(101);
        toCreate.setName(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group name is too long."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testCreateUsersGroup_whenGivenNameExists_throwIllegalArgumentException() throws Exception {
       //given
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        toCreate.setName("fooGroup");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group with that name already exist."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testCreateUsersGroup_createsUsersGroup() throws Exception {
        //given
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        toCreate.setName("barfoo");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:" + "8080" + "/groups/" + 4));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testCreateUsersGroup_whenGivenNameHasMaxChar_createsUsersGroup() throws Exception {
        //given
        UserGroupWriteModel toCreate = new UserGroupWriteModel();
        var name = createLongString(100);
        toCreate.setName(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toCreate);
        //and
        int beforeSize = repository.findAll().size();
        UserGroup userGroup = repository.findById(beforeSize).orElse(null);
        assert userGroup != null;
        int newUserGroupId = userGroup.getId() + 1;

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:" + "8080" + "/groups/" + newUserGroupId));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void updateUserGroupWithReceiptTypesIds_throwsUserGroupNotFoundException() throws Exception {
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(1684);
        toUpdate.setName("NewUserGroup");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        //when
        mockMvc.perform(put("/groups/1,2,3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void updateUserGroupWithReceiptTypesIds_throwsIllegalArgumentException() throws Exception {
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(2);
        toUpdate.setName("CEO");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        //when
        mockMvc.perform(put("/groups/1,2,3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group with that name already exist."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void updateReceiptType_updatesUserGroup() throws Exception {
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        String name = "toUpdate";
        int receiptId = 5;
        toUpdate.setName(name);
        toUpdate.setId(receiptId);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        //when
        mockMvc.perform(put("/groups/1,2,6,4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        //and when
        mockMvc.perform(get("/groups/" + receiptId))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(receiptId))
                .andExpect(jsonPath("$.receiptTypes[*].id", containsInAnyOrder(1, 2, 4, 6)))
                .andExpect(jsonPath("$.receiptTypes[*].name", containsInAnyOrder("Train_AllUsers", "Aeroplane_CEO", "Food_CEO", "Other_Directors")))
                .andExpect(jsonPath("$.receiptTypes", hasSize(4)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void testDeleteUserGroup_deletesUserGroup() throws Exception {
        //given
        int userGroupId = 2;
        int beforeSize = repository.findAll().size();
        int beforeReceiptNr1size = receiptTypeRepository.findAllByUserGroups_Id(userGroupId).size();
        //when
        mockMvc.perform(delete("/groups/" + userGroupId)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/groups/" + userGroupId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));

        mockMvc.perform(get("/receipts/userGroup/" + userGroupId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));

        int afterSize = repository.findAll().size();

        assertThat(afterSize + 1).isEqualTo(beforeSize);
        assertThat(beforeReceiptNr1size).isNotEqualTo(0);
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testDeleteUserGroup_deletesTheFirstUserGroup() throws Exception {
        //given
        int beforeSize = repository.findAll().size();

        //when
        mockMvc.perform(delete("/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        int afterSize = repository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testDeleteUserGroup_deletesTheLastUserGroup() throws Exception {
        //given
        int beforeSize = repository.findAll().size();

        //when
        mockMvc.perform(delete("/groups/3")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        int afterSize = repository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testDeleteUserGroup_noUserGroup_throwsUserGroupNotFoundException() throws Exception {
        //when
        mockMvc.perform(delete("/groups/10")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testDeleteUserGroup_deletesUserGroup_whichWasDeleted_throwsUserGroupNotFoundException() throws Exception {
        //given
        repository.deleteById(2);
        //when
        mockMvc.perform(delete("/groups/2")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testUpdateUserGroup_throwsUserGroupNotFoundException() throws Exception {
        //given
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(10);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);
        //when
        mockMvc.perform(put("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testUpdateUsersGroup_whenEmptyNameParam_throwsIllegalArgumentException() throws Exception {
        //given
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(2);
        toUpdate.setName("   ");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        //when
        mockMvc.perform(put("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group name couldn't be empty."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testUpdateUsersGroup_whenGivenNameHasMoreThen_100_characters_throwsIllegalArgumentException() throws Exception {
        //given
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        String name = createLongString(101);
        toUpdate.setId(2);
        toUpdate.setName(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        //when
        mockMvc.perform(put("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group name is too long."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testUpdateUsersGroup_whenGivenNameExists_throwIllegalArgumentException() throws Exception {
        //given
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(2);
        toUpdate.setName("fooGroup");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);
        //when
        mockMvc.perform(put("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group with that name already exist."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/003-test-data-user-groups.sql"})
    void testUpdateUsersGroup_updatesUsersGroup() throws Exception {
        //given
        UserGroupWriteModel toUpdate = new UserGroupWriteModel();
        toUpdate.setId(2);
        toUpdate.setName("barFoo");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);
        //when
        mockMvc.perform(put("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/groups"))
                .andExpect(jsonPath("$[1].name").value("barFoo"))
                .andExpect(jsonPath("$[1].id").value(2));
    }



    private String createLongString(int length){
        if (length <=0 ){
            return "";
        }
        return String.valueOf('A').repeat(length);
    }
}