package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.User;
import pl.iseebugs.TripReimbursementApp.model.UserGroup;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;
import pl.iseebugs.TripReimbursementApp.model.UserRepository;
import pl.iseebugs.TripReimbursementApp.model.projection.UserDTO;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository groupRepository;

    @Test
    @Sql({"/sql/001-test-schema.sql"})
    void testReadAllUsers_returnsEmptyList() throws Exception {
        //when
        mockMvc.perform(get("/users"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testReadAllUsers_returnsAllUsers() throws Exception {
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
    @Sql({"/sql/001-test-schema.sql"})
    void testReadById_throwsUserNotFoundException() throws Exception {
        //when
        mockMvc.perform(get("/users/7"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testReadById_readUser() throws Exception {
        //when
        mockMvc.perform(get("/users/2"))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("bar"))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.userGroup.name").value("fooGroup"));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testCreateUser_whenGivenUserIdAlreadyExist_throwsIllegalArgumentException() throws Exception {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(8);

        ObjectMapper objectMapper = new ObjectMapper();
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
        UserDTO userDTO = new UserDTO();
        userDTO.setName("   ");

        ObjectMapper objectMapper = new ObjectMapper();
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
        UserDTO userDTO = new UserDTO();
        String name = createLongString(101);
        userDTO.setName(name);

        ObjectMapper objectMapper = new ObjectMapper();
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
        UserDTO userDTO = new UserDTO();
        userDTO.setName("foo");

        ObjectMapper objectMapper = new ObjectMapper();
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
        UserDTO userDTO = new UserDTO();
        userDTO.setName("foo");
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setName("fooGroup");
        userDTO.setUserGroup(userGroupDTO);

        ObjectMapper objectMapper = new ObjectMapper();
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testCreateUser_createsUser() throws Exception {
        //given
        UserGroup userGroup = groupRepository.findById(1).orElse(null);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("foo");
        assert userGroup != null;
        userDTO.setUserGroup(new UserGroupDTO(userGroup));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);
        //and
        int beforeSize = userRepository.findAll().size();
        User user = userRepository.findById(beforeSize).orElse(null);

        assert user != null;
        int newUserId = user.getId() + 1;

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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testDeleteUser_noUser_throwsUserNotFoundException() throws Exception {
        //when
        mockMvc.perform(delete("/users/15")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));
    }

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testUpdateUserById_throwsUserNotFoundException() throws Exception {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(12);

        ObjectMapper objectMapper = new ObjectMapper();
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testUpdateUserById_whenEmptyNameParam_throwsIllegalArgumentException() throws Exception {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(7);
        userDTO.setName("   ");

        ObjectMapper objectMapper = new ObjectMapper();
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testUpdateUserById_whenGivenNameHasMoreThen_100_characters_throwsIllegalArgumentException() throws Exception {
        //when
        UserDTO userDTO = new UserDTO();
        String name = createLongString(101);
        userDTO.setId(7);
        userDTO.setName(name);

        ObjectMapper objectMapper = new ObjectMapper();
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
    void testUpdateUserById_whenEmptyUserGroupParam_throwsUserGroupNotFoundException() throws Exception {
        //when
        UserDTO userDTO = new UserDTO();
        userDTO.setId(7);
        userDTO.setName("foo");

        ObjectMapper objectMapper = new ObjectMapper();
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
    @Sql({"/sql/001-test-schema.sql", "/sql/002-test-data-users.sql"})
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

        mockMvc.perform(get("/users/4"))
                .andExpect(jsonPath("$.name").value("barFoo"))
                .andExpect(jsonPath("$.userGroup.name").value("fooGroup"))
                .andExpect(jsonPath("$.userGroup.id").value(1));
    }

    private String createLongString(int length){
        if (length <=0 ){
            return "";
        }
        return String.valueOf('A').repeat(length);
    }
}