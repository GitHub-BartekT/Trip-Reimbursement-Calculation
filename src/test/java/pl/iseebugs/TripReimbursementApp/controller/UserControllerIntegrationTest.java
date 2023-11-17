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
import pl.iseebugs.TripReimbursementApp.logic.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository groupRepository;

    @Test
    void testReadAllUsers_returnsEmptyList() throws Exception {
        userRepository.deleteAll();
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(0)
    void testReadAllUsers_returnsAllUsers() throws Exception {
        //given
        setUpRepoBeforeTest();
        //when
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(jsonPath("$[0].name").value("foo"))
                .andExpect(jsonPath("$[1].name").value("bar"))
                .andExpect(jsonPath("$[2].name").value("foobar"))
                .andExpect(jsonPath("$[1].userGroup.name").value("fooGroup"));
    }

    @Test
    void testReadById_throwsUserNotFoundException() throws Exception {
        userRepository.deleteAll();
        mockMvc.perform(get("/users/7"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Order(1)
    void testReadById_readUser() throws Exception {
        mockMvc.perform(get("/users/2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("bar"))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.userGroup.name").value("fooGroup"));
    }

    @Test
    @Order(7)
    void testCreateUser_whenGivenUserIdAlreadyExist_throwsIllegalArgumentException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(8);
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        //then
                .andExpect(status().isBadRequest())
                    .andExpect(content().string("This User already exists."));
    }

    @Test
    void testCreateUser_whenEmptyNameParam_throwsIllegalArgumentException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("   ");
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User name couldn't be empty."));
    }

    @Test
    void testCreateUser_whenGivenNameHasMoreThen_100_characters_throwsIllegalArgumentException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        String name = createLongString(101);
        userDTO.setName(name);
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User name is too long."));
    }

    @Test
    void testCreateUser_whenEmptyUserGroupParam_throwsUserGroupNotFoundException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("foo");
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    void testCreateUser_whenNoUserGroupId_throwsUserGroupNotFoundException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("foo");
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("fooGroup");
        userDTO.setUserGroup(userGroupDTO);
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Order(7)
    void testCreateUser_createsUser() throws Exception {
        //given
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("fooGroup");
        UserGroup userGroup = groupRepository.save(userGroupDTO.toUserGroup());

        UserDTO userDTO = new UserDTO();
        userDTO.setName("foo");
        userDTO.setUserGroup(new UserGroupDTO(userGroup));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);
        //and

        int beforeSize = userRepository.findAll().size();
        User user = userRepository.findAll().stream()
                .reduce((first, second) -> second).orElse(null);
        assert user != null;
        int newUserId = user.getId() + 2; //Last index was deleted in testDeleteUser_deletesTheLastUser()

        //when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isCreated())
                .andExpect(header().
                        string("Location", "http://localhost:8080/users/" + newUserId));

        int afterSize = userRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    @Order(1)
    void testDeleteUser_deletesUser() throws Exception {
        //given
        int beforeSize = userRepository.findAll().size();
        //when
        mockMvc.perform(delete("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        int afterSize = userRepository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @Order(2)
    void testDeleteUser_deletesTheFirstUser() throws Exception {
        //given
        int beforeSize = userRepository.findAll().size();
        //when
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        int afterSize = userRepository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @Order(3)
    void testDeleteUser_deletesTheLastUser() throws Exception {
        //given
        int beforeSize = userRepository.findAll().size();
        //when
        mockMvc.perform(delete("/users/9")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());

        int afterSize = userRepository.findAll().size();
        assertThat(afterSize + 1).isEqualTo(beforeSize);
    }

    @Test
    @Order(4)
    void testDeleteUser_noUser_throwsUserNotFoundException() throws Exception {
          mockMvc.perform(delete("/users/15")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Order(4)
    void testDeleteUser_tryDeletesUser_whichWasDeleted_throwsUserNotFoundException() throws Exception {
        //given
        userRepository.deleteById(5);
        //when
        mockMvc.perform(delete("/users/5")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    /*
        Users DataBase              Users Groups
        1 - deleted in Order(2)     1 - fooGroup
        2 - bar                     1
        3 - deleted in Order(1)     1
        4 - foo                     2 - barGroup
        5 - deleted in Order(4)     2
        6 - foobar                  2
        7 - foo                     3 - foobarGroup
        8 - bar                     3
        9 - deleted in Order(3)     3
    */

    @Test
    @Order(4)
    void testUpdateUserById_throwsUserNotFoundException() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(12);
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Order(5)
    void testUpdateUserById_whenEmptyNameParam_throwsIllegalArgumentException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(7);
        userDTO.setName("   ");
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User name couldn't be empty."));
    }

    @Test
    @Order(5)
    void testUpdateUserById_whenGivenNameHasMoreThen_100_characters_throwsIllegalArgumentException() throws Exception {
        //when
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        String name = createLongString(101);
        userDTO.setId(7);
        userDTO.setName(name);
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User name is too long."));
    }

    @Test
    @Order(5)
    void testUpdateUserById_whenEmptyUserGroupParam_throwsUserGroupNotFoundException() throws Exception {
        //when
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(7);
        userDTO.setName("foo");
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Group not found."));
    }

    @Test
    @Order(6)
    void testUpdateUserById_updatesUser() throws Exception {
        //given
        UserGroup userGroup = groupRepository.findById(1).orElse(null);
        assert userGroup != null;
        UserGroupDTO userGroupDTO = new UserGroupDTO(userGroup);

        int idToUpdate = 4;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(idToUpdate);
        userDTO.setName("barFoo");
        userDTO.setUserGroup(userGroupDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);

        //when
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[1].name").value("barFoo"))
                .andExpect(jsonPath("$[1].userGroup.name").value("fooGroup"))
                .andExpect(jsonPath("$[1].userGroup.id").value(1));
    }

    private String createLongString(int length){
        if (length <=0 ){
            return "";
        }
        return String.valueOf('A').repeat(length);
    }

    public void setUpRepoBeforeTest() throws UserGroupNotFoundException, UserNotFoundException {
        List<String> userGroupsNames = List.of("fooGroup", "barGroup", "foobarGroup");
        List<String> userNames = List.of("foo", "bar", "foobar");

        for (String entity : userGroupsNames) {
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            userGroupDTO.setName(entity);
            UserGroup userGroup = groupRepository.save(userGroupDTO.toUserGroup());
            for (String entityUser : userNames) {
                UserDTO user = new UserDTO();
                user.setName(entityUser);
                user.setUserGroup(new UserGroupDTO(userGroup));
                userRepository.save(user.toUser());
            }
        }
    }
}