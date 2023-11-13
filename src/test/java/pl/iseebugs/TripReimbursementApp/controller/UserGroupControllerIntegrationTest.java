package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.UserGroupDTO;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserGroupControllerIntegrationTest {

  /*  @LocalServerPort
    private int port;*/

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGroupRepository repository;

    @Test
    void testReadAllUsersGroup_returnsEmptyList() throws Exception {
        repository.deleteAll();
        mockMvc.perform(get("/groups"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testReadAllUsersGroup_returnsAllUsersGroups() throws Exception {
        repository.deleteAll();
        setUpRepoBeforeTest();
        mockMvc.perform(get("/groups"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("foo"))
                .andExpect(jsonPath("$[1].name").value("bar"))
                .andExpect(jsonPath("$[2].name").value("foobar"));
    }

    @Test
    void testCreateUsersGroup_whenGivenIdAlreadyExist_throwsIllegalArgumentException() throws Exception {
        setUpRepoBeforeTest();
        //and
        ObjectMapper objectMapper = new ObjectMapper();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("foo");
        String json = objectMapper.writeValueAsString(userGroupDTO);

        //when
        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This User Group already exists."));
    }

    @Test
    void testCreateUsersGroup_whenEmptyNameParam_throwsIllegalArgumentException() throws Exception {
        setUpRepoBeforeTest();
        //and
        ObjectMapper objectMapper = new ObjectMapper();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("   ");
        String json = objectMapper.writeValueAsString(userGroupDTO);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group name couldn't be empty."));
    }

    @Test
    void testCreateUsersGroup_whenGivenNameHasMoreThen_100_characters_throwsIllegalArgumentException() throws Exception {
        setUpRepoBeforeTest();
        //and
        ObjectMapper objectMapper = new ObjectMapper();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        String name = createLongString(101);
        userGroupDTO.setName(name);
        String json = objectMapper.writeValueAsString(userGroupDTO);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group name is too long."));
    }

    @Test
    void testCreateUsersGroup_whenGivenNameExists_throwIllegalArgumentException() throws Exception {
        setUpRepoBeforeTest();
        //and
        ObjectMapper objectMapper = new ObjectMapper();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("foo");
        String json = objectMapper.writeValueAsString(userGroupDTO);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This User Group already exists."));
    }

    @Test
    void testCreateUsersGroup_createsUsersGroup() throws Exception {
        setUpRepoBeforeTest();
        //and
        ObjectMapper objectMapper = new ObjectMapper();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("barfoo");
        String json = objectMapper.writeValueAsString(userGroupDTO);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:" + "8080" + "/groups"));
    }

    @Test
    void testCreateUsersGroup_whenGivenNameHasMaxChar_createsUsersGroup() throws Exception {
        //given
        setUpRepoBeforeTest();
        //and
        ObjectMapper objectMapper = new ObjectMapper();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        var name = createLongString(100);
        userGroupDTO.setName(name);
        String json = objectMapper.writeValueAsString(userGroupDTO);

        //when
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:" + "8080" + "/groups"));
    }

    @Test
    @Order(1)
    void testDeleteUserGroup_deletesUserGroup() throws Exception {
        //given
        setUpRepoBeforeTest();
        /*
        DataBase
        1   - foo
        2   - bar
        3   - foobar
         */

        //when
        mockMvc.perform(delete("/groups/2")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    void testDeleteUserGroup_deletesTheFirstUserGroup() throws Exception {
         /*
        DataBase
        1   - foo
        2 - deleted in test Order(1)
        3   - foobar
         */
        //when
        mockMvc.perform(delete("/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(3)

    void testDeleteUserGroup_deletesTheLastUserGroup() throws Exception {
         /*
        DataBase
        1 - deleted in test Order(2)
        2 - deleted in test Order(1)
        3   - foobar
         */
        //when
        mockMvc.perform(delete("/groups/3")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void testDeleteUserGroup_noUserGroup_throwsUserGroupNotFoundException() throws Exception {
         /*
        DataBase
        1 - deleted in test Order(2)
        2 - deleted in test Order(1)
        3 - deleted in test Order(3)
         */
        //when
        mockMvc.perform(delete("/groups/5")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Order(5)
    void testDeleteUserGroup_deletesUserGroup_whichWasDeleted_throwsUserGroupNotFoundException() throws Exception {
         /*
        DataBase
        1 - deleted in test Order(2)
        2 - deleted in test Order(1)
        3 - deleted in test Order(3)
         */
        //when
        mockMvc.perform(delete("/groups/2")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    private String createLongString(int length){
        if (length <=0 ){
            return "";
        }
        return String.valueOf('A').repeat(length);
    }


    //    @Before
    public void setUpRepoBeforeTest(){
        UserGroupDTO  user1 = new UserGroupDTO();
        user1.setName("foo");
        repository.save(user1.toUserGroup());

        UserGroupDTO  user2 = new UserGroupDTO();
        user2.setName("bar");
        repository.save(user2.toUserGroup());

        UserGroupDTO  user3 = new UserGroupDTO();
        user3.setName("foobar");
        repository.save(user3.toUserGroup());
    }
}