package pl.iseebugs.TripReimbursementApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.iseebugs.TripReimbursementApp.model.UserGroupDTO;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGroupRepository repository;

    @Before
    private void setUpRepoBeforeTest(){
        UserGroupDTO  user = new UserGroupDTO();
        user.setName("foo");
        repository.save(user.toUserGroup());
        user.setName("bar");
        repository.save(user.toUserGroup());
        user.setName("foobar");
        repository.save(user.toUserGroup());
    }

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

}