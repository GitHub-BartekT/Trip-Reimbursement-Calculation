package pl.iseebugs.TripReimbursementApp.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGroupRepository repository;

    @Test
    void testReadAllUsersGroup_emptyTable() throws Exception {
        mockMvc.perform(get("/groups"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testReadAllUsersGroup() throws Exception {
        setUpRepoBeforeTest();
        mockMvc.perform(get("/groups"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("foo"))
                .andExpect(jsonPath("$[1].name").value("bar"));
    }

    @Before
    void setUpRepoBeforeTest(){
        UserGroupDTO  user = new UserGroupDTO();
        user.setName("foo");
        repository.save(user.toUserGroup());
        user.setName("bar");
        repository.save(user.toUserGroup());
    }







}